package com.example.regnetes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.regnetes.ui.theme.RegnetesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegnetesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val c1 = Location().apply {
                        name = "E"
                        latitude = 41.1
                        longtitude = 23.4
                    }
                    Greeting(
                        name = "Android",
                        location = c1,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String,location: Location, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name! Location: ${location.name}, Lat: ${location.latitude}, Lng: ${location.longtitude}\",",
        modifier = modifier
    )
}