package com.example.sih_version_3.studentActivities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sih_version_3.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class improveYourSpeech : AppCompatActivity() {


    private val client = OkHttpClient()

    private lateinit var startVoiceRecognitionButton: Button
    private lateinit var recognizedText: TextView
    private lateinit var correctedText: TextView // Declare correctedText at the class level
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_improve_your_speech)


        startVoiceRecognitionButton = findViewById(R.id.startVoiceRecognitionButton)
        recognizedText = findViewById(R.id.recognizedText)
        correctedText = findViewById(R.id.correctedText) // Initialize correctedText

        // Check and request audio recording permission if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognitionIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        startVoiceRecognitionButton.setOnClickListener {
            startVoiceRecognition()
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // Called when the recognizer is ready to receive speech input
            }

            override fun onBeginningOfSpeech() {
                // Called when the user starts speaking
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Called when the amplitude of the input voice changes
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Called when sound data is received
            }

            override fun onEndOfSpeech() {
                // Called when the user stops speaking
            }

            override fun onError(error: Int) {
                // Called when an error occurs during recognition
                Toast.makeText(
                    applicationContext,
                    "Error during recognition. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResults(results: Bundle?) {
                // Called when recognition is successful
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val reco = matches[0]
                    // Display the recognized text
                    recognizedText.text = reco

                    val makePromptText = recognizedText.text.toString()

                    makePromptForApi(makePromptText)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Called when partial recognition results are available
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Called when an event related to recognition occurs
            }
        })
    }

    private fun makePromptForApi(makePromptText: String) {
        val sentence = "${makePromptText} . correct grammar and restructure the sentence"
        getResponse(sentence)
    }

    private fun getResponse(sentence: String) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/engines/text-davinci-003/completions"

        val requestBody = """
            {
            "prompt": "$sentence",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult = jsonArray.getJSONObject(0).getString("text")

                    // Display the corrected text in the TextView
                    runOnUiThread {
                        correctedText.text = textResult
                    }
                } else {
                    Log.v("data", "empty")
                }
            }
        })
    }

    private fun startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {
            // Start voice recognition
            speechRecognizer.startListening(recognitionIntent)
        } else {
            Toast.makeText(
                applicationContext,
                "Please grant audio recording permission to use voice recognition.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}