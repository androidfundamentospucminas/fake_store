package com.walker.fakeecommerce.data.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walker.fakeecommerce.model.AccessToken
import com.walker.fakeecommerce.repositories.UserRepository
import com.walker.fakeecommerce.utils.SessionManager
import com.walker.fakeecommerce.utils.Validator
import com.walker.fakeecommerce.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.util.Date
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var logoutUser = mutableStateOf(false)

    init {
        loginUIState.value.loadingAlreadyLogged = true
        val tokenExists = sessionManager.readToken() != null
        if (tokenExists) {
            val tokenDate = sessionManager.readDate()
            if (Period.between(LocalDate.parse(tokenDate), LocalDate.now()).days > 20) {
                onEvent(LoginUIEvent.LogoutUser)
                logoutUser.value = true
            } else {
                loginUIState.value.alreadyLogged = true
            }
        }
        loginUIState.value.loadingAlreadyLogged = false
    }

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
                viewModelScope.launch {
                    login(event.onSuccess, event.onFailure)
                }
            }

            is LoginUIEvent.LogoutUser -> {
                sessionManager.logout()
                logoutUser.value = true
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



    private suspend fun login(onSuccess: () -> Unit, onFailure: () -> Unit) {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        val result = userRepository.postLogin(email, password)

        if (result.isSuccessful) {
            result.body()?.let {
                onSuccess()
                sessionManager.writeToken(it)
            }
        } else {
            onFailure()
        }

        loginInProgress.value = false
    }

}