import android.media.MediaRecorder

import android.os.Build
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.IOException

class VoiceNoteViewModel : ViewModel() {

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private val _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean> get() = _isRecording

    fun startRecording() {
        val audioDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) // Scoped Storage on Android 10+

        } else {
            // For earlier versions, we can use the legacy approach (not recommended on Android 10+)
            Environment.getExternalStorageDirectory()
        }

        val outputFile = File(audioDirectory, "/topics/files/voice_note_${System.currentTimeMillis()}.3gp")

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile.absolutePath)

            try {
                prepare()
                start()
                audioFile = outputFile
                _isRecording.postValue(true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        _isRecording.postValue(false)
    }

    fun getAudioFile(): File? {
        return audioFile
    }
}
