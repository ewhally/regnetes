package com.example.regnetes

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regnetes.entity.Location
import com.example.regnetes.ui.theme.RegnetesTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.Manifest



class MainActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val REQUEST_CODE_PERMISSION = 1001


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_PERMISSION
                )
            }
        }

        NotificationHelper.createNotificationChannel(this)

        setContent {
            RegnetesTheme {
                val locations by viewModel.allLocations.observeAsState(emptyList())
                Column(modifier = Modifier.fillMaxSize()) {
                    LocationScreen(
                        locations = locations,
                        onAddLocation = { name ->
                            viewModel.insertLocation(Location(name = name))
                        },
                        onDeleteLocation = { location ->
                            viewModel.deleteLocation(location)
                        }
                    )
                    WeatherSection(locations = locations)
                }
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

@Composable
fun WeatherSection(locations: List<Location>) {
    val context = LocalContext.current

    var precipitationResults by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val weatherRepository = WeatherRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    val results = mutableMapOf<String, Double>()
                    val rainLocations = mutableListOf<String>()

                    for (location in locations) {
                        val locationName = location.name.trim()
                        val precipitation = weatherRepository.fetchWeatherForLocation(locationName) ?: 0.0

                        results[locationName] = precipitation

                        if (precipitation > 2.5) {
                            rainLocations.add(locationName)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        precipitationResults = results
                        isLoading = false

                        if (rainLocations.isNotEmpty()) {
                            val message = rainLocations.joinToString(", ") { "$it is raining today" }
                            NotificationHelper.showNotification(context, "Weather Alert", message)
                        }
                    }
                }
            }
        ) {
            Text("Fetch Weather")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Text("Loading...")
        } else {
            precipitationResults.forEach { (locationName, precipitation) ->
                val status = if (precipitation > 2.5) "It is raining today" else "No rain"
                Text("$locationName: $status ($precipitation mm)")
            }
        }
    }
}
