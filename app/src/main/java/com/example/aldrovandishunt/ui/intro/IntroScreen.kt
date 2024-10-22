package com.example.aldrovandishunt.ui.intro

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Api
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.ui.theme.bluePermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlin.random.Random


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IntroScreen(
    navController: NavController, viewModel: IntroViewModel
) {
    val introUiState by viewModel.introUiState.collectAsState()
    val (height, width) = LocalConfiguration.current.run { screenHeightDp.dp to screenWidthDp.dp }
    val fontSize = when {
        height < 600.dp -> 16.sp
        width < 600.dp -> 18.sp
        else -> 20.sp
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.acceptedPermissions()
        }
    }


    if (viewModel.intro.value == true) {
        Dialog(properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { }) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.speech_bubble),
                            contentDescription = "Fumetto di dialogo",
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(bottom = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            TypewriterTextEffect(
                                text = stringResource(id = introUiState.textId),
                                displayTextComposable = { text ->
                                    Text(
                                        text = text,
                                        color= Color.Black,
                                        fontSize = fontSize,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(52.dp, 52.dp, 52.dp, 0.dp),
                                    )
                                },
                                minDelayInMillis = 50,
                                maxDelayInMillis = 51,
                                onEffectCompleted = {
                                    viewModel.stopTalking()
                                    viewModel.showButton()
                                }
                            )
                            if (introUiState.skipButtonVisible) {
                                Button(
                                    onClick = { viewModel.nextPage() },
                                    colors = buttonColors()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = if(introUiState.page==viewModel.PAGES-1) stringResource(
                                            R.string.start
                                        ) else stringResource(id = R.string.continue_button))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                            contentDescription = "Freccia destra",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                            if (introUiState.permissionButtonVisible) {
                                Button(
                                    onClick = {
                                        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                    },
                                    colors = buttonColors(
                                        containerColor = bluePermission,
                                    )
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = stringResource(R.string.accept_permissions))
                                        Icon(
                                            imageVector = Icons.Default.Api,
                                            contentDescription = "Freccia destra",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedTalkingMan(
                        talk = viewModel.talk.value
                    )
                }
            }
        }
    }

}

@Composable
fun AnimatedTalkingMan(
    talk:Boolean,
) {
    var showOpenMouth by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = talk) {
        while (talk) {
            showOpenMouth = Random.nextBoolean()
            delay(100) // Ritardo tra gli switch
        }
        showOpenMouth = false
    }

    if (showOpenMouth) {
        Image(
            painter = painterResource(id = R.drawable.aldrovandi_open),
            contentDescription = "Aldrovandi con bocca aperta",
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.aldrovandi_closed),
            contentDescription = "Aldrovandi con bocca chiusa",
            modifier = Modifier.fillMaxSize()
        )
    }
}



