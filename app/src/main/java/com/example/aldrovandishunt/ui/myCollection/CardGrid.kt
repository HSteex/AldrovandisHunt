package com.example.aldrovandishunt.ui.myCollection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.aldrovandishunt.data.database.Card

@Composable
fun CardGrid(
    cardList: List<Card>,
    onCardClick: (Card) -> Unit,
) {
    val horizontalSpacer = 36.dp
    val width= LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (width - (horizontalSpacer+16.dp))  / 2
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacer)
    ) {
        items(cardList.size) { index ->
            val card = cardList[index]
            CardComposable(
                card = card,
                onCardClick = {
                    onCardClick(card)
                },
                cardWidth = cardWidth
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }


}