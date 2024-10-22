package com.example.aldrovandishunt.ui.caccia

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode

@Composable
fun ImageRecognition(
    modifier: Modifier = Modifier,
    cardName: String,
    onSuccess: () -> Unit
) {
    val augmentedImageNodes = mutableListOf<AugmentedImageNode>()
    var counter = 0
    var stopOnException = false
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ARSceneView(context).apply {
                configureSession { session, config ->
                    while (!stopOnException) {
                        try {
                            config.addAugmentedImage(
                                session, cardName,
                                context.assets.open("imageRecognition/$cardName$counter.jpg")
                                    .use(BitmapFactory::decodeStream)
                            )
                            counter++
                        } catch (e: Exception) {
                            stopOnException = true
                        }
                    }
                }
                onSessionUpdated = { session, frame ->
                    frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                        if (augmentedImageNodes.none { it.imageName == augmentedImage.name }) {
                            onSuccess()
                        }
                    }
                }
            }
        })
}