package com.example.lab11


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lab11.ui.theme.Lab11Theme

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private var isScanning by mutableStateOf(false)
    private val scanResults = mutableStateListOf<ScanResult>()
    private val scanPeriod: Long = 10000 // 10 seconds

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            startBluetoothScan()
        } else {
            Log.e("BluetoothScan", "Permissions denied.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        if (!hasPermissions()) {
            requestPermissions()
        }

        setContent {
            Lab11Theme {
                BluetoothScannerApp(
                    scanResults = scanResults,
                    isScanning = isScanning,
                    startScan = { startBluetoothScan() },
                    stopScan = { stopBluetoothScan() }
                )
            }
        }
    }

    private fun hasPermissions(): Boolean {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        requestPermissionsLauncher.launch(permissions.toTypedArray())
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                    ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("BluetoothScan", "Device found: ${result.device.name ?: "Unnamed Device"} - ${result.device.address}")
                } else {
                    Log.d("BluetoothScan", "Device found: ${result.device.address}")
                }
                scanResults.add(result)
            } catch (e: SecurityException) {
                Log.e("BluetoothScan", "SecurityException: ${e.message}")
            }
        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            results?.forEach { result ->
                result?.let {
                    try {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                            ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                            Log.d("BluetoothScan", "Device found: ${it.device.name ?: "Unnamed Device"} - ${it.device.address}")
                        } else {
                            Log.d("BluetoothScan", "Device found: ${it.device.address}")
                        }
                        scanResults.add(it)
                    } catch (e: SecurityException) {
                        Log.e("BluetoothScan", "SecurityException: ${e.message}")
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothScan", "Scan failed with error: $errorCode")
        }
    }

    private fun startBluetoothScan() {
        if (!hasPermissions()) {
            requestPermissions()
            return
        }

        if (!isLocationEnabled()) {
            Log.e("BluetoothScan", "Location services are disabled. Please enable location services.")
            return
        }

        if (!isScanning) {
            if (bluetoothAdapter.isEnabled) {
                isScanning = true
                scanResults.clear()
                try {
                    bluetoothLeScanner.startScan(scanCallback)
                    Log.d("BluetoothScan", "Started Bluetooth scan")

                    // Stop scanning after the predefined period
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopBluetoothScan()
                    }, scanPeriod)
                } catch (e: SecurityException) {
                    Log.e("BluetoothScan", "SecurityException: ${e.message}")
                }
            } else {
                Log.d("BluetoothScan", "Bluetooth is disabled. Please enable Bluetooth.")
            }
        }
    }

    private fun stopBluetoothScan() {
        if (isScanning) {
            isScanning = false
            try {
                bluetoothLeScanner.stopScan(scanCallback)
                Log.d("BluetoothScan", "Stopped scanning for devices")
            } catch (e: SecurityException) {
                Log.e("BluetoothScan", "SecurityException: ${e.message}")
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Composable
    fun BluetoothScannerApp(
        scanResults: List<ScanResult>,
        isScanning: Boolean,
        startScan: () -> Unit,
        stopScan: () -> Unit
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(onClick = {
                if (isScanning) stopScan() else startScan()
            }) {
                Text(text = if (isScanning) "Stop Scanning" else "Start Scanning")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(scanResults) { result ->
                    val deviceName = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                        ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        result.device.name ?: "Unnamed Device"
                    } else {
                        "Permission Required"
                    }
                    val macAddress = result.device.address
                    val rssi = result.rssi

                    Text(text = "$deviceName ($macAddress) RSSI: $rssi")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewBluetoothScannerApp() {
        Lab11Theme {
            BluetoothScannerApp(
                scanResults = emptyList(),
                isScanning = false,
                startScan = {},
                stopScan = {}
            )
        }
    }
}