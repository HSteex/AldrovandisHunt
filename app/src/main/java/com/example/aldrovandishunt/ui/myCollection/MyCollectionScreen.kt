package com.example.aldrovandishunt.ui.myCollection

import android.view.SurfaceView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.aldrovandishunt.data.database.Carte
import com.example.aldrovandishunt.ui.itemOverlay.ItemOverlayScreen
import com.example.aldrovandishunt.utils.ModelRenderer

@Composable
fun MyCollectionScreen(
    navController: NavController,
    viewModel: MyCollectionViewModel,
    //TODO Aggiungere viewModel
) {
    //FIXME Utilizzatao solo per testare le card
    val bottomSheetState by viewModel.bottomSheetUiState.collectAsState()
    //val cardList= viewModel.unlockedCards
    val cardList: List<Carte> = viewModel.cardList

    Model3DView()



//    Box(modifier = Modifier
//        .padding(16.dp)
//        .fillMaxSize()
//      ) {
//        if (cardList.isNotEmpty()) {
//            CardGrid(cardList = cardList, onCardClick = { viewModel.onCardClicked(it) } )
//        }
//    }
//
//    if (bottomSheetState.isBottomSheetOpen && bottomSheetState.card != null) {
//        ItemOverlayScreen(
//            card = bottomSheetState.card,
//            onDismissRequest = {
//                viewModel.closeBottomSheet()
//            },
//            lifecycle = lifecycle
//        )
//    }
}

@Composable
fun Model3DView() {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val modelRenderer = remember { ModelRenderer() }

    DisposableEffect(context) {
        // Quando il composable viene montato, chiamiamo onSurfaceAvailable
        val surfaceView = SurfaceView(context)
        modelRenderer.onSurfaceAvailable(surfaceView, lifecycle, "T-Rex")

        // Quando il composable viene smontato, puliamo le risorse
        onDispose {
            // Aggiungi eventuali pulizie necessarie
        }
    }

    AndroidView(factory = { modelRenderer.surfaceView })
}