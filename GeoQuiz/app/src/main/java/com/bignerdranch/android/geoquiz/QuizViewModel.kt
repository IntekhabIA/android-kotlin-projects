package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // create list of questions
    internal val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    // declare index and answer counters
    var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var answeredQuestions = 0
    var correctAnswers: Int = 0
    val sizeOfQuestionBank: Int = questionBank.size

    // determine whether user cheated
    var isCheater: Boolean = questionBank[currentIndex].cheated

    // get the answer for the current question
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    // get the text for the current question
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    // move to the next question in the list of questions
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
        isCheater = questionBank[currentIndex].cheated
    }

    // move to the previous question in the list of questions
    fun moveToPrevious() {
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        } else {
            currentIndex -= 1
        }
        isCheater = questionBank[currentIndex].cheated
    }

}
