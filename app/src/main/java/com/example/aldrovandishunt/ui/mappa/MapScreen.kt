package com.example.aldrovandishunt.ui.mappa

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aldrovandishunt.AppScreen
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.data.database.Rooms
import com.example.aldrovandishunt.ui.intro.IntroScreen
import com.example.aldrovandishunt.ui.intro.IntroViewModel

@Composable
fun MapScreen(
    navController: NavController,
    introViewModel: IntroViewModel,
    mapViewModel: MapViewModel
) {
    IntroScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )

    val mapUiState = mapViewModel.mapUiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {

        LazyColumn(content = {
            items(mapUiState.value.rooms.size) { index ->
                RoomComposable(
                    room = mapUiState.value.rooms[index],
                    navController = navController,
                    cardToUnlock = mapUiState.value.cardToUnlock[mapUiState.value.rooms[index]]
                        ?: 0,
                    unlockedCard = mapUiState.value.unlockedCard[mapUiState.value.rooms[index]] ?: 0
                )
            }
        })
    }
}

@Composable
fun RoomComposable(
    room: Rooms,
    navController: NavController,
    cardToUnlock: Int,
    unlockedCard: Int
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            navController.navigate("${AppScreen.Room.name}/${room.id}/${room.name}")
        },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
        ) {
        Column {
            Image(
                painter = painterResource(id = getImageOfRooms(room.name)),
                contentDescription = room.name,
                modifier = Modifier
                    .height(screenWidth.div(2.4f))
                    .fillMaxWidth()
                    ,
                contentScale = ContentScale.Crop,
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = room.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            fontSize = 24.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.cards_to_unlock))
                            Text(text = cardToUnlock.toString(), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.cards_unlocked))
                            Text(text = unlockedCard.toString(), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }





}

fun getImageOfRooms(name: String): Int {
    when (name) {
        "Museo di Palazzo Poggi" -> return R.drawable.palazzo_poggi
        "Orto Botanico" -> return R.drawable.orto_botanico
        else -> return 0
    }
}
