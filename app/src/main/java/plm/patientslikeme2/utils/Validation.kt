package plm.patientslikeme2.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validation {

    /*
    Email validation
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    /*
    Username validation
    */
    fun isValidUsername(username: String): Boolean {
        val pattern = Pattern.compile("^[A-Za-z]\\w{5,29}$")
        val matcher = pattern.matcher(username)
        return matcher.matches()
    }

    /*
    Password validation
     */
    fun isValidPassword(input: String): Boolean {
        val specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"
        var currentCharacter: Char
        var numberPresent = false
        var upperCasePresent = false
        var lowerCasePresent = false
        var specialCharacterPresent = false
        for (element in input) {
            currentCharacter = element
            when {
                Character.isDigit(currentCharacter) -> {
                    numberPresent = true
                }
                Character.isUpperCase(currentCharacter) -> {
                    upperCasePresent = true
                }
                Character.isLowerCase(currentCharacter) -> {
                    lowerCasePresent = true
                }
                specialChars.contains(currentCharacter.toString()) -> {
                    specialCharacterPresent = true
                }
            }
        }

        var count = 0
        if (numberPresent) count++
        if (upperCasePresent) count++
        if (lowerCasePresent) count++
        if (specialCharacterPresent) count++

        return count >= 2
    }
}