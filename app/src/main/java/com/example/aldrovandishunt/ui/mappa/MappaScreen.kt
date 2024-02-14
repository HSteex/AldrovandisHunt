package com.example.aldrovandishunt.ui.mappa

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aldrovandishunt.AppScreen
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.ui.intro.IntroScreen
import com.example.aldrovandishunt.ui.intro.IntroViewModel

@Composable
fun MappaScreen(
    navController: NavController,
    viewModel: IntroViewModel,
) {
    IntroScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )
    Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        MapWithOverlayButton(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            navController = navController
        )
        Column (modifier= Modifier
            .padding(16.dp)
            .wrapContentHeight() ) {
            Text(text = "Use the map to select a point of interest!")
            //TODO
            if (false) {
                Row {
                    Text(text = "Suggested position: ")
                    //TODO get position from
                    Text(text = "Poggi", fontWeight = FontWeight.Bold, color = Color.Red)
                }
                Button(onClick = {


                }) {
                }

            }else{
                Spacer(modifier = Modifier.size(10.dp))
            }

        }
    }


}

@Composable
fun MapWithOverlayButton(
    modifier: Modifier,
    navController: NavController,
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_background),
            contentDescription = "Mappa",
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.map_overlay_poggi),
            contentDescription = "Button for poggi",
            modifier = Modifier
                .size(50.dp)
                .offset(200.dp, 300.dp)
                .clickable {
                    navController.navigate("${AppScreen.Room.name}/Stanza 1")
                }
        )
    }


}