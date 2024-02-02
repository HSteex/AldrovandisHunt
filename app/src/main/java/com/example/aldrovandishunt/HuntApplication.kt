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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aldrovandishunt.data.database.HuntDatabase
import com.example.aldrovandishunt.ui.caccia.CacciaScreen
import com.example.aldrovandishunt.ui.myCollection.MyCollectionScreen
import com.example.aldrovandishunt.ui.intro.IntroViewModel
import com.example.aldrovandishunt.ui.mappa.MappaScreen
import com.example.aldrovandishunt.ui.myCollection.MyCollectionViewModel
import com.example.aldrovandishunt.ui.stanza.StanzaScreen
import dagger.hilt.android.HiltAndroidApp

sealed class AppScreen(val name: String) {
    object Map : AppScreen("Map")
    object Room : AppScreen("Room")
    object Collection : AppScreen("My Collection")
    object Hunt : AppScreen("Hunt Screen")
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
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.orangeButton),
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        },
        modifier = modifier
    )

}

@Composable
fun BottomBar(
    onMapClick: () -> Unit,
    onMyCollectionClick: () -> Unit,
) {
    BottomAppBar(
        containerColor = colorResource(id = R.color.orangeButton),
        modifier = Modifier.height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = onMapClick,
                colors = ButtonDefaults.buttonColors(
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
                        modifier= Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(R.string.mapButton))


                }
            }
            Button(
                onClick = onMyCollectionClick,
                colors = ButtonDefaults.buttonColors(
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
                        modifier= Modifier.padding(end = 8.dp)
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

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = when(navBackStackEntry?.destination?.route) {
        AppScreen.Map.name -> stringResource(id = R.string.mapButton)
        AppScreen.Room.name ->  "Room" //FIXME Vorrei dare il titolo in base alla stanza in cui ci si trova8i
        AppScreen.Collection.name -> stringResource(id = R.string.collectionButton)
        AppScreen.Hunt.name -> "Hunt Screen" //FIXME Non so cosa mettere qua, potrei pure togliere il titolo
        else -> AppScreen.Map.name
    }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBarFunction(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomBar(
                onMapClick = { navController.navigate(AppScreen.Map.name) },
                onMyCollectionClick = { navController.navigate(AppScreen.Collection.name) }
            )
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding)
    }

}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
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
        composable(AppScreen.Room.name) {
            StanzaScreen(navController)
        }
        composable(AppScreen.Collection.name) {
            val collectionViewModel = hiltViewModel<MyCollectionViewModel>()
            MyCollectionScreen(navController, collectionViewModel)
        }
        composable(AppScreen.Hunt.name) {
            CacciaScreen(navController)
        }
    }
}