package com.example.sceneview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.sceneview.ui.theme.SceneViewTheme
import com.gorisse.thomas.lifecycle.lifecycleScope
import io.github.sceneview.Scene
import io.github.sceneview.loaders.loadHdrIndirectLight
import io.github.sceneview.loaders.loadHdrSkybox
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.nodes.ModelNode
import io.github.sceneview.nodes.Node

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SceneViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ModelScreen()
                }
            }
        }
    }
}

@Composable
fun ModelScreen() {
    val nodes = remember { mutableStateListOf<Node>() }

    Box(modifier = Modifier.fillMaxSize()) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            onCreate = { sceneView ->
                // Apply your configuration
                sceneView.lifecycleScope.launchWhenCreated {
                    val hdrFile = "environments/studio_small_09_2k.hdr"
                    sceneView.loadHdrIndirectLight(hdrFile, specularFilter = true) {
                        intensity(30_000f)
                    }
                    sceneView.loadHdrSkybox(hdrFile) {
                        intensity(50_000f)
                    }

                    val model = sceneView.modelLoader.loadModel("models/MaterialSuite.glb")!!
                    val modelNode = ModelNode(sceneView, model).apply {
                        transform(
                            position = Position(z = -4.0f),
                            rotation = Rotation(x = 15.0f)
                        )
                        scaleToUnitsCube(2.0f)
                        // TODO: Fix centerOrigin
                        // centerOrigin(Position(x=-1.0f, y=-1.0f))
                        playAnimation()
                    }
                    sceneView.addChildNode(modelNode)
                }
            }
        )
    }
}
