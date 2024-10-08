package com.example.lab14

import android.app.ActionBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create an instance of BluetoothViewModel using BluetoothViewModelFactory
        val bluetoothViewModelFactory = BluetoothViewModelFactory(this)
        val bluetoothViewModel = ViewModelProvider(this, bluetoothViewModelFactory)
            .get(BluetoothViewModel::class.java)

        setContent {
            // Pass the ViewModel instance to the Composable function
            BluetoothScannerScreen(bluetoothViewModel = bluetoothViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothScannerScreen(bluetoothViewModel: BluetoothViewModel) {
    val devices by bluetoothViewModel.deviceList.collectAsState()
    val rssiValues by bluetoothViewModel.rssiValues.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "scanner") {
        composable("scanner") {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Bluetooth Scanner") }) },
                content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        Button(onClick = { bluetoothViewModel.startBluetoothScan() }) {
                            Text("Start Scanning")
                        }
                        Button(onClick = { bluetoothViewModel.stopBluetoothScan() }) {
                            Text("Stop Scanning")
                        }
                        LazyColumn {
                            items(devices) { device ->
                                Text(
                                    text = "${device.name} - RSSI: ${device.rssi}",
                                    modifier = Modifier
                                        .clickable {
                                            bluetoothViewModel.setSelectedDevice(device)
                                            bluetoothViewModel.connectToDevice(device) // Connect to the mouse
                                            navController.navigate("graph")
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
        composable("graph") {
            GraphScreen(rssiValues)
        }
    }
}

@Composable
fun GraphScreen(rssiValues: List<Int>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "RSSI Graph", style = MaterialTheme.typography.titleLarge)

        // Create a line chart for the RSSI values
        val entries = rssiValues.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val dataSet = LineDataSet(entries, "RSSI Values").apply {
            color = MaterialTheme.colorScheme.primary.toArgb() // Set the line color
            valueTextColor = MaterialTheme.colorScheme.onBackground.toArgb() // Set value text color
        }

        LineChart(dataSet)
    }
}

@Composable
fun LineChart(dataSet: LineDataSet) {
    val lineData = LineData(dataSet)

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                this.data = lineData
                this.description.isEnabled = false
                this.xAxis.setDrawGridLines(false)
                this.axisLeft.setDrawGridLines(false)
                this.axisRight.isEnabled = false
                this.legend.isEnabled = true
                layoutParams = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    400
                )
            }








        },
        update = { chart ->
            chart.data = lineData
            chart.invalidate() // Refresh the chart
        },
        modifier = Modifier
            .fillMaxWidth() // Make it full width
            .height(400.dp) // Set height as per your requirement
    )
}

