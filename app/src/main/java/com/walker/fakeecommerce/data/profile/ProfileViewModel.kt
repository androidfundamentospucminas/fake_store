package com.walker.fakeecommerce.data.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.repositories.UserRepository
import com.walker.fakeecommerce.utils.SessionManager
import com.walker.fakeecommerce.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
): ViewModel() {

    private val TAG = ProfileViewModel::class.simpleName

    var profileUIState = mutableStateOf(ProfileUIState())

    var logoutUser = mutableStateOf(false)

    var allValidationsPassed = mutableStateOf(false)

    val profile = Profile(
        id = "123",
        email = "test@test.mail",
        name = "Test",
        avatar = ""
    )

    fun onEvent(event: ProfileUIEvents) {
        when (event) {
            is ProfileUIEvents.DeleteAccount -> {
                logoutUser.value = true
            }
            is ProfileUIEvents.GetProfile -> {
                profileUIState.value = profileUIState.value.copy(
                    profileIsLoading = true
                )

                viewModelScope.launch {
                    val result = userRepository.getProfile()

                    if (result.isSuccessful) {
                        val profileResult = result.body()
                        profileUIState.value = profileUIState.value.copy(
                            profile = profileResult,
                            profileError =  false,
                            nameField = profileResult?.name ?: "-",
                            profileIsLoading = false
                        )
                    } else {
                        profileUIState.value = profileUIState.value.copy(
                            profile = null,
                            profileError =  true,
                            profileIsLoading = false
                        )
                    }
                }
            }

            is ProfileUIEvents.Logout -> {
                logoutUser.value = true
                sessionManager.logout()
            }

            is ProfileUIEvents.EditProfile -> {
                editProfile()
            }

            is ProfileUIEvents.HasNameChanged -> {
                profileUIState.value = profileUIState.value.copy(
                    nameField = event.name
                )

                validateProfileUIDataWithRules()
            }

            is ProfileUIEvents.CleanProfile -> {
                profileUIState.value = profileUIState.value.copy(
                    profile = null,
                    profileIsLoading = false,
                    profileError =  false
                )
            }
        }
    }

    private fun validateProfileUIDataWithRules() {
        val nameResult = Validator.validateName(
            name = profileUIState.value.nameField
        )

        profileUIState.value = profileUIState.value.copy(
            nameFieldError = nameResult.status
        )

        allValidationsPassed.value = nameResult.status
    }

    private fun editProfile() {
        profileUIState.value = profileUIState.value.copy(
            profileIsLoading = true
        )

        viewModelScope.launch {
            delay(500)

            profileUIState.value.profile?.let {
                val profileToEdit = it

                profileToEdit.name = profileUIState.value.nameField

                profileUIState.value = profileUIState.value.copy(
                    profileIsLoading = false,
                    profile = profileToEdit,
                    nameField = profileToEdit.name
                )
            }
        }
    }

}