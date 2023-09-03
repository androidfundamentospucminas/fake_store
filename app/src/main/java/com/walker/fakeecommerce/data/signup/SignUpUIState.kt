package com.walker.fakeecommerce.data.signup

import android.net.Uri
import androidx.core.net.toUri

data class SignUpUIState(
    var name: String = "",
    var email: String = "",
    var image: Uri = "".toUri(),
    var password: String = "",
    var privacyPolicyAccepted: Boolean = false,

    var firstNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var privacyPolicyError: Boolean = false,

    var registerError: Boolean = false
)