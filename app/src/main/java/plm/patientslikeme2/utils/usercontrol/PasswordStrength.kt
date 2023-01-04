package plm.patientslikeme2.utils.usercontrol

import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.MyApplication

enum class PasswordStrength(var msg: Int, var color: Int) {
    // we use some color in green tint =>
    //more secure is the password, more darker is the color associated

    WEAK(R.string.low, MyApplication.instance.getColor(R.color.red_error)),
    MEDIUM(R.string.medium, MyApplication.instance.getColor(R.color.gold_dark)),
    STRONG(R.string.strong, MyApplication.instance.getColor(R.color.data_moderate)),
    VERY_STRONG(R.string.strong, MyApplication.instance.getColor(R.color.green_success));

    companion object {
        private const val MIN_LENGTH = 8
        private const val MAX_LENGTH = 15
        fun calculate(password: String): PasswordStrength {
            var score = 0
            // boolean indicating if password has an upper case
            var upper = false
            // boolean indicating if password has a lower case
            var lower = false
            // boolean indicating if password has at least one digit
            var digit = false
            // boolean indicating if password has a leat one special char
            var specialChar = false
            for (element in password) {
                if (!specialChar && !Character.isLetterOrDigit(element)) {
                    score++
                    specialChar = true
                } else {
                    if (!digit && Character.isDigit(element)) {
                        score++
                        digit = true
                    } else {
                        if (!upper || !lower) {
                            if (Character.isUpperCase(element)) {
                                upper = true
                            } else {
                                lower = true
                            }
                            if (upper && lower) {
                                score++
                            }
                        }
                    }
                }
            }
            val length = password.length
            if (length > MAX_LENGTH) {
                score++
            } else if (length < MIN_LENGTH) {
                score = 0
            }
            when (score) {
                0 -> return WEAK
                1 -> return MEDIUM
                2 -> return STRONG
                3 -> return VERY_STRONG
                else -> {
                }
            }
            return VERY_STRONG
        }
    }
}