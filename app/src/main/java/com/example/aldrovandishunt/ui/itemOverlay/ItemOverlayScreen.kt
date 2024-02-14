package com.example.aldrovandishunt.ui.itemOverlay

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.ui.theme.primaryOrange
import com.google.android.filament.Engine
import io.github.sceneview.Scene
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberNode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ItemOverlayScreen(
    onDismissRequest: () -> Unit,
    card: Card? = null,
    onARClick: () -> Unit,
    itemOverlayOpened: Boolean,
    engine: Engine,
    modelLoader: ModelLoader,
    cameraNode: CameraNode,
    centerNode: Node,
) {
    val height = LocalConfiguration.current.screenHeightDp.dp
    ModalBottomSheet(onDismissRequest = { onDismissRequest() },
        modifier = Modifier.height(height),
        sheetState = SheetState(
            skipPartiallyExpanded = true,
        ),
        containerColor = Color.DarkGray,
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(80.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = primaryOrange)
                        .align(Alignment.Center)
                )
            }
        }


    ) {

        if (card != null) {
            Log.v("ItemOverlayScreen", "Card: $card")
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column (modifier=Modifier.weight(1f)){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color.Black)

                    ) {
                        if (itemOverlayOpened) {
                            val cameraNode = rememberCameraNode(engine).apply {
                                position = Position(z = 1.5f)
                            }
                            Scene(modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                                engine = engine,
                                modelLoader = modelLoader,
                                cameraNode = cameraNode,
                                childNodes = listOf(centerNode, rememberNode {
                                    ModelNode(
                                        modelInstance = modelLoader.createModelInstance(
                                            assetFileLocation = "models/${card.name}AR.glb"
                                        ), scaleToUnits = 1.5f
                                    )
                                }),
                                onFrame = {
                                    cameraNode.lookAt(centerNode)
                                })
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        val titleStyle = TextStyle(
                            color = primaryOrange, fontSize = 24.sp
                        )
                        Text(
                            text = "Name",
                            style = titleStyle,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(text = card.name, color = Color.White)
                        Text(
                            text = "Description",
                            style = titleStyle,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(text = card.description, color = Color.White)
                    }
                }

                Button(
                    onClick = {
                        onARClick()
                    },
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .wrapContentHeight()
                ) {
                    Text(text = "View in AR")
                    Icon(
                        imageVector = Icons.Default.ViewInAr, contentDescription = "View in AR icon"
                    )

                }
            }
        }
    }
}