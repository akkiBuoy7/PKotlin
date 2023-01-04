package plm.patientslikeme2.utils

import plm.patientslikeme2.utils.enum.MobileTheme

object AppAndroidUtils {

    fun getMobileThemeColor(theme: String): String {
        var result = "#C7EAF3"
        when (theme) {
            MobileTheme.Blue.toString() -> {
                result = "#C7EAF3"
            }
            MobileTheme.Teal.toString() -> {
                result = "#C1EFE1"
            }
            MobileTheme.Pink.toString() -> {
                result = "#FCDEDE"
            }
            MobileTheme.Purple.toString() -> {
                result = "#D6E3F8"
            }
        }
        return result
    }
}