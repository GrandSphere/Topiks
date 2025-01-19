package com.example.topics2.activities
import VoiceNoteViewModel
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.topics2.ui.themes.TopicsTheme
class TTSActivity : ComponentActivity() {


        private lateinit var voiceNoteViewModel: VoiceNoteViewModel

        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val deniedPermissions = permissions.filter { !it.value }

                if (deniedPermissions.isNotEmpty()) {
                    // Check which permission is denied and show a detailed message
                    val deniedPermissionNames = deniedPermissions.keys.joinToString(", ")
                    val deniedPermissionMessage = when {
                        deniedPermissions.contains(Manifest.permission.RECORD_AUDIO) -> "Audio recording permission is denied."
                        deniedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> "Storage permission is denied."
                        else -> "Some permissions are denied."
                    }
                    Toast.makeText(this, deniedPermissionMessage, Toast.LENGTH_LONG).show()
                }
            }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                TopicsTheme {
                    VoiceNoteUI()
                }
            }

            // Initialize ViewModel
            voiceNoteViewModel = ViewModelProvider(this).get(VoiceNoteViewModel::class.java)

            // Request permissions on startup
            requestPermissions()
        }

        private fun requestPermissions() {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE // Only for pre-Android 10 devices
                )
            )
        }

        @Composable
        fun VoiceNoteUI() {
            val isRecording by voiceNoteViewModel.isRecording.observeAsState(false)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = if (isRecording) "Recording..." else "Press to Start Recording")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isRecording) {
                            voiceNoteViewModel.stopRecording()
                        } else {
                            voiceNoteViewModel.startRecording()
                        }
                    }
                ) {
                    Text(text = if (isRecording) "Stop Recording" else "Start Recording")
                }
            }
        }
    }
