package library

import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.ResourceContainer
import dev.icerock.moko.resources.StringResource

actual object MR {
    actual object strings : ResourceContainer<StringResource>
    actual object plurals : ResourceContainer<PluralsResource>
    actual object images : ResourceContainer<ImageResource>
    actual object fonts : ResourceContainer<FontResource>
    actual object files : ResourceContainer<FileResource>
    actual object colors : ResourceContainer<ColorResource>
    actual object assets : ResourceContainer<AssetResource>
}