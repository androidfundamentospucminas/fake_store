package com.walker.fakeecommerce.composables

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.walker.fakeecommerce.R
import com.walker.fakeecommerce.components.ButtonComponent
import com.walker.fakeecommerce.components.CheckboxComponent
import com.walker.fakeecommerce.components.ClickableLoginTextComponent
import com.walker.fakeecommerce.components.DividerTextComponent
import com.walker.fakeecommerce.components.HeadingTextComponent
import com.walker.fakeecommerce.components.MyTextFieldComponent
import com.walker.fakeecommerce.components.NormalTextComponent
import com.walker.fakeecommerce.components.PasswordTextFieldComponent
import com.walker.fakeecommerce.data.signup.SignUpUIEvent
import com.walker.fakeecommerce.data.signup.SignUpViewModel
import com.walker.fakeecommerce.navigation.Screen

@Composable
fun SignUpScreen(
    navController: NavHostController,
    signupViewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            signupViewModel.onEvent(SignUpUIEvent.ImageChanged(uri))
        } else {
            Log.d("PickVisualMedia", "No media selected")
        }
    }

    val signUpSuccessCompleted = signupViewModel.signUpSuccessCompleted.value
    val signUpInProgress = signupViewModel.signUpInProgress.value
    val registerError = signupViewModel.signUpUIState.value.registerError

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (signUpSuccessCompleted) {
            navController.navigateUp()
            Toast.makeText(context, stringResource(id = R.string.success_registerd), Toast.LENGTH_SHORT).show()
            signupViewModel.signUpSuccessCompleted.value = false
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if(!signUpInProgress) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    NormalTextComponent(value = stringResource(id = R.string.hello))

                    SubcomposeAsyncImage(
                        model = signupViewModel.signUpUIState.value.image,
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .width(180.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.Gray)
                            .clickable(onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }),
                        contentScale = ContentScale.Crop
                    )


                    HeadingTextComponent(value = stringResource(id = R.string.create_account))
                    Spacer(modifier = Modifier.height(20.dp))

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.name),
                        imageVector = Icons.Default.Person,
                        onTextChanged = {
                            signupViewModel.onEvent(SignUpUIEvent.NameChanged(it))
                        },
                        errorStatus = signupViewModel.signUpUIState.value.nameError
                    )

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.email),
                        imageVector = Icons.Default.Mail,
                        onTextChanged = {
                            signupViewModel.onEvent(SignUpUIEvent.EmailChanged(it))
                        },
                        errorStatus = signupViewModel.signUpUIState.value.emailError
                    )

                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        imageVector = Icons.Default.Lock,
                        onTextSelected = {
                            signupViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))
                        },
                        errorStatus = signupViewModel.signUpUIState.value.passwordError
                    )

                    CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                        onTextSelected = {
                            navController.navigate(Screen.TERMS_AND_POLICY.name)
                        },
                        onCheckedChange = {
                            signupViewModel.onEvent(SignUpUIEvent.PrivacyPolicyCheckBoxClicked(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    ButtonComponent(
                        value = stringResource(id = R.string.register),
                        onButtonClicked = {
                            signupViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
                        },
                        isEnabled = signupViewModel.allValidationsPassed.value,
                        imageVector = Icons.Default.Login
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                        navController.navigate(Screen.LOGIN_SCREEN.name)
                    })
                }
            }
            if(signUpInProgress) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                    )
                    HeadingTextComponent(value = stringResource(id = R.string.loading))
                }
            }
        }
    }


}