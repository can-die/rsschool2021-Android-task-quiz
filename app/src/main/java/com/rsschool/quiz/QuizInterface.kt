package com.rsschool.quiz

interface QuizInterface {
    fun goNext(currentIndex: Int)
    fun goPrev(currentIndex: Int)
    fun getQuestionCount(): Int
    fun getQuestion(currentIndex: Int): QuizQuestion
    fun getResultText(): String
    fun getShareText(): String
    fun restartQuiz()
}
