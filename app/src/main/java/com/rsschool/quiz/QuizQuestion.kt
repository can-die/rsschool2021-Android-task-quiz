package com.rsschool.quiz

data class QuizQuestion(
    var question: String = "",
    var answer1: String = "",
    var answer2: String = "",
    var answer3: String = "",
    var answer4: String = "",
    var answer5: String = "",
    var rightAnswer: Int = -1,
    var userAnswer: Int = -1
    )
