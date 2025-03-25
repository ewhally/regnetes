package com.example.regnetes

import android.os.Bundle
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.regnetes.ui.theme.RegnetesTheme
import androidx.compose.ui.platform.LocalContext

class RainStatusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isRaining = intent.getBooleanExtra("isRaining", false)

        setContent {
            RegnetesTheme {
                RainStatusScreen(isRaining = isRaining)
            }
        }
    }
}

@Composable
fun RainStatusScreen(isRaining: Boolean) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isRaining) {
            Image(painter = painterResource(id = R.drawable.rain_image), contentDescription = "Rainy Weather")
        } else {
            Image(painter = painterResource(id = R.drawable.sunny_image), contentDescription = "Sunny Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = if (isRaining) "It's raining today!" else "No rain today!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            activity?.finish()
        }) {
            Text("Go Back")
        }
    }
}
