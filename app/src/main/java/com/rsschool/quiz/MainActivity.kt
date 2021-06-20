package com.rsschool.quiz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : FragmentActivity(), QuizInterface {

    private lateinit var binding: ActivityMainBinding
    private var idTheme = 0

    private val model: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        binding.apply {
            viewPager.adapter = adapter
            viewPager.isUserInputEnabled = false

            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }
    }

    inner class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = getQuestionCount() + 1

        override fun createFragment(position: Int): Fragment {
            if (position < getQuestions().size) {
                return QuizFragment.newInstance(position)
            } else {
                return SubmitFragment.newInstance ()
            }
        }
    }

    private fun getQuestions(): List<QuizQuestion> {
        return model.getQuestions().value ?: listOf()
    }

    override fun goNext(currentIndex: Int) {
        binding.viewPager.setCurrentItem(currentIndex + 1)
    }

    override fun goPrev(currentIndex: Int) {
        binding.viewPager.setCurrentItem(currentIndex - 1)
    }

    override fun getQuestionCount(): Int = getQuestions().size

    override fun getQuestion(currentIndex: Int): QuizQuestion {
        return getQuestions()[currentIndex]
    }

    override fun restartQuiz() {
        for (question in getQuestions()) {
            question.userAnswer = -1
        }
        binding.viewPager.setCurrentItem(0)
    }

    override fun getResultText(): String {
        var rightCount = 0
        val questions = getQuestions()
        for (question in questions) {
            if (question.userAnswer == question.rightAnswer)
                rightCount = rightCount + 1
        }
        return "${getString(R.string.score_text)} $rightCount of ${questions.size}"
    }

    override fun getShareText(): String {
        var text = getResultText() + "\n\n"
        val questions = getQuestions()
        for ((index, question) in questions.withIndex()) {
            text = text + "${index + 1}) ${question.question}\n${getString(R.string.your_answer)} "
            when (question.userAnswer) {
                0 -> text = text + question.answer1 + "\n\n`"
                1 -> text = text + question.answer2 + "\n\n`"
                2 -> text = text + question.answer3 + "\n\n`"
                3 -> text = text + question.answer4 + "\n\n`"
                4 -> text = text + question.answer5 + "\n\n`"
            }
        }
        return text
    }
}