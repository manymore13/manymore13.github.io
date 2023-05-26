package com.yingsheng.blektx.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.os.Handler
import androidx.annotation.Keep

/**
 * ble搜索
 */
class BleSearcher(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val handler: Handler
) {
    companion object {
        const val SCAN_PERIOD: Long = 10000
    }

    private var mScanning: Boolean = false

    private var mBleScanCallback: ScanCallback? = null

    fun scanBLeDevice(scanMillis: Long = SCAN_PERIOD, bleScanCallback: ScanCallback) {

        val scanner = bluetoothAdapter?.bluetoothLeScanner?:return
        scanner.stopScan(bleScanCallback)
        mBleScanCallback = bleScanCallback

        // Stops scanning after a pre-defined scan period.
        handler.postDelayed({
            mScanning = false
            stopLeScanDevice()
        }, scanMillis)
        mScanning = true
        scanner.startScan(bleScanCallback)

    }

    fun stopLeScanDevice() {
        try{
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(mBleScanCallback)
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }
    }
}