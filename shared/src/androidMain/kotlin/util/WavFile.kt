package util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


/*
Source based on https://github.com/Semantive/waveform-android/blob/master/library/src/main/java/com/semantive/waveformandroid/waveform/soundfile/WavFile.java
*/
@Parcelize
class WavFile private constructor() : Parcelable {
    private enum class IOState {
        READING, WRITING, CLOSED
    }

    private var file: File? = null // File that will be read from or written to
    private var ioState: IOState? =
        null // Specifies the IO State of the Wav File (used for snaity checking)
    private var bytesPerSample = 0 // Number of bytes required to store a single sample
    var numberOfFrames: Long = 0 // Number of frames within the data section
        private set
    private var oStream: FileOutputStream? = null // Output stream used for writting data
    private var iStream: FileInputStream? = null // Input stream used for reading data
    private var floatScale = 0f // Scaling factor used for int <-> float conversion
    private var floatOffset = 0f // Offset factor used for int <-> float conversion
    private val wordAlignAdjust =
        false // Specify if an extra byte at the end of the data chunk is required for word alignment

    // Wav Header
    var channels: Int = 0 // 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
        private set
    var sampleRate: Long = 0 // 4 bytes unsigned, 0x00000001 (1) to 0xFFFFFFFF (4,294,967,295)
        private set

    // Although a java int is 4 bytes, it is signed, so need to use a long
    private var blockAlign = 0 // 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
    var validBits: Int = 0 // 2 bytes unsigned, 0x0002 (2) to 0xFFFF (65,535)
        private set

    // Buffering
    private val buffer: ByteArray // Local buffer used for IO
    private var bufferPointer = 0 // Points to the current position in local buffer
    private var bytesRead = 0 // Bytes read after last read into local buffer
    private var frameCounter: Long = 0 // Current number of frames read or written
    var fileSize: Long = 0
        private set

    // Cannot instantiate WavFile directly, must either use newWavFile() or openWavFile()
    init {
        buffer = ByteArray(BUFFER_SIZE)
    }

    val framesRemaining: Long
        get() = numberOfFrames - frameCounter

    val duration: Long
        get() = numberOfFrames / sampleRate

    @Throws(IOException::class, WavFileException::class)
    private fun readSample(): Double {
        var `val`: Long = 0

        for (b in 0 until bytesPerSample) {
            if (bufferPointer == bytesRead) {
                val read = iStream!!.read(buffer, 0, BUFFER_SIZE)
                if (read == -1) throw WavFileException("Not enough data available")
                bytesRead = read
                bufferPointer = 0
            }

            var v = buffer[bufferPointer].toInt()
            if (b < bytesPerSample - 1 || bytesPerSample == 1) v = v and 0xFF
            `val` += (v shl (b * 8)).toLong()

            bufferPointer++
        }

        return `val` / 32767.0
    }

    @Throws(IOException::class, WavFileException::class)
    fun readFrames(sampleBuffer: FloatArray, numFramesToRead: Int): Int {
        return readFramesInternal(sampleBuffer, 0, numFramesToRead)
    }

    @Throws(IOException::class, WavFileException::class)
    private fun readFramesInternal(
        sampleBuffer: FloatArray,
        offset: Int,
        numFramesToRead: Int
    ): Int {
        var offset = offset
        if (ioState != IOState.READING) throw IOException("Cannot read from WavFile instance")

        for (f in 0 until numFramesToRead) {
            if (frameCounter == numberOfFrames) return f

            for (c in 0 until channels) {
                sampleBuffer[offset] = floatOffset + readSample().toFloat() / floatScale
                offset++
            }

            frameCounter++
        }

        return numFramesToRead
    }

    @Throws(IOException::class, WavFileException::class)
    fun readFrames(sampleBuffer: Array<DoubleArray>, numFramesToRead: Int, frameOffset: Int): Int {
        return readFramesInternal(sampleBuffer, frameOffset, numFramesToRead)
    }

    @Throws(IOException::class, WavFileException::class)
    private fun readFramesInternal(
        sampleBuffer: Array<DoubleArray>,
        frameOffset: Int,
        numFramesToRead: Int
    ): Int {
        var frameOffset = frameOffset
        if (ioState != IOState.READING) throw IOException("Cannot read from WavFile instance")

        for (f in 0 until numFramesToRead) {
            if (frameCounter == numberOfFrames) return frameOffset

            for (c in 0 until channels) {
                sampleBuffer[c][frameOffset] = readSample()
            }
            frameCounter++
            frameOffset++
        }

        return frameOffset
    }

    @Throws(IOException::class)
    fun close() {
        // Close the input stream and set to null
        if (iStream != null) {
            iStream!!.close()
            iStream = null
        }

        if (oStream != null) {
            // Write out anything still in the local buffer
            if (bufferPointer > 0) oStream!!.write(buffer, 0, bufferPointer)

            // If an extra byte is required for word alignment, add it to the end
            if (wordAlignAdjust) oStream!!.write(0)

            // Close the stream and set to null
            oStream!!.close()
            oStream = null
        }

        // Flag that the stream is closed
        ioState = IOState.CLOSED
    }

    companion object {
        private const val BUFFER_SIZE = 4096

        private const val FMT_CHUNK_ID = 0x20746D66
        private const val DATA_CHUNK_ID = 0x61746164
        private const val RIFF_CHUNK_ID = 0x46464952
        private const val RIFF_TYPE_ID = 0x45564157

        @Throws(IOException::class, WavFileException::class)
        fun openWavFile(file: File): WavFile {
            // Instantiate new Wavfile and store the file reference
            val wavFile = WavFile()
            wavFile.file = file

            // Create a new file input stream for reading file data
            wavFile.iStream = FileInputStream(file)

            // Read the first 12 bytes of the file
            var bytesRead = wavFile.iStream!!.read(wavFile.buffer, 0, 12)
            if (bytesRead != 12) throw WavFileException("Not enough wav file bytes for header")

            // Extract parts from the header
            val riffChunkID = getLE(wavFile.buffer, 0, 4)
            var chunkSize = getLE(wavFile.buffer, 4, 4)
            val riffTypeID = getLE(wavFile.buffer, 8, 4)

            // Check the header bytes contains the correct signature
            if (riffChunkID != RIFF_CHUNK_ID.toLong()) throw WavFileException("Invalid Wav Header data, incorrect riff chunk ID")
            if (riffTypeID != RIFF_TYPE_ID.toLong()) throw WavFileException("Invalid Wav Header data, incorrect riff type ID")

            // Check that the file size matches the number of bytes listed in header
            if (file.length() != chunkSize + 8) {
                throw WavFileException("Header chunk size (" + chunkSize + ") does not match file size (" + file.length() + ")")
            }

            wavFile.fileSize = chunkSize

            var foundFormat = false
            var foundData = false

            // Search for the Format and Data Chunks
            while (true) {
                // Read the first 8 bytes of the chunk (ID and chunk size)
                bytesRead = wavFile.iStream!!.read(wavFile.buffer, 0, 8)
                if (bytesRead == -1) throw WavFileException("Reached end of file without finding format chunk")
                if (bytesRead != 8) throw WavFileException("Could not read chunk header")

                // Extract the chunk ID and Size
                val chunkID = getLE(wavFile.buffer, 0, 4)
                chunkSize = getLE(wavFile.buffer, 4, 4)

                // Word align the chunk size
                // chunkSize specifies the number of bytes holding data. However,
                // the data should be word aligned (2 bytes) so we need to calculate
                // the actual number of bytes in the chunk
                var numChunkBytes = if ((chunkSize % 2 == 1L)) chunkSize + 1 else chunkSize

                if (chunkID == FMT_CHUNK_ID.toLong()) {
                    // Flag that the format chunk has been found
                    foundFormat = true

                    // Read in the header info
                    bytesRead = wavFile.iStream!!.read(wavFile.buffer, 0, 16)

                    // Check this is uncompressed data
                    val compressionCode = getLE(wavFile.buffer, 0, 2).toInt()
                    if (compressionCode != 1) throw WavFileException("Compression Code $compressionCode not supported")

                    // Extract the format information
                    wavFile.channels = getLE(wavFile.buffer, 2, 2).toInt()
                    wavFile.sampleRate = getLE(wavFile.buffer, 4, 4)
                    wavFile.blockAlign = getLE(wavFile.buffer, 12, 2).toInt()
                    wavFile.validBits = getLE(wavFile.buffer, 14, 2).toInt()

                    if (wavFile.channels == 0) throw WavFileException("Number of channels specified in header is equal to zero")
                    if (wavFile.blockAlign == 0) throw WavFileException("Block Align specified in header is equal to zero")
                    if (wavFile.validBits < 2) throw WavFileException("Valid Bits specified in header is less than 2")
                    if (wavFile.validBits > 64) throw WavFileException("Valid Bits specified in header is greater than 64, this is greater than a long can hold")

                    // Calculate the number of bytes required to hold 1 sample
                    wavFile.bytesPerSample = (wavFile.validBits + 7) / 8
                    if (wavFile.bytesPerSample * wavFile.channels != wavFile.blockAlign) throw WavFileException(
                        "Block Align does not agree with bytes required for validBits and number of channels"
                    )

                    // Account for number of format bytes and then skip over
                    // any extra format bytes
                    numChunkBytes -= 16
                    if (numChunkBytes > 0) wavFile.iStream!!.skip(numChunkBytes)
                } else if (chunkID == DATA_CHUNK_ID.toLong()) {
                    // Check if we've found the format chunk,
                    // If not, throw an exception as we need the format information
                    // before we can read the data chunk
                    if (foundFormat == false) throw WavFileException("Data chunk found before Format chunk")

                    // Check that the chunkSize (wav data length) is a multiple of the
                    // block align (bytes per frame)
                    if (chunkSize % wavFile.blockAlign != 0L) throw WavFileException("Data Chunk size is not multiple of Block Align")

                    // Calculate the number of frames
                    wavFile.numberOfFrames = chunkSize / wavFile.blockAlign

                    // Flag that we've found the wave data chunk
                    foundData = true

                    /* byte[] data = null;
                final ByteArrayOutputStream baout = new ByteArrayOutputStream();
                int c;
                while ((c = wavFile.iStream.read(wavFile.buffer, 0, 16)) != -1) {
	                baout.write(wavFile.buffer, 0, c);
	            }

	            baout.close();
	            data = baout.toByteArray();

	            double[] doubleInputBuffer = new double[data.length];
	            int j = 0;
	   		 	for (int i = 0; i < data.length; i++) {
	   		 	    doubleInputBuffer[i] = data[i] / 32767.0;
	   		 	    j = j + 1;
	   		      }


	   		 	MFCC mfccConvert = new MFCC();
	   		 	float[] mfccInput = mfccConvert.process(doubleInputBuffer);
	   		 	System.out.println(1);

                */
                    break
                } else {
                    // If an unknown chunk ID is found, just skip over the chunk data
                    wavFile.iStream!!.skip(numChunkBytes)
                }
            }

            // Throw an exception if no data chunk has been found
            if (foundData == false) {
                throw WavFileException("Did not find a data chunk")
            }

            // Calculate the scaling factor for converting to a normalised double
            if (wavFile.validBits > 8) {
                // If more than 8 validBits, data is signed
                // Conversion required dividing by magnitude of max negative value
                wavFile.floatOffset = 0f
                wavFile.floatScale = (1 shl (wavFile.validBits - 1)).toFloat()
            } else {
                // Else if 8 or less validBits, data is unsigned
                // Conversion required dividing by max positive value
                wavFile.floatOffset = -1f
                wavFile.floatScale = 0.5f * ((1 shl wavFile.validBits) - 1)
            }

            wavFile.bufferPointer = 0
            wavFile.bytesRead = 0
            wavFile.frameCounter = 0
            wavFile.ioState = IOState.READING

            return wavFile
        }

        private fun getLE(buffer: ByteArray, pos: Int, numBytes: Int): Long {
            var outputPos = pos
            var outputNumBytes = numBytes
            outputNumBytes--
            outputPos += outputNumBytes

            var output = (buffer[outputPos].toInt() and 0xFF).toLong()
            for (b in 0 until outputNumBytes) output = (output shl 8) + (buffer[--outputPos].toInt() and 0xFF)

            return output
        }
    }
}