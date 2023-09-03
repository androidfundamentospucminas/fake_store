package com.walker.fakeecommerce.data.signup

import android.net.Uri


sealed class SignUpUIEvent {

    data class FirstNameChanged(val firstName:String) : SignUpUIEvent()
    data class EmailChanged(val email:String): SignUpUIEvent()
    data class PasswordChanged(val password: String): SignUpUIEvent()
    data class ImageChanged(val image: Uri) : SignUpUIEvent()

    data class PrivacyPolicyCheckBoxClicked(val status:Boolean): SignUpUIEvent()

    object RegisterButtonClicked: SignUpUIEvent()
}