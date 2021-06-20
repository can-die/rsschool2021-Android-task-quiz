package com.rsschool.quiz

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import com.rsschool.quiz.databinding.FragmentQuizBinding

private const val ARG_INDEX = "index"

class QuizFragment : Fragment() {

    private var index = 0
    private var themeId = 0
    private var binding: FragmentQuizBinding? = null
    private var quizQuestion: QuizQuestion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt(ARG_INDEX)
        }

        when (index) {
            0 -> themeId = R.style.Theme_Quiz_First
            1 -> themeId = R.style.Theme_Quiz_Second
            2 -> themeId = R.style.Theme_Quiz_Third
            3 -> themeId = R.style.Theme_Quiz_Fourth
            4 -> themeId = R.style.Theme_Quiz_Fifth
            else -> themeId = R.style.Theme_Quiz_First // default theme is first
        }
    }

    override fun onResume() {
        super.onResume()

        val value = TypedValue ()

        requireActivity().setTheme(themeId)
        requireActivity().getTheme().resolveAttribute(android.R.attr.statusBarColor, value, true)
        requireActivity().window.statusBarColor = value.data

        binding?.apply {
            if (quizQuestion?.userAnswer != -1) {
                radioGroup.forEachIndexed { index, view ->
                    if (index == quizQuestion?.userAnswer) {
                        radioGroup.check(view.id)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is QuizInterface)
            quizQuestion = (activity as QuizInterface).getQuestion(index)

        binding = FragmentQuizBinding.bind(view)
        binding?.apply {
            toolbar.title = getString(R.string.fragment_toolbar_title) + " ${index + 1}"
            toolbar.setNavigationOnClickListener {
                (activity as QuizInterface).goPrev(index)
            }

            prevButton.setOnClickListener { (activity as QuizInterface).goPrev(index) }

            if (index < (activity as QuizInterface).getQuestionCount() - 1) {
                nextButton.text = getString(R.string.fragment_next_button)
            } else {
                nextButton.text = getString(R.string.fragment_submit_button)
            }
            nextButton.setOnClickListener {
                if (activity is QuizInterface)
                    (activity as QuizInterface).goNext(index)
            }

            question.text    = quizQuestion?.question
            optionOne.text   = quizQuestion?.answer1
            optionTwo.text   = quizQuestion?.answer2
            optionThree.text = quizQuestion?.answer3
            optionFour.text  = quizQuestion?.answer4
            optionFive.text  = quizQuestion?.answer5

            radioGroup.setOnCheckedChangeListener { _, _ ->
                radioGroup.forEachIndexed { index, view ->
                    if ((view as RadioButton).isChecked) {
                        quizQuestion?.userAnswer = index
                    }
                }
                enableButton()
            }

            enableButton()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.radioGroup?.clearCheck()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTheme(themeId)
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    private fun enableButton() {
        binding?.apply {
            prevButton.visibility = if(index == 0) View.GONE else View.VISIBLE
            nextButton.isEnabled = quizQuestion?.userAnswer != -1
            if (index == 0)
                toolbar.setNavigationIcon(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(pos: Int) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, pos)
                }
            }
    }
}