package com.eklitstudio.advokatkotilin.validator

import android.text.TextUtils
import java.util.regex.Pattern

class Validator {

    companion object{

        fun isNumberValid(number: String): Boolean{
            val expresion = "^[0-9]{1,8}\$"
            val pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(number)
            return matcher.matches()
        }

        /**
         * Validate the username
         * @param username the entered username
         * @return validated or not.
         */
        fun isUsernameValid(username: String): Boolean {
            val expression = "[a-zA-Z0-9\\._\\-]{3,}"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(username)
            return matcher.matches()
        }

        /**
         * Validate the email
         * @param email the entered email
         * @return validated or not.
         */
        fun isEmailValid(email: String): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun isValidPhone(phone: CharSequence): Boolean {
            return if (TextUtils.isEmpty(phone)) {
                false
            } else {
                android.util.Patterns.PHONE.matcher(phone).matches()
            }
        }
    }
}