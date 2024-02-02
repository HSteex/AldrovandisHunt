package com.example.aldrovandishunt.ui.mappa

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aldrovandishunt.ui.intro.IntroScreen
import com.example.aldrovandishunt.ui.intro.IntroViewModel

@Composable
fun MappaScreen (
    navController: NavController,
    viewModel: IntroViewModel,
    ) {
    IntroScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )

}