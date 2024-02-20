package com.example.aldrovandishunt.ui.myCollection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aldrovandishunt.AppScreen
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.ui.itemOverlay.ItemOverlayScreen
import com.google.android.filament.Engine
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.Node

@Composable
fun MyCollectionScreen(
    navController: NavController,
    viewModel: MyCollectionViewModel,
    engine: Engine,
    modelLoader: ModelLoader,
    cameraNode: CameraNode,
    centerNode: Node,
) {

    //FIXME Utilizzatao solo per testare le card
    val bottomSheetState by viewModel.bottomSheetUiState.collectAsState()
    val cardListUiState by viewModel.unlockedCardsUiState.collectAsState()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (cardListUiState.unlockedCards.isNotEmpty()) {
            CardGrid(
                cardList = cardListUiState.unlockedCards,
                onCardClick = { viewModel.onCardClicked(it) })
        }
    }

    LaunchedEffect(Unit) {
        if (viewModel.isARScreenOpen.value) {
            viewModel.onARBackClick()
        }
    }
    if (bottomSheetState.isBottomSheetOpen && bottomSheetState.card != null) {
        ItemOverlayScreen(
            card = bottomSheetState.card,
            onDismissRequest = {
                viewModel.closeBottomSheet()
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
