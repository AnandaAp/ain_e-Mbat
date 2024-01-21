package util.handler.handler

import constants.AppConstant
import constants.GendherBarungConstant
import util.GamelanConverter
import util.getMinusTolerateHertz
import util.getPlusTolerateHertz
import util.rangeOf

object GendherBarung {
    object Slendro: GamelanConverter() {
        override fun convert(hertz: Float): String {
            return if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Y_BAWAH),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Y_BAWAH)
                )) {
                GendherBarungConstant.Slendro.LARAS.Y_BAWAH
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Q),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Q)
                )) {
                GendherBarungConstant.Slendro.LARAS.Q
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.W),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.W)
                )) {
                GendherBarungConstant.Slendro.LARAS.W
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.E),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.E)
                )) {
                GendherBarungConstant.Slendro.LARAS.E
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.T),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.T)
                )) {
                GendherBarungConstant.Slendro.LARAS.T
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Y_ATAS),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.Y_ATAS)
                )) {
                GendherBarungConstant.Slendro.LARAS.Y_ATAS
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.ONE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.ONE)
                )) {
                GendherBarungConstant.Slendro.LARAS.ONE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.TWO),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.TWO)
                )) {
                GendherBarungConstant.Slendro.LARAS.TWO
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.THREE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.THREE)
                )) {
                GendherBarungConstant.Slendro.LARAS.THREE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.FIVE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.FIVE)
                )) {
                GendherBarungConstant.Slendro.LARAS.FIVE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.SIX),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.SIX)
                )) {
                GendherBarungConstant.Slendro.LARAS.SIX
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.EXCLAMATION),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.EXCLAMATION)
                )) {
                GendherBarungConstant.Slendro.LARAS.EXCLAMATION
            }  else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.ANNOTATION),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.ANNOTATION)
                )) {
                GendherBarungConstant.Slendro.LARAS.ANNOTATION
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.HASHTAG),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.Slendro.FREQUENCY.HASHTAG)
                )) {
                GendherBarungConstant.Slendro.LARAS.HASHTAG
            } else {
                AppConstant.DEFAULT_STRING_VALUE
            }
        }

        fun pitch(hertz: Float) = convert(hertz = hertz)
    }

    object PelogNem: GamelanConverter() {
        override fun convert(hertz: Float): String {
            return if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Y_BAWAH),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Y_BAWAH)
                )) {
                GendherBarungConstant.PelogNem.LARAS.Y_BAWAH
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Q),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Q)
                )) {
                GendherBarungConstant.PelogNem.LARAS.Q
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.W),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.W)
                )) {
                GendherBarungConstant.PelogNem.LARAS.W
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.E),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.E)
                )) {
                GendherBarungConstant.PelogNem.LARAS.E
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.T),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.T)
                )) {
                GendherBarungConstant.PelogNem.LARAS.T
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Y_ATAS),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.Y_ATAS)
                )) {
                GendherBarungConstant.PelogNem.LARAS.Y_ATAS
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.ONE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.ONE)
                )) {
                GendherBarungConstant.PelogNem.LARAS.ONE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.TWO),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.TWO)
                )) {
                GendherBarungConstant.PelogNem.LARAS.TWO
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.THREE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.THREE)
                )) {
                GendherBarungConstant.PelogNem.LARAS.THREE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.FIVE),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.FIVE)
                )) {
                GendherBarungConstant.PelogNem.LARAS.FIVE
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.SIX),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.SIX)
                )) {
                GendherBarungConstant.PelogNem.LARAS.SIX
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.EXCLAMATION),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.EXCLAMATION)
                )) {
                GendherBarungConstant.PelogNem.LARAS.EXCLAMATION
            }  else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.ANNOTATION),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.ANNOTATION)
                )) {
                GendherBarungConstant.PelogNem.LARAS.ANNOTATION
            } else if (hertz.rangeOf(
                    minimumValue = getMinusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.HASHTAG),
                    maximumValue = getPlusTolerateHertz(GendherBarungConstant.PelogNem.FREQUENCY.HASHTAG)
                )) {
                GendherBarungConstant.PelogNem.LARAS.HASHTAG
            } else {
                AppConstant.DEFAULT_STRING_VALUE
            }
        }
        fun pitch(hertz: Float) = Slendro.convert(hertz = hertz)
    }
}