package com.example.lab14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
                                    modifier = Modifier.clickable {
                                        bluetoothViewModel.setSelectedDevice(device)
                                        navController.navigate("graph")
                                    }
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
    Column {
        Text(text = "RSSI Graph")
        for ((index, value) in rssiValues.withIndex()) {
            Text(text = "Index $index: RSSI $value")
        }
    }
}