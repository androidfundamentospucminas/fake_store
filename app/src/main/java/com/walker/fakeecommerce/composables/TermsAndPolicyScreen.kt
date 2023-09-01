package com.walker.fakeecommerce.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndPolicyScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Política de Privacidade e Termo de Uso") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = {
            Text(text = "Lorem ipsum dolorÏ sit amet, consectetur adipiscing elit. Nulla facilisi. Curabitur accumsan justo vitae tortor tincidunt auctor. Quisque posuere velit justo, sit amet accumsan turpis luctus at. Nullam rhoncus, augue eget venenatis pulvinar, urna risus laoreet ex, id gravida felis massa non nunc. Nullam venenatis, nulla at rhoncus tincidunt, mauris eros convallis tortor, nec elementum odio augue a felis.\n" +
                    "\n" +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse non ante ac justo bibendum facilisis. Suspendisse potenti. Donec vel consectetur nulla. Cras et eros id felis vulputate blandit. In hac habitasse platea dictumst. Sed non odio vel nulla convallis tincidunt vel nec metus.\n" +
                    "\n" +
                    "Quisque quis nulla id nunc pulvinar pharetra. Fusce semper neque a tortor dictum venenatis. Vestibulum auctor, metus eu pellentesque cursus, mi enim dictum est, vel blandit quam sem eget neque. In hac habitasse platea\n",
                modifier = Modifier.padding(vertical = it.calculateTopPadding(), horizontal = 15.dp))
        },
    )
}