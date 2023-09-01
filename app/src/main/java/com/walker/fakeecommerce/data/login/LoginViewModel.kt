package com.walker.fakeecommerce.data.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.walker.fakeecommerce.utils.Validator
import com.walker.fakeecommerce.utils.isValidEmail

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                validateLoginUIDataWithRules()
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                validateLoginUIDataWithRules()
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login(event.onSuccess, event.onFailure)
                validateLoginUIDataWithRules()
            }
        }
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun login(onSuccess: () -> Unit, onFailure: () -> Unit) {

        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        if (email == "annyufrr@gmail.com" && password == "anny123") {
            onSuccess()
        } else {
            onFailure()
        }
        loginInProgress.value = false
    }

}