package com.example.aldrovandishunt

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aldrovandishunt.data.database.HuntDatabase
import com.example.aldrovandishunt.ui.caccia.CaptureScreen
import com.example.aldrovandishunt.ui.caccia.CaptureViewModel
import com.example.aldrovandishunt.ui.myCollection.MyCollectionScreen
import com.example.aldrovandishunt.ui.intro.IntroViewModel
import com.example.aldrovandishunt.ui.itemOverlay.ARScene
import com.example.aldrovandishunt.ui.mappa.MappaScreen
import com.example.aldrovandishunt.ui.stanza.RoomViewModel
import com.example.aldrovandishunt.ui.myCollection.MyCollectionViewModel
import com.example.aldrovandishunt.ui.stanza.RoomScreen
import com.google.android.filament.Engine
import dagger.hilt.android.HiltAndroidApp
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.Node

sealed class AppScreen(val name: String) {
    object Map : AppScreen("Map")
    object Room : AppScreen("Room")
    object Collection : AppScreen("My Collection")
    object Hunt : AppScreen("Hunt Screen")
    object ARScreen : AppScreen("AR")
}

@HiltAndroidApp
class HuntApplication : Application() {
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { HuntDatabase.getDatabase(this) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarFunction(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen,
                fontWeight = FontWeight.Medium,
            )
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.orangeButton),
        ), navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "Back button"
                    )
                }
            }
        }, modifier = modifier
    )

}

@Composable
fun BottomBar(
    onMapClick: () -> Unit,
    onMyCollectionClick: () -> Unit,
) {
    BottomAppBar(
        containerColor = colorResource(id = R.color.orangeButton), modifier = Modifier.height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = onMapClick, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Map button",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(R.string.mapButton))


                }
            }
            Button(
                onClick = onMyCollectionClick, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Backpack,
                        contentDescription = "My Collection button",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(R.string.collectionButton))

                }
            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigator(
    navController: NavHostController = rememberNavController(),
    engine: Engine,
    modelLoader: ModelLoader,
    cameraNode: CameraNode,
    centerNode: Node,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = when (navBackStackEntry?.destination?.route) {
        AppScreen.Map.name -> stringResource(id = R.string.mapButton)
        AppScreen.Room.name -> "Room" //FIXME Vorrei dare il titolo in base alla stanza in cui ci si trova8i
        AppScreen.Collection.name -> stringResource(id = R.string.collectionButton)
        AppScreen.Hunt.name -> "Hunt Screen" //FIXME Non so cosa mettere qua, potrei pure togliere il titolo
        AppScreen.ARScreen.name -> "AR"
        else -> AppScreen.Map.name
    }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }, topBar = {
        if (currentRoute?.contains(AppScreen.ARScreen.name) == true || currentRoute?.contains(AppScreen.Hunt.name)==true ) null
        else {
            TopAppBarFunction(currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        }
    }, bottomBar = {
        if (currentRoute?.contains(AppScreen.ARScreen.name) == true || currentRoute?.contains(AppScreen.Hunt.name)==true ) null
        else {
            BottomBar(onMapClick = { navController.navigate(AppScreen.Map.name) },
                onMyCollectionClick = { navController.navigate(AppScreen.Collection.name) })
        }
    }) { innerPadding ->
        NavigationGraph(
            navController, innerPadding, engine, modelLoader, cameraNode, centerNode
        )
    }

}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    engine: Engine,
    modelLoader: ModelLoader,
    cameraNode: CameraNode,
    centerNode: Node,

    ) {

    NavHost(
        navController = navController,
        startDestination = AppScreen.Map.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(AppScreen.Map.name) {
            val introViewModel = hiltViewModel<IntroViewModel>()
            MappaScreen(navController, introViewModel)
        }
        composable("${AppScreen.Room.name}/{roomName}",
            arguments = listOf(navArgument("roomName") { type = NavType.StringType })) {
            val roomViewModel = hiltViewModel<RoomViewModel>()
            RoomScreen(
                navController,
                roomViewModel,
                it.arguments?.getString("roomName") ?: "",
                engine,
                modelLoader,
                cameraNode,
                centerNode
            )
        }
        composable(AppScreen.Collection.name) {
            val collectionViewModel = hiltViewModel<MyCollectionViewModel>()
            MyCollectionScreen(
                navController, collectionViewModel, engine, modelLoader, cameraNode, centerNode
            )
        }
        composable("${AppScreen.Hunt.name}/{cardId}/{cardName}",
            arguments = listOf(navArgument("cardId") { type = NavType.IntType }, navArgument("cardName") { type = NavType.StringType })) {
            val captureViewModel = hiltViewModel<CaptureViewModel>()
            CaptureScreen(navController, captureViewModel, it.arguments?.getInt("cardId") ?: -1, it.arguments?.getString("cardName") ?: "")
        }
        composable("${AppScreen.ARScreen.name}/{cardName}",
            arguments = listOf(navArgument("cardName") { type = NavType.StringType })) {
            ARScene(navController)
        }
    }
}