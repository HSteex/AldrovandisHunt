package com.example.aldrovandishunt.ui.caccia

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.data.database.CaptureHint
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.ui.intro.AnimatedTalkingMan
import com.example.aldrovandishunt.ui.intro.TypewriterTextEffect
import com.example.aldrovandishunt.ui.myCollection.Card
import com.example.aldrovandishunt.ui.theme.primaryOrange
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.Session
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@Composable
fun CaptureScreen(
    navController: NavController,
    viewModel: CaptureViewModel,
    cardId: Int,
    cardName: String,
) {
    val captureUiState = viewModel.captureUiState.collectAsState()


    if (!captureUiState.value.unlocked) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(0.7f)) {
                ImageRecognition(
                    cardName = cardName,
                    onSuccess = { viewModel.onCapture() },
                )
            }
            CaptureHintRow(hint = captureUiState.value.hint)
        }
        Box(modifier = Modifier.padding(8.dp)) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .padding(8.dp)
                    .background(primaryOrange)
            )
            {

                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )

            }
        }
    } else {
        CardUnlockScreen(
            card = captureUiState.value.card,
            onContinue = { navController.popBackStack() })
    }


}

@Composable
fun CaptureHintRow(
    hint: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,

        ) {

        Box(
            modifier = Modifier
                .weight(0.8f)
                .padding(vertical = 16.dp)
                .offset(x = 16.dp)

        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(primaryOrange)
                    .fillMaxWidth()

            ) {
                TypewriterTextEffect(text = hint) {
                    Text(
                        text = it,
                        style = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier
                            .height(300.dp)
                            //.wrapContentSize(align = Alignment.CenterStart)
                            .padding(16.dp),
                    )
                }

            }
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .align(Alignment.Bottom)
        ) {
            CroppedAnimatedTalkingMan(durationInSeconds = 3, 1)
        }
    }
}

@Composable
fun CroppedAnimatedTalkingMan(
    durationInSeconds: Int,
    talk: Int,
) {
    var showOpenMouth by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = talk) {
        val endTime = System.currentTimeMillis() + durationInSeconds * 1000
        while (System.currentTimeMillis() < endTime) {
            showOpenMouth = Random.nextBoolean()
            delay(100) // Ritardo tra gli switch
        }
        showOpenMouth = false
    }

    if (showOpenMouth) {
        Image(
            painter = painterResource(id = R.drawable.aldrovandi_open_crop),
            contentDescription = "Aldrovandi con bocca aperta",

            )
    } else {
        Image(
            painter = painterResource(id = R.drawable.aldrovandi_closed_crop),
            contentDescription = "Aldrovandi con bocca chiusa",

            )

    }
}

@Composable
fun CardUnlockScreen(
    card: Card,
    onContinue: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You have unlocked this card!",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(48.dp))
        val horizontalSpacer = 36.dp
        val width = LocalConfiguration.current.screenWidthDp.dp
        val cardWidth = (width - (horizontalSpacer + 16.dp)) / 2
        Card(
            card = card,
            onCardClick = {},
            cardWidth = cardWidth
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "You can find it in your collection", style = TextStyle(fontSize = 16.sp))
        Spacer(modifier = Modifier.height(100.dp))
        Button(onClick = { onContinue() }) {
            Text(text = "Continue")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = "Arrow forward"
            )

        }

    }


    fun parade(): List<Party> {
        val party = Party(
            speed = 10f,
            maxSpeed = 30f,
            damping = 0.9f,
            angle = Angle.RIGHT - 50,
            spread = Spread.WIDE,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 1, TimeUnit.SECONDS).perSecond(50),
            position = Position.Relative(0.0, 0.4)
        )

        return listOf(
            party,
            party.copy(
                angle = Angle.LEFT+50, // flip angle from right to left
                position = Position.Relative(1.0, 0.4)
            ),
        )
    }

    KonfettiView(parties = parade(), modifier = Modifier.fillMaxSize())

}


@Preview
@Composable
fun CaptureHintPreview() {
    CardUnlockScreen(
        card = Card(
            0,
            "T-Rex",
            "",
            com.example.aldrovandishunt.data.database.Rarity.UNCOMMON,
            "",
            true
        )
    )
}



