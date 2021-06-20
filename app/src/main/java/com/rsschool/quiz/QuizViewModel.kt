package com.rsschool.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private var questionList : MutableLiveData<List<QuizQuestion>> = MutableLiveData()

    init {
        questionList.value = QuizData.getQuizQuestions()
    }

    fun getQuestions() = questionList
}