package util

import android.content.Context
import android.os.Environment
import constants.AppConstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import model.Recognizer
import model.WavModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.audio.classifier.Classifications
import org.tensorflow.lite.task.core.BaseOptions
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.text.DecimalFormat
import java.util.PriorityQueue


class AinAudioClassifier(useGpu: Boolean = false): KoinComponent {
    private val context: Context by inject()
    private val modelFile = "tflite_audio_model.tflite"
    private val tag = "AinAudioClassifier"
    private var classifierOption = if (useGpu) {
        AudioClassifier.AudioClassifierOptions
            .builder()
            .setBaseOptions(BaseOptions.builder().useGpu().build())
            .build()
    } else {
        AudioClassifier.AudioClassifierOptions
            .builder()
            .setBaseOptions(BaseOptions.builder().useNnapi().build())
            .build()
    }
    private val classifier = AudioClassifier.createFromFileAndOptions(context, modelFile, classifierOption)
    private val audioTensor = classifier.createInputTensorAudio()
    private val record = classifier.createAudioRecord()
    private val result = MutableStateFlow(mutableListOf<Classifications>())
    private val numberOfThread = 2

    private fun loadAudioSample(){
        audioTensor.load(record);
    }

    fun startRecording() {
        record.startRecording()
    }

    fun stopRecording() {
        record.stop()
    }

    fun showResult() {
        result.update { classifier.classify(audioTensor) }
    }

    fun execute(): String? {
        val sampleAudioPath = Environment.getExternalStorageDirectory().toString() +
                "/Recordings/Pitch Estimator/pianos-by-jtwayne-7-174717.wav"
        val wavModel = WavModel()
        try {
            val wavFile = WavFile.openWavFile(File(sampleAudioPath))
            val numberOfFrame = wavFile.numberOfFrames.toInt()
            val sampleRate = wavFile.sampleRate.toInt()
            val channel = wavFile.channels
            val buffer = Array(channel) {
                DoubleArray(numberOfFrame)
            }
            val loopCounter = numberOfFrame * channel / 4096 + 1
            wavModel.update(
                WavModel(
                    numberOfFrame = numberOfFrame,
                    sampleRate = sampleRate,
                    channel = channel,
                    buffer = buffer,
                    frameOffside = 0,
                    loopCounter = loopCounter
                )
            )
            for (index in AppConstant.DEFAULT_INTEGER_VALUE until loopCounter) {
                wavModel.update(frameOffside = wavFile.readFrames(
                    wavModel.buffer,
                    wavModel.numberOfFrame,
                    wavModel.frameOffside
                ))
            }

            //trimming the magnitude values to 5 decimal digits
            val df = DecimalFormat("#.#####")
            df.setRoundingMode(RoundingMode.CEILING)
            val meanBuffer = DoubleArray(wavModel.numberOfFrame)

            for (index in AppConstant.DEFAULT_INTEGER_VALUE until wavModel.numberOfFrame) {
                var frameVal = 0.0
                for (innerIndex in AppConstant.DEFAULT_INTEGER_VALUE until wavModel.channel) {
                    frameVal += buffer[innerIndex][index]
                }
                meanBuffer[index] = df.format(frameVal / wavModel.channel).toDouble()
            }

            //MFCC java library.
            val mfccConvert = MFCC()
            mfccConvert.setSampleRate(wavModel.sampleRate)
            val nMFCC = 40
            mfccConvert.setN_mfcc(nMFCC)
            val mfccInput = mfccConvert.process(meanBuffer)
            val nFFT = mfccInput.size / nMFCC
            val mfccValues = Array(nMFCC) { DoubleArray(nFFT) }

            //loop to convert the mfcc values into multi-dimensional array
            for (index in AppConstant.DEFAULT_INTEGER_VALUE until nFFT) {
                var indexCounter = index * nMFCC
                val rowIndexValue = index % nFFT
                for (innerIndex in 0 until nMFCC) {
                    mfccValues[innerIndex][rowIndexValue] = mfccInput[indexCounter].toDouble()
                    indexCounter++
                }
            }

            //code to take the mean of mfcc values across the rows such that
            //[nMFCC x nFFT] matrix would be converted into
            //[nMFCC x 1] dimension - which would act as an input to tflite model
            wavModel.update(meanMFCCValue = FloatArray(nMFCC))
            for (index in 0 until nMFCC) {
                var fftValAcrossRow = 0.0
                for (innerIndex in 0 until nFFT) {
                    fftValAcrossRow += mfccValues[index][innerIndex]
                }
                val fftMeanValAcrossRow = fftValAcrossRow / nFFT
                wavModel.meanMFCCValue[index] = fftMeanValAcrossRow.toFloat()
            }
        } catch (e: IOException) {
            Timber.tag(tag).e("Unable to execute:\n${e.printStackTrace()}")
        } catch (e: WavFileException) {
            Timber.tag(tag).e("Unable to execute:\n${e.printStackTrace()}")
        } catch (e: Exception) {
            Timber.tag(tag).e("Unable to execute:\n${e.printStackTrace()}")
        }

        val output = loadModelAndMakePredictions(wavModel.meanMFCCValue)
        return output
    }

    private fun loadModelAndMakePredictions(meanMFCCValues : FloatArray) : String? {
        var predictedResult: String? = "unknown"
        try {
            //load the TFLite model in 'MappedByteBuffer' format using TF Interpreter
            val tfliteModel: MappedByteBuffer = FileUtil.loadMappedFile(context, modelFile)
            val tflite: InterpreterApi

            /** Options for configuring the Interpreter.  */
            val tfliteOptions = InterpreterApi
                .Options()
                .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
            tfliteOptions.setNumThreads(numberOfThread)
            tflite = InterpreterApi.create(tfliteModel, tfliteOptions)
            //obtain the input and output tensor size required by the model
            //for urban sound classification, input tensor should be of 1x40x1x1 shape
            val imageTensorIndex = 0
            val imageShape =
                tflite.getInputTensor(imageTensorIndex).shape()
            val imageDataType: DataType = tflite.getInputTensor(imageTensorIndex).dataType()
            val probabilityTensorIndex = 0
            val probabilityShape =
                tflite.getOutputTensor(probabilityTensorIndex).shape()
            val probabilityDataType: DataType =
                tflite.getOutputTensor(probabilityTensorIndex).dataType()

            //need to transform the MFCC 1d float buffer into 1x40x1x1 dimension tensor using TensorBuffer
            val inBuffer: TensorBuffer = TensorBuffer.createDynamic(imageDataType)
            inBuffer.loadArray(meanMFCCValues, imageShape)
            val inpBuffer: ByteBuffer = inBuffer.buffer
            val outputTensorBuffer: TensorBuffer =
                TensorBuffer.createFixedSize(probabilityShape, probabilityDataType)
            //run the predictions with input and output buffer tensors to get probability values across the labels
            tflite.run(inpBuffer, outputTensorBuffer.buffer)


            //Code to transform the probability predictions into label values
            val ASSOCIATED_AXIS_LABELS = "labels.txt"
            var associatedAxisLabels: List<String?>? = null
            try {
                associatedAxisLabels = FileUtil.loadLabels(context, ASSOCIATED_AXIS_LABELS)
            } catch (e: IOException) {
                Timber.tag(tag).e("Error reading label file\n$e")
            }

            //Tensor processor for processing the probability values and to sort them based on the descending order of probabilities
            val probabilityProcessor: TensorProcessor = TensorProcessor.Builder()
                .add(NormalizeOp(0.0f, 255.0f)).build()
            if (null != associatedAxisLabels) {
                // Map of labels and their corresponding probability
                val labels = TensorLabel(
                    associatedAxisLabels,
                    probabilityProcessor.process(outputTensorBuffer)
                )

                // Create a map to access the result based on label
                val floatMap: Map<String, Float> = labels.mapWithFloatValue

                //function to retrieve the top K probability values, in this case 'k' value is 1.
                //retrieved values are storied in 'Recognition' object with label details.
                val resultPrediction: List<Recognizer> = getTopKProbability(floatMap);

                //get the top 1 prediction from the retrieved list of top predictions
                predictedResult = getPredictedValue(resultPrediction)

            }
            return predictedResult
        } catch (e: Exception) {
            Timber.tag(tag).e("unable to create the interpreter\n${e.message}")
        }
        return predictedResult
    }

    fun getPredictedValue(predictedList:List<Recognizer>?): String?{
        val top1PredictedValue : Recognizer? = predictedList?.get(0)
        return top1PredictedValue?.title
    }

    /** Gets the top-k results.  */
    private fun getTopKProbability(labelProb: Map<String, Float>): List<Recognizer> {
        // Find the best classifications.
        val maximumValue: Int = 1
        val pq: PriorityQueue<Recognizer> = PriorityQueue(
            maximumValue
        ) { lhs, rhs -> // Intentionally reversed to put high confidence at the head of the queue.
            rhs.confidence.compareTo(lhs.confidence)
        }
        for (entry in labelProb.entries) {
            pq.add(Recognizer(AppConstant.DEFAULT_STRING_VALUE + entry.key, entry.key, entry.value))
        }
        val recognitions: ArrayList<Recognizer> = ArrayList()
        val recognitionsSize: Int = pq.size.coerceAtMost(maximumValue)
        for (i in 0 until recognitionsSize) {
            pq.poll()?.let { recognitions.add(it) }
        }
        return recognitions
    }

}