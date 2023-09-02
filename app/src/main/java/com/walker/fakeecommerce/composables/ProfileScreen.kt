package com.walker.fakeecommerce.composables

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.walker.fakeecommerce.R
import com.walker.fakeecommerce.components.ButtonComponent
import com.walker.fakeecommerce.components.MyTextFieldComponent
import com.walker.fakeecommerce.data.profile.ProfileUIEvents
import com.walker.fakeecommerce.data.profile.ProfileViewModel
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val profile = profileViewModel.profileUIState.value.profile
    val profileIsLoading = profileViewModel.profileUIState.value.profileIsLoading
    val profileError = profileViewModel.profileUIState.value.profileError
    val logoutUser = profileViewModel.logoutUser.value

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

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileUIEvents.GetProfile)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Cart")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        profileViewModel.onEvent(ProfileUIEvents.DeleteAccount)
                    }) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = "Delete")
                    }
                    IconButton(onClick = {
                        profileViewModel.onEvent(ProfileUIEvents.Logout)
                    }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
                    }

                }
            )
        },
        content = {
            if (!profileError && profile != null && !profileIsLoading) {
                ProfileContent(it, profile, profileViewModel)
            }

            if (profileError && !profileIsLoading && profile == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = it.calculateTopPadding(), horizontal = 15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.error_loading_profile))
                    ButtonComponent(
                        value = stringResource(id = R.string.retry),
                        onButtonClicked = {
                            profileViewModel.onEvent(ProfileUIEvents.GetProfile)
                        },
                        imageVector = Icons.Default.ArrowCircleDown,
                        isEnabled = true
                    )
                }
            }

            if (profileIsLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            profileViewModel.onEvent(ProfileUIEvents.CleanProfile)
        }
    }
}

@Composable
fun ProfileContent(paddingValues: PaddingValues, profile: Profile, profileViewModel: ProfileViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfilePicture(
                modifier = Modifier
                    .size(210.dp)
                    .padding(16.dp),
                profile
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = profile.email,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.name),
                Icons.Default.Message,
                onTextChanged = {
                    profileViewModel.onEvent(ProfileUIEvents.HasNameChanged(it))
                },
                initialValue = profile.name
            )

            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(
                value = stringResource(id = R.string.edit),
                onButtonClicked = {
                    profileViewModel.onEvent(
                        ProfileUIEvents.EditProfile
                    )
                },
                isEnabled = profileViewModel.allValidationsPassed.value,
                imageVector = Icons.Default.Login
            )
        }
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier, profile: Profile) {
    SubcomposeAsyncImage(
        model = profile.avatar,
        contentDescription = profile.name,
        loading = {
            CircularProgressIndicator()
        },
        modifier = modifier
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
