package com.example.nytviewer.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nytviewer.navigation.NavGraphInterface
import com.example.nytviewer.navigation.Navigator
import com.example.nytviewer.ui.theme.NYTViewerTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var navGraph: NavGraphInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navigator = navigator, navGraph = navGraph)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYTViewerTheme {
        Greeting("Android")
    }
}