package com.walker.fakeecommerce.data.signup

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.walker.fakeecommerce.repositories.UserRepository
import com.walker.fakeecommerce.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val storageReference: StorageReference,
    private val userRepository: UserRepository
): ViewModel() {

    private val TAG = SignUpViewModel::class.simpleName

    var signUpUIState = mutableStateOf(SignUpUIState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    var signUpSuccessCompleted = mutableStateOf(false)

    fun onEvent(event: SignUpUIEvent) {
        when (event) {
            is SignUpUIEvent.FirstNameChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    name = event.firstName
                )
                printState()
                validateDataWithRules()
            }

            is SignUpUIEvent.EmailChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    email = event.email
                )
                printState()
                validateDataWithRules()
            }

            is SignUpUIEvent.PasswordChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    password = event.password
                )
                printState()
                validateDataWithRules()
            }

            is SignUpUIEvent.RegisterButtonClicked -> {
                signUp()
                validateDataWithRules()
            }

            is SignUpUIEvent.PrivacyPolicyCheckBoxClicked -> {
                signUpUIState.value = signUpUIState.value.copy(
                    privacyPolicyAccepted = event.status
                )
                validateDataWithRules()
            }

            is SignUpUIEvent.ImageChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    image = event.image
                )
                validateDataWithRules()
            }
        }
    }


    private fun signUp() {
        signUpInProgress.value = true

        signUpUIState.value.registerError = false
        signUpSuccessCompleted.value = false

        createUser(
            email = signUpUIState.value.email,
            password = signUpUIState.value.password,
            image = signUpUIState.value.image,
            onSuccess = {
                signUpUIState.value.registerError = false
                signUpSuccessCompleted.value = true
                signUpInProgress.value = false
            },
            onFailure = {
                signUpUIState.value.registerError = true
                signUpSuccessCompleted.value = false
                signUpInProgress.value = false
            },
            name = signUpUIState.value.name
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = signUpUIState.value.name
        )

        val emailResult = Validator.validateEmail(
            email = signUpUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = signUpUIState.value.password
        )

        val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(
            statusValue = signUpUIState.value.privacyPolicyAccepted
        )

        val imageResult = Validator.validateImage(
            image = signUpUIState.value.image
        )

        signUpUIState.value = signUpUIState.value.copy(
            firstNameError = fNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )

        allValidationsPassed.value = fNameResult.status && imageResult.status &&
                emailResult.status && passwordResult.status && privacyPolicyResult.status

    }


    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, signUpUIState.value.toString())
    }


    private fun createUser(
        email: String,
        password: String,
        image: Uri,
        name: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val storageRef = storageReference.child("photos/$email")
        val task = storageRef.putFile(image)

        task.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                postUser(email, password, it, name, onSuccess, onFailure)
            }.addOnFailureListener {
                onFailure()
            }
        }.addOnFailureListener {
            Log.d(TAG, "error:${it.message}")
            onFailure()
        }
    }

    private fun postUser(
        email: String,
        password: String,
        image: Uri,
        name: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.postUser(name, email, password, image.toString())

            if (result.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
                Log.d("error::", result.message())
            }
        }
    }

}