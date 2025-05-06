import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.ns200310.sol.dashboard.sensors.EnvironmentSensors
import io.github.ns200310.sol.dashboard.sensors.GravitySensor.Gravity
import io.github.ns200310.sol.supabase_config.supabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.sqrt
@Composable
fun SensorCard(title: String, value: String, unit: String, icon: ImageVector) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
    ) {
        Row (
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    text = "$value",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Serializable
data class SensorReading(
    val user_id: String,
    val gravity: Double,
    val temperature: Double?,
    val pressure: Double?,
    val humidity: Double?,
    val recorded_at: Long
)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeterScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val gravityValues = remember { mutableStateOf(FloatArray(3)) }
    val context = LocalContext.current
    var temperature by remember { mutableStateOf("fetching...") }
    var pressure by remember { mutableStateOf("fetching...") }
    var humidity by remember { mutableStateOf("fetching...") }


    val environmentSensor = remember {
        EnvironmentSensors(context) { temp, press, hum ->
            temp?.let { temperature = "%.1f°C".format(it) }
            press?.let { pressure = "%.1f hPa".format(it) }
            hum?.let { humidity = "%.1f%%".format(it) }
        }
    }

    // Gravity sensor setup
    DisposableEffect(Unit) {
        val sensorManager = Gravity(context) { newValues ->
            gravityValues.value = newValues
        }
        environmentSensor.startListening()

        sensorManager.startListening()
        onDispose {
            sensorManager.stopListening()
            environmentSensor.stopListening()
        }
    }





    val magnitude = sqrt(
        gravityValues.value[0].pow(2) +
                gravityValues.value[1].pow(2) +
                gravityValues.value[2].pow(2)
    )


    val saveSensorData = suspend {
        try {

            val userId = supabase.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User not logged in")

            // Convert formatted strings back to numbers

            val reading = SensorReading(
                user_id = userId,
                gravity = magnitude.toDouble(),
                temperature = temperature.removeSuffix("°C").toDoubleOrNull(),
                pressure = pressure.removeSuffix(" hPa").toDoubleOrNull(),
                humidity = humidity.removeSuffix("%").toDoubleOrNull(),

                recorded_at = System.currentTimeMillis()
            )

            // Insert data into Supabase
            supabase.postgrest.from("sensor_data").insert(
                reading
            )

            // Show success message
            coroutineScope.launch {
                Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error saving data: ${e.message}")
            // Show error message
            coroutineScope.launch {
                Toast.makeText(context, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Observation Meter",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Gravity Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "GRAVITY",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    text = magnitude.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Temperature Card
        SensorCard(
            title = "TEMPERATURE",
            value = temperature.removeSuffix(""),
            unit = "°C",
            icon = Icons.Default.Thermostat
        )

        // Pressure Card
        SensorCard(
            title = "PRESSURE",
            value = pressure.removeSuffix(""),
            unit = "hPa",
            icon = Icons.Default.Speed
        )

        // Humidity Card
        SensorCard(
            title = "HUMIDITY",
            value = humidity.removeSuffix(""),
            unit = "%",
            icon = Icons.Default.WaterDrop
        )
        Button(
            onClick = {
                // Launch a coroutine to save data
                CoroutineScope(Dispatchers.IO).launch {
                    saveSensorData()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Data")
        }
    }



}