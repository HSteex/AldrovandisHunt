package com.example.aldrovandishunt.ui.caccia

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContentProviderCompat.requireContext
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.node.ModelNode

@Composable
fun ImageRecognition (
    modifier: Modifier = Modifier,
    cardName: String,
    onSuccess: () -> Unit
){
    val augmentedImageNodes = mutableListOf<AugmentedImageNode>()
    AndroidView(
        modifier = modifier,
        factory = { context ->
        ARSceneView(context).apply {
            configureSession { session, config ->
                config.addAugmentedImage(
                    session, cardName,
                    context.assets.open("augmentedImage/$cardName.jpg")
                        .use(BitmapFactory::decodeStream)
                )
                config.addAugmentedImage(
                    session, cardName,
                    context.assets.open("augmentedImage/T-Rex2.jpg")
                        .use(BitmapFactory::decodeStream)
                )
            }
            onSessionUpdated = { session, frame ->
                frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                    if (augmentedImageNodes.none { it.imageName == augmentedImage.name }) {
                        val augmentedImageNode = AugmentedImageNode(engine, augmentedImage).apply {
                            when (augmentedImage.name) {
                                cardName -> onSuccess()
                            }
                        }
                        addChildNode(augmentedImageNode)
                        augmentedImageNodes += augmentedImageNode
                    }
                }
            }
        }
    })

}