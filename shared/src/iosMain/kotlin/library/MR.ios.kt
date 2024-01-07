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
    actual object strings : ResourceContainer<StringResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object plurals : ResourceContainer<PluralsResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object images : ResourceContainer<ImageResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object fonts : ResourceContainer<FontResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object files : ResourceContainer<FileResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object colors : ResourceContainer<ColorResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() = TODO("Not yet implemented")
    }

    actual object assets : ResourceContainer<AssetResource> {
        override val nsBundle: platform.Foundation.NSBundle
            get() {
                TODO()
            }
    }
}