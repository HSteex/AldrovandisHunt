package com.example.aldrovandishunt.ui.myCollection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.data.database.Carte

enum class Rarity {
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}

@Composable
fun Card(
    card: Carte,
    cardWidth: Dp,
    onCardClick: () -> Unit = {},

    ) {
    var backgroundColor = when (card.rarita) {
        Rarity.UNCOMMON -> Brush.verticalGradient(
            0.0f to Color(0xFF90DD90),
            1.0f to Color(0xFF229940)
        )

        Rarity.RARE -> Brush.verticalGradient(
            0.0f to Color(0xFF6E91E9),
            1.0f to Color(0xFF3A5BE9)
        )

        Rarity.EPIC -> Brush.verticalGradient(
            0.0f to Color(0xFFDB83D6),
            1.0f to Color(0xFFA259B6)
        )

        Rarity.LEGENDARY -> Brush.verticalGradient(
            0.0f to Color(0xFFCEAB76),
            1.0f to Color(0xFFE6A636)
        )
    }


    Box(
        modifier = Modifier
            .width(cardWidth)
            .shadow(
                elevation = 8.dp, shape = RoundedCornerShape(8.dp)
            )
            .background(brush = backgroundColor)
            .clickable { onCardClick() }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = card.nome,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = R.drawable.cardtest_delete),
                contentDescription = "Image of ${card.nome}",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.White)
                    .width(cardWidth-4.dp)
                    .height(cardWidth-8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            RarityBadge(rarity = card.rarita)

        }


    }
}

@Composable
fun RarityBadge(rarity: Rarity) {

    val backgroundColor = when (rarity) {
        Rarity.UNCOMMON -> Color(0xFF8AD18A)
        Rarity.RARE -> Color(0xFF6E91E9)
        Rarity.EPIC -> Color(0xFFF086EA)
        Rarity.LEGENDARY -> Color(0xFFF5B85C)
    }
    val textColor = when (rarity) {
        Rarity.UNCOMMON -> Color(0xFF229940)
        Rarity.RARE -> Color(0xFF3A5BE9)
        Rarity.EPIC -> Color(0xFFA259B6)
        Rarity.LEGENDARY -> Color(0xFFE6A636)
    }
    val text = when (rarity) {
        Rarity.UNCOMMON -> stringResource(R.string.uncommonRarity)
        Rarity.RARE -> stringResource(R.string.rareRarity)
        Rarity.EPIC -> stringResource(R.string.epicRarity)
        Rarity.LEGENDARY -> stringResource(R.string.legendaryRarity)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = backgroundColor)
            .defaultMinSize(minWidth = 80.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style= TextStyle(
                fontWeight = FontWeight.Bold,
                color = textColor,
                shadow = Shadow(
                    color = textColor,
                    blurRadius = 1f,
                )
            )
        )
    }
}
