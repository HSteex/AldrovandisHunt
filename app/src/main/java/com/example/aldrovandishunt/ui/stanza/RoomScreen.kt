package com.example.aldrovandishunt.ui.stanza


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aldrovandishunt.AppScreen
import com.example.aldrovandishunt.ui.itemOverlay.ItemOverlayScreen
import com.example.aldrovandishunt.ui.myCollection.CardGrid
import com.google.android.filament.Engine
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.Node


@Composable
fun RoomScreen(
    navController: NavController,
    roomViewModel: RoomViewModel,
    roomName: String,
    engine: Engine,
    modelLoader: ModelLoader,
    cameraNode: CameraNode,
    centerNode: Node,
) {
    val bottomSheetState by roomViewModel.bottomSheetUiState.collectAsState()
    val roomUiState by roomViewModel.roomUiState.collectAsState()


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                tint = Color.Red
            )
            Text("You are in: ")
            Text(
                roomName,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        if (roomUiState.cards.isNotEmpty()) {
            CardGrid(cardList = roomUiState.cards, onCardClick = {
                if (it.isUnlocked) roomViewModel.onUnlockedCardClicked(it)
                else navController.navigate("${AppScreen.Hunt.name}/${it.ID}/${it.name}")
            })
        }
    }

    if (bottomSheetState.isBottomSheetOpen && bottomSheetState.card != null) {
        ItemOverlayScreen(
            card = bottomSheetState.card,
            onDismissRequest = {
                roomViewModel.closeBottomSheet()
            },
            onARClick = {
                navController.navigate("${AppScreen.ARScreen.name}/${bottomSheetState.card!!.name}") {
                    launchSingleTop = true
                }
            },
            engine = engine,
            modelLoader = modelLoader,
            centerNode = centerNode
        )
    }
}

