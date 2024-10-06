package com.example.lab13

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.UUID

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {
    private val _heartRate = MutableLiveData<Int>()
    val heartRate: LiveData<Int> get() = _heartRate

    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> get() = _connectionStatus

    private var bluetoothGatt: BluetoothGatt? = null

    fun updateHeartRate(bpm: Int) {
        _heartRate.value = bpm
    }

    fun updateConnectionStatus(status: String) {
        _connectionStatus.value = status
    }

    fun connectToDevice(device: BluetoothDevice) {
        try {
            bluetoothGatt = device.connectGatt(getApplication(), false, gattCallback)
        } catch (e: SecurityException) {
            Log.e("BluetoothViewModel", "Permission denied: ${e.message}")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("BluetoothViewModel", "Connected to GATT server.")
                    updateConnectionStatus("Connected")
                    try {
                        bluetoothGatt?.discoverServices()
                    } catch (e: SecurityException) {
                        Log.e("BluetoothViewModel", "Permission denied: ${e.message}")
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("BluetoothViewModel", "Disconnected from GATT server.")
                    updateConnectionStatus("Disconnected")
                    try {
                        bluetoothGatt?.close()
                    } catch (e: SecurityException) {
                        Log.e("BluetoothViewModel", "Permission denied: ${e.message}")
                    }
                    bluetoothGatt = null
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BluetoothViewModel", "Services discovered.")
                readHeartRateCharacteristic()
            } else {
                Log.w("BluetoothViewModel", "onServicesDiscovered received: $status")
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val heartRateValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                updateHeartRate(heartRateValue)
                Log.d("BluetoothViewModel", "Heart Rate: $heartRateValue")
            }
        }
    }

    private fun readHeartRateCharacteristic() {
        val heartRateCharacteristicUUID = "00002A37-0000-1000-8000-00805f9b34fb"
        val serviceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
        val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(UUID.fromString(heartRateCharacteristicUUID))
        characteristic?.let {
            try {
                bluetoothGatt?.readCharacteristic(it)
            } catch (e: SecurityException) {
                Log.e("BluetoothViewModel", "Permission denied: ${e.message}")
            }
        }
    }
}