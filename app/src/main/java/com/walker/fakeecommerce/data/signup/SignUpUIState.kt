package com.walker.fakeecommerce.data.signup

import android.net.Uri
import androidx.core.net.toUri

data class SignUpUIState(
    var name :String = "",
    var image: Uri = "".toUri(),
    var email  :String = "",
    var password  :String = "",
    var privacyPolicyAccepted :Boolean = false,

    var nameError :Boolean = false,
    var emailError :Boolean = false,
    var passwordError : Boolean = false,
    var privacyPolicyError:Boolean = false,

    var registerError: Boolean = false
)