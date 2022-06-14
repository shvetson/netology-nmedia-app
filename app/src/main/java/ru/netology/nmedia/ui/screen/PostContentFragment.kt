package ru.netology.nmedia.ui.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostContentViewModel

class PostContentFragment : Fragment(), HasCustomTitle {
    private lateinit var binding: FragmentPostContentBinding
    private val viewModel: PostContentViewModel by viewModels { factory() }

    override fun getTitleRes(): Int = R.string.title_new_post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostContentBinding.inflate(inflater, container, false)
        requestFocusAndShowSoftInput(binding.postAuthorEditText)

        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            navigator().goBack()
        }
        return binding.root
    }

    private fun onOkButtonClicked() {
        viewModel.onSaveClicked(
            author = binding.postAuthorEditText.text.toString(),
            content = binding.postContentEditText.text.toString(),
            video = binding.postVideoEditText.text.toString()
        )
    }

    companion object {
        fun newInstance(): PostContentFragment {
            return PostContentFragment()
        }
    }

    private fun requestFocusAndShowSoftInput(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        view.postDelayed(Runnable {
            view.requestFocus()
            imm!!.showSoftInput(view, 0)
        }, 100)
    }
}