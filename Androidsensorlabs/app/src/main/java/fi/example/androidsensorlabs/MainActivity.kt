package fi.example.androidsensorlabs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fi.example.androidsensorlabs.ui.theme.AndroidsensorlabsTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

const val N = 100 // Number of deposit operations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val results = bankProcess()
        setContent {
            AndroidsensorlabsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowResults(saldo1 = results.saldo1, saldo2 = results.saldo2)
                }
            }
        }
    }

    @Composable
    fun ShowResults(saldo1: Double, saldo2: Double) {
        Column {
            Text(text = "Saldo1: $saldo1")
            Text(text = "Saldo2: $saldo2")
        }
    }

    class Account {
        private var amount: Double = 0.0
        private val mutex = Mutex()

        suspend fun deposit(amount: Double) {
            mutex.withLock {
                val currentAmount = this.amount
                delay(1) // Simulates processing time
                this.amount = currentAmount + amount
                println("Deposited $amount, current balance: ${this.amount}")
            }
        }

        fun saldo(): Double = amount
    }

    fun withTimeMeasurement(title: String, isActive: Boolean = true, code: () -> Unit) {
        if (!isActive) return
        val time = measureTimeMillis { code() }
        Log.i("MSU", "Operation in '$title' took ${time} ms")
    }

    fun bankProcess(): Saldos {
        var saldo1: Double = 0.0
        var saldo2: Double = 0.0

        withTimeMeasurement("Single coroutine deposit $N times") {
            runBlocking {
                val account = Account()
                launch {
                    repeat(N) {
                        account.deposit(1.0)
                    }
                    saldo1 = account.saldo()
                    println("Saldo1 after single coroutine: $saldo1")
                }.join()
            }
        }

        withTimeMeasurement("Two $N times deposit coroutines together") {
            runBlocking {
                val account1 = Account()
                val account2 = Account()
                val job1 = launch {
                    repeat(N) {
                        account1.deposit(1.0)
                    }
                }
                val job2 = launch {
                    repeat(N) {
                        account2.deposit(1.0)
                    }
                }
                job1.join()
                job2.join()
                saldo2 = account1.saldo() + account2.saldo()
                println("Saldo2 after two coroutines: $saldo2")
            }
        }

        return Saldos(saldo1, saldo2)
    }

    data class Saldos(val saldo1: Double, val saldo2: Double)
}