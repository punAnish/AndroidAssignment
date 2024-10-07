package fi.example.lab15

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*

class MainActivity : ComponentActivity() {
    private var isRecording = false
    private lateinit var audioFile: File
    private val sampleRate = 44100
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
    )
    private lateinit var audioRecord: AudioRecord
    private lateinit var audioTrack: AudioTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request microphone permission
        if (!hasPermissions()) {
            requestAudioPermissions()
        }

        // Setup the file to store audio recording
        audioFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "recording.pcm")

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                MainScreen(
                    modifier = Modifier.padding(innerPadding),
                    onStartRecordingClick = { startRecording() },
                    onStopRecordingClick = { stopRecording() },
                    onPlayRecordingClick = { playRecording() }
                )
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    private fun startRecording() {
        if (!hasPermissions()) {
            requestAudioPermissions()
            return
        }

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
            audioRecord.startRecording()
            isRecording = true

            Thread {
                writeAudioDataToFile()
            }.start()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun writeAudioDataToFile() {
        val audioData = ByteArray(bufferSize)
        FileOutputStream(audioFile).use { outputStream ->
            try {
                while (isRecording) {
                    val read = audioRecord.read(audioData, 0, bufferSize)
                    if (read > 0) {
                        outputStream.write(audioData, 0, read)
                    }
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Recording error: $e")
            }
        }
    }

    private fun stopRecording() {
        isRecording = false
        audioRecord.stop()
        audioRecord.release()
    }

    private fun playRecording() {
        if (!audioFile.exists()) return

        audioTrack = AudioTrack.Builder()
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()

        audioTrack.play()

        Thread {
            FileInputStream(audioFile).use { inputStream ->
                val audioData = ByteArray(bufferSize)
                var read: Int
                while (inputStream.read(audioData).also { read = it } != -1) {
                    audioTrack.write(audioData, 0, read)
                }
                audioTrack.stop()
                audioTrack.release()
            }
        }.start()
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onStartRecordingClick: () -> Unit,
    onStopRecordingClick: () -> Unit,
    onPlayRecordingClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(onClick = onStartRecordingClick) {
            Text(text = "Start Recording")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStopRecordingClick) {
            Text(text = "Stop Recording")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onPlayRecordingClick) {
            Text(text = "Play Recording")
        }
    }
}

