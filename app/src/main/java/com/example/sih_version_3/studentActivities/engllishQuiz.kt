package com.example.sih_version_3.studentActivities

import android.content.res.AssetManager
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sih_version_3.R
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset

class engllishQuiz : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var nextButton: Button

    private lateinit var quizData: JSONArray
    private var currentQuestionIndex = -1
    private var score = 0 // Initialize the score to 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_engllish_quiz)
        questionTextView = findViewById(R.id.questionTextView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        nextButton = findViewById(R.id.nextButton)

        // Load quiz data from the JSON file in the assets folder
        loadQuizDataFromJson()

        // Display the first question
        loadNextQuestion()

        nextButton.setOnClickListener {
            // Check if an option is selected
            val selectedRadioButtonId = optionsRadioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == -1) {
                // No option selected
                return@setOnClickListener
            }

            // Check if the selected option is correct
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val selectedOptionIndex = optionsRadioGroup.indexOfChild(selectedRadioButton)
            val correctOptionIndex = quizData.getJSONObject(currentQuestionIndex)
                .getInt("correct_option")

            if (selectedOptionIndex == correctOptionIndex) {
                // The selected option is correct
                score++ // Increment the score for correct answers
            } else {
                // The selected option is incorrect
                // Handle incorrect answer logic here
            }

            // Load the next question
            loadNextQuestion()
        }
    }

    private fun loadQuizDataFromJson() {
        try {
            val assetManager: AssetManager = assets
            val inputStream = assetManager.open("englishquestion.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonText = String(buffer, Charset.forName("UTF-8"))
            quizData = JSONArray(jsonText)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++

        if (currentQuestionIndex < quizData.length()) {
            val question = quizData.getJSONObject(currentQuestionIndex)
                .getString("question")
            val options = quizData.getJSONObject(currentQuestionIndex)
                .getJSONArray("options")

            questionTextView.text = question
            optionsRadioGroup.removeAllViews()

            // Randomize the order of answer options
            val optionIndices = (0 until options.length()).toList().shuffled()
            for (i in optionIndices) {
                val option = options.getString(i)
                val radioButton = RadioButton(this)
                radioButton.text = option
                optionsRadioGroup.addView(radioButton)
            }

            // Clear the selected option and enable the "Next" button
            optionsRadioGroup.clearCheck()
            nextButton.isEnabled = true
        } else {
            // End of the quiz
            // Show a toast message with the score
            Toast.makeText(
                this,
                "Quiz completed. Your score: $score / 20",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
