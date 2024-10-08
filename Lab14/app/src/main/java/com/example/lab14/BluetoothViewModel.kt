package com.example.lab14

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothViewModel(private val context: Context) : ViewModel() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private val _deviceList = MutableStateFlow<List<BluetoothDeviceWithRSSI>>(emptyList())
    val deviceList: StateFlow<List<BluetoothDeviceWithRSSI>> get() = _deviceList

    private val _rssiValues = MutableStateFlow<List<Int>>(emptyList())
    val rssiValues: StateFlow<List<Int>> get() = _rssiValues

    private val _selectedDevice = MutableStateFlow<BluetoothDeviceWithRSSI?>(null)
    val selectedDevice: StateFlow<BluetoothDeviceWithRSSI?> get() = _selectedDevice

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val rssi = result.rssi
            addDevice(device, rssi)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            results.forEach { result ->
                val device = result.device
                val rssi = result.rssi
                addDevice(device, rssi)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            // Handle scan failure
        }
    }

    @SuppressLint("MissingPermission")
    fun startBluetoothScan() {
        if (hasPermissions()) {
            viewModelScope.launch {
                // Clear the device list on new scan
                _deviceList.value = emptyList()
                // Clear the RSSI values on new scan
                _rssiValues.value = emptyList()
                bluetoothLeScanner?.startScan(scanCallback)
            }
        } else {
            // Handle the case where permissions are not granted
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDeviceWithRSSI) {
        // Implement the logic to connect to the Bluetooth device
        val bluetoothDevice = device.device
        // Example: Initiate a connection to the device
        bluetoothDevice.createBond()
    }

    @SuppressLint("MissingPermission")
    fun stopBluetoothScan() {
        if (hasPermissions()) {
            bluetoothLeScanner?.stopScan(scanCallback)
        } else {
            // Handle the case where permissions are not granted
        }
    }

    fun setSelectedDevice(device: BluetoothDeviceWithRSSI) {
        _selectedDevice.value = device
    }

    private fun addDevice(device: BluetoothDevice, rssi: Int) {
        viewModelScope.launch {
            val existingDevice = _deviceList.value.find { it.device.address == device.address }
            if (existingDevice == null) {
                _deviceList.value = _deviceList.value + BluetoothDeviceWithRSSI(device, rssi)
            }
            // Collect RSSI values
            _rssiValues.value = _rssiValues.value + rssi
        }
    }

    private fun hasPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}

data class BluetoothDeviceWithRSSI(val device: BluetoothDevice, val rssi: Int) {
    val name: String
        @SuppressLint("MissingPermission")
        get() = device.name ?: "Unknown Device"
}
