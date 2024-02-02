package com.example.aldrovandishunt.ui.itemOverlay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aldrovandishunt.data.database.Carte

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemOverlayScreen(
    onDismissRequest: () -> Unit,
    card: Carte? = null
) {
    val height = LocalConfiguration.current.screenHeightDp.dp
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.height(height),
        sheetState = SheetState(
            skipPartiallyExpanded = true,
        )
    ) {
        if(card != null) {
        }



    }
}