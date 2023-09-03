package com.walker.fakeecommerce.composables

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.walker.fakeecommerce.components.HeadingTextComponent
import com.walker.fakeecommerce.components.MyTextFieldComponent
import com.walker.fakeecommerce.components.NormalTextComponent
import com.walker.fakeecommerce.components.PasswordTextFieldComponent
import com.walker.fakeecommerce.data.login.LoginViewModel
import com.walker.fakeecommerce.R
import com.walker.fakeecommerce.components.ButtonComponent
import com.walker.fakeecommerce.components.ClickableLoginTextComponent
import com.walker.fakeecommerce.components.DividerTextComponent
import com.walker.fakeecommerce.data.login.LoginUIEvent
import com.walker.fakeecommerce.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val alreadyLogged = loginViewModel.loginUIState.value.alreadyLogged
    val loadingAlreadyLogged = loginViewModel.loginUIState.value.loadingAlreadyLogged
    val logoutUser = loginViewModel.logoutUser.value

    fun onSuccess() {
        navController.navigate(Screen.PRODUCTS_SCREEN.name) {
            popUpTo(0)
        }
    }

    if (logoutUser) {
        navController.navigate(Screen.LOGIN_SCREEN.name) {
            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    if (alreadyLogged) {
        onSuccess()
    }

    if (!loadingAlreadyLogged && !alreadyLogged) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(28.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    NormalTextComponent(value = stringResource(id = R.string.login))
                    HeadingTextComponent(value = stringResource(id = R.string.welcome))
                    Spacer(modifier = Modifier.height(20.dp))

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.email),
                        Icons.Default.Message,
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError
                    )

                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        Icons.Default.Lock,
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError
                    )

                    Spacer(modifier = Modifier.height(70.dp))


                    if (!loginViewModel.loginInProgress.value) {
                        ButtonComponent(
                            value = stringResource(id = R.string.login),
                            onButtonClicked = {
                                loginViewModel.onEvent(
                                    LoginUIEvent.LoginButtonClicked(
                                        onSuccess = {
                                            onSuccess()
                                        },
                                        onFailure = {
                                            Toast.makeText(
                                                context,
                                                "Login falhou! Usuário ou senha incorreto(s)",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    ))
                            },
                            isEnabled = loginViewModel.allValidationsPassed.value,
                            imageVector = Icons.Default.Login
                        )
                    } else {
                        CircularProgressIndicator()
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                        navController.navigate(Screen.SIGNUP_SCREEN.name)
                    })
                }
            }
        }
    }
}