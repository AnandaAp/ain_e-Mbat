package util

import constants.AppConstant
import constants.GendherBarung

object GendherBarung {
    object Slendro: GamelanConverter() {
        override fun convert(hertz: Float): String {
            return if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Y_BAWAH),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Y_BAWAH)
                )) {
                GendherBarung.Slendro.NAME.Y_BAWAH
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Q),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Q)
                )) {
                GendherBarung.Slendro.NAME.Q
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.W),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.W)
                )) {
                GendherBarung.Slendro.NAME.W
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.E),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.E)
                )) {
                GendherBarung.Slendro.NAME.E
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.T),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.T)
                )) {
                GendherBarung.Slendro.NAME.T
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Y_ATAS),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.Y_ATAS)
                )) {
                GendherBarung.Slendro.NAME.Y_ATAS
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.ONE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.ONE)
                )) {
                GendherBarung.Slendro.NAME.ONE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.TWO),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.TWO)
                )) {
                GendherBarung.Slendro.NAME.TWO
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.THREE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.THREE)
                )) {
                GendherBarung.Slendro.NAME.THREE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.FIVE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.FIVE)
                )) {
                GendherBarung.Slendro.NAME.FIVE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.SIX),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.SIX)
                )) {
                GendherBarung.Slendro.NAME.SIX
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.EXCLAMATION),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.EXCLAMATION)
                )) {
                GendherBarung.Slendro.NAME.EXCLAMATION
            }  else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.ANNOTATION),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.ANNOTATION)
                )) {
                GendherBarung.Slendro.NAME.ANNOTATION
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.Slendro.FREQUENCY.HASHTAG),
                    maximumValue = getPlusTolerateHertz(GendherBarung.Slendro.FREQUENCY.HASHTAG)
                )) {
                GendherBarung.Slendro.NAME.HASHTAG
            } else {
                AppConstant.DEFAULT_STRING_VALUE
            }
        }

        fun pitch(hertz: Float) = convert(hertz = hertz)
    }

    object PelogNem: GamelanConverter() {
        override fun convert(hertz: Float): String {
            return if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Y_BAWAH),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Y_BAWAH)
                )) {
                GendherBarung.PelogNem.NAME.Y_BAWAH
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Q),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Q)
                )) {
                GendherBarung.PelogNem.NAME.Q
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.W),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.W)
                )) {
                GendherBarung.PelogNem.NAME.W
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.E),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.E)
                )) {
                GendherBarung.PelogNem.NAME.E
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.T),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.T)
                )) {
                GendherBarung.PelogNem.NAME.T
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Y_ATAS),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.Y_ATAS)
                )) {
                GendherBarung.PelogNem.NAME.Y_ATAS
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.ONE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.ONE)
                )) {
                GendherBarung.PelogNem.NAME.ONE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.TWO),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.TWO)
                )) {
                GendherBarung.PelogNem.NAME.TWO
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.THREE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.THREE)
                )) {
                GendherBarung.PelogNem.NAME.THREE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.FIVE),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.FIVE)
                )) {
                GendherBarung.PelogNem.NAME.FIVE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.SIX),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.SIX)
                )) {
                GendherBarung.PelogNem.NAME.SIX
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.EXCLAMATION),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.EXCLAMATION)
                )) {
                GendherBarung.PelogNem.NAME.EXCLAMATION
            }  else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.ANNOTATION),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.ANNOTATION)
                )) {
                GendherBarung.PelogNem.NAME.ANNOTATION
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.HASHTAG),
                    maximumValue = getPlusTolerateHertz(GendherBarung.PelogNem.FREQUENCY.HASHTAG)
                )) {
                GendherBarung.PelogNem.NAME.HASHTAG
            } else {
                AppConstant.DEFAULT_STRING_VALUE
            }
        }
        fun pitch(hertz: Float) = Slendro.convert(hertz = hertz)
    }
}