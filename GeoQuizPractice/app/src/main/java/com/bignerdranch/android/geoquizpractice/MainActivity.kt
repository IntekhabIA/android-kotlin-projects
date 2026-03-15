package com.bignerdranch.android.geoquizpractice

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.bignerdranch.android.geoquizpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var trueButton: Button
//    private lateinit var falseButton: Button

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        trueButton = findViewById(R.id.true_button)
//        falseButton = findViewById(R.id.false_button)

//        val rootView: View = findViewById(android.R.id.content)

//        trueButton.setOnClickListener { view: View ->
        binding.trueButton.setOnClickListener { view: View ->
            //Do something in response to the click here
//            Toast.makeText(
//                this,
//                R.string.correct_toast,
//                Toast.LENGTH_SHORT
//            ).show()
//            Snackbar.make(rootView, R.string.correct_toast, Snackbar.LENGTH_SHORT).setAction("Action", null)
//                .setActionTextColor(Color.WHITE)
//                .setBackgroundTint(Color.GREEN)
//                .show()
            checkAnswer(true)


        }
//        falseButton.setOnClickListener { view: View ->
        binding.falseButton.setOnClickListener { view: View ->
            //Do something in response to the click here
//            Toast.makeText(
//                this,
//                R.string.incorrect_toast,
//                Toast.LENGTH_SHORT
//            ).show()
//            Snackbar.make(rootView, R.string.incorrect_toast, Snackbar.LENGTH_SHORT).setAction("Action", null)
//                .setActionTextColor(Color.WHITE)
//                .setBackgroundTint(Color.RED)
//                .show()
            checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
//            val questionTextResId = questionBank[currentIndex].textResId
//            binding.questionTextView.setText(questionTextResId)
            updateQuestion()
        }

//        val questionTextResId = questionBank[currentIndex].textResId
//        binding.questionTextView.setText(questionTextResId)
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val rootView: View = findViewById(android.R.id.content)
        val correctAnswer = questionBank[currentIndex].answer
        val snackBarColor:Int

        val messageResId = if (userAnswer == correctAnswer) {
            snackBarColor = Color.GREEN
            R.string.correct_toast
        } else {
            snackBarColor = Color.RED
            R.string.incorrect_toast
        }
        Snackbar.make(rootView, messageResId, Snackbar.LENGTH_SHORT).setAction("Action", null)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(snackBarColor)
            .show()
    }

}