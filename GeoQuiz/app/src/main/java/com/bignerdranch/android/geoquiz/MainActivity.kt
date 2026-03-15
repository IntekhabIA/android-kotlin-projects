package com.bignerdranch.android.geoquiz
import android.app.Activity
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        //Handle the result
        if(result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?:false
        }
    }

    // on app startup
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        // check if the actual answer to the question is true
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }

        // check if the actual answer to the question is false
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        /* move to the next question in the list of questions
        OR calculate their score if the user has answered all the questions */
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            val rootView: View = findViewById(android.R.id.content)
            updateQuestion()
        }

        // move to the previous question in the list of questions
        binding.previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        /* move to the next question in the list of
        questions if clicking the question text */
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        if (quizViewModel.sizeOfQuestionBank == quizViewModel.answeredQuestions){
            binding.submitButton.visibility = View.VISIBLE
        }
        // calculate user's score
        binding.submitButton.setOnClickListener {
            calculateScore()
        }

        binding.cheatButton.setOnClickListener {
            //start cheat activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }
        updateQuestion()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }
    }

    // debug function
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    // debug function
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
        Log.d(TAG,"answered Question: ${quizViewModel.answeredQuestions}, size of questionBank: ${quizViewModel.sizeOfQuestionBank} \n " +
                "current Index: ${quizViewModel.currentIndex}")
    }

    // debug function
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    // debug function
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    /* update which question is on the screen.
    If it has been answered, the buttons should not enabled */
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        if (quizViewModel.questionBank[quizViewModel.currentIndex].hasBeenAnswered) {
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
            binding.cheatButton.isEnabled = false
        } else {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
            binding.cheatButton.isEnabled = true
        }
    }

    // compare user's answer with the correct answer
    private fun checkAnswer(userAnswer: Boolean){
        val rootView: View = findViewById(android.R.id.content)
        val snackBarColor: Int

        /* set the values to be displayed based on
        whether the user cheated or answered correctly */
        val messageResId = when{
            quizViewModel.isCheater -> {
                snackBarColor = Color.BLUE
                R.string.judgement_toast
            }
            userAnswer == quizViewModel.currentQuestionAnswer ->{
                quizViewModel.correctAnswers++
                snackBarColor = Color.GREEN
                R.string.correct_toast
            }
            else ->{
                snackBarColor = Color.RED
                R.string.incorrect_toast
            }
        }

        /* prevent user from answering again,
        update counters and hasBeenAnswered flag */
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
        binding.cheatButton.isEnabled = false
        quizViewModel.questionBank[quizViewModel.currentIndex].hasBeenAnswered = true
        quizViewModel.answeredQuestions++

        // display whether user answered correctly
        Snackbar.make(rootView, messageResId, Snackbar.LENGTH_SHORT).setAction("Action", null)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(snackBarColor)
            .show()
        Log.d(TAG,"answered Question: ${quizViewModel.answeredQuestions}, size of questionBank: ${quizViewModel.sizeOfQuestionBank} \n " +
                "current Index: ${quizViewModel.currentIndex}")

        if(quizViewModel.answeredQuestions == quizViewModel.sizeOfQuestionBank)
        {
            binding.submitButton.visibility = View.VISIBLE
        }


    }

    // calculate the user's score
    private fun calculateScore() {
        // calculate score, format it to two decimal
        val score = (quizViewModel.correctAnswers.toFloat() / quizViewModel.sizeOfQuestionBank) * 100
        val formattedScore = String.format("%.2f", score)
        val rootView: View = findViewById(android.R.id.content)

        // display score
        Snackbar.make(rootView, "$formattedScore %", Snackbar.LENGTH_INDEFINITE).setAction("Action", null)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .show()

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

}