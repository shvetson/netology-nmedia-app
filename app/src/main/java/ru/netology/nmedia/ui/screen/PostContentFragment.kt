package ru.netology.nmedia.ui.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment(R.layout.fragment_post_content) {

    private lateinit var binding: FragmentPostContentBinding
    private val viewModel: PostViewModel by viewModels()

    companion object {
        const val REQUEST_KEY = "NEW_POST"
        const val RESULT_KEY = "RESULT_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostContentBinding.inflate(inflater, container, false)
        requestFocusAndShowSoftInput(binding.postAuthorEditText)

        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun onOkButtonClicked() {
        val newPost = viewModel.newPost(
            author = binding.postAuthorEditText.text.toString(),
            content = binding.postContentEditText.text.toString(),
            video = binding.postVideoEditText.text.toString()
                .ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" }
        )!!

        setFragmentResult(
            requestKey = REQUEST_KEY,
            bundleOf(RESULT_KEY to newPost)
        )
    }

    private fun requestFocusAndShowSoftInput(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        view.postDelayed(Runnable {
            view.requestFocus()
            imm!!.showSoftInput(view, 0)
        }, 100)
    }
}