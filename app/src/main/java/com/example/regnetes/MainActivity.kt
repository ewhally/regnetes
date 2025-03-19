package com.example.regnetes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regnetes.entity.Location
import com.example.regnetes.ui.theme.RegnetesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.insertLocation(Location(name = "Zuerich", latitude = 8.02, longitude = 5.5))

        setContent {
            val locations by viewModel.allLocations.observeAsState(emptyList())
            RegnetesTheme {
                LocationList(locations)
            }
        }
    }

    @Composable
    fun LocationList(locations: List<Location>) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(locations) { location ->
                Text(text = "${location.name}: ${location.latitude}, ${location.longitude}")
            }
        }
    }
}


