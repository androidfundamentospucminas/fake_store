package com.walker.fakeecommerce.data.login

sealed class LoginUIEvent{

    data class EmailChanged(val email:String): LoginUIEvent()
    data class PasswordChanged(val password: String) : LoginUIEvent()

    data class LoginButtonClicked(val onSuccess: () -> Unit, val onFailure: () -> Unit) : LoginUIEvent()
}