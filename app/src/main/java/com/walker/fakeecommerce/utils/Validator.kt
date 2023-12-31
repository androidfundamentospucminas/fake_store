package com.walker.fakeecommerce.utils

import android.net.Uri

object Validator {
    fun validateName(name: String): ValidationResult {
        return ValidationResult(
            (!name.isNullOrEmpty() && name.length >= 2)
        )

    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty())
        )
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 4)
        )
    }

    fun validatePrivacyPolicyAcceptance(statusValue:Boolean):ValidationResult{
        return ValidationResult(
            statusValue
        )
    }

    fun validateImage(image: Uri): ValidationResult {
        return ValidationResult(
            image.toString().isNotEmpty()
        )
    }
}

data class ValidationResult(
    val status: Boolean = false
)