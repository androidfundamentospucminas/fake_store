package com.walker.fakeecommerce.data.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.repositories.UserRepository
import com.walker.fakeecommerce.utils.SessionManager
import com.walker.fakeecommerce.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val TAG = ProfileViewModel::class.simpleName

    var profileUIState = mutableStateOf(ProfileUIState())

    var logoutUser = mutableStateOf(false)

    var allValidationsPassed = mutableStateOf(false)

    fun onEvent(event: ProfileUIEvents) {
        when (event) {
            is ProfileUIEvents.DeleteAccount -> {
                viewModelScope.launch {
                    profileUIState.value.profile?.id?.let {
                        val result = userRepository.deleteProfile(it)

                        if (result.isSuccessful) {
                            sessionManager.logout()
                            logoutUser.value = true
                        }
                    }
                }
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
                            nameField = profileResult?.name ?: "",
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
                sessionManager.logout()
                logoutUser.value = true
            }

            is ProfileUIEvents.EditProfile -> {
                editProfile()
            }

            is ProfileUIEvents.HasNameChanged -> {
                profileUIState.value = profileUIState.value.copy(
                    nameField = event.name
                )

                validateLoginUIDataWithRules()
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

    private fun validateLoginUIDataWithRules() {
        val nameResult = Validator.validateFirstName(
            fName = profileUIState.value.nameField
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
            profileUIState.value.profile?.let {
                val profileToEdit = it

                profileToEdit.name = profileUIState.value.nameField

                val result = userRepository.editProfile(it)
                if (result.isSuccessful) {
                    val newProfile = result.body()
                    profileUIState.value = profileUIState.value.copy(
                        profileIsLoading = false,
                        profile = result.body(),
                        nameField = newProfile?.name ?: ""
                    )
                } else {
                    profileUIState.value = profileUIState.value.copy(
                        profileIsLoading = false,
                        profileError = true
                    )
                }
            }
        }
    }

}