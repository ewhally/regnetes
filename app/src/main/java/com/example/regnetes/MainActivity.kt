package com.example.regnetes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regnetes.entity.Location
import com.example.regnetes.ui.theme.RegnetesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegnetesTheme {
                val locations by viewModel.allLocations.observeAsState(emptyList())
                LocationScreen(
                    locations = locations,
                    onAddLocation = { name ->
                        viewModel.insertLocation(Location(name = name))
                    },
                    onDeleteLocation = { location ->
                        viewModel.deleteLocation(location)
                    }
                )
            }
        }
    }
}

@Composable
fun LocationScreen(
    locations: List<Location>,
    onAddLocation: (String) -> Unit,
    onDeleteLocation: (Location) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Add location UI
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAddLocation(text)
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Location")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location list
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(locations) { location ->
                LocationItem(
                    location = location,
                    onDelete = { onDeleteLocation(location) }
                )
            }
        }
    }
}

@Composable
fun LocationItem(location: Location, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location.name,
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


