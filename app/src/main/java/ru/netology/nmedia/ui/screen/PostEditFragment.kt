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
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostEditBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.viewModel.PostViewModel

class PostEditFragment : Fragment(R.layout.fragment_post_edit) {
    private lateinit var binding: FragmentPostEditBinding
    private val viewModel: PostViewModel by viewModels()
    private val args: PostEditFragmentArgs by navArgs()

    private val initialPost
//        get() = PostDetailsFragmentArgs.fromBundle(requireArguments()).initialPost
        get() = args.initialPost

    private var author: String? = null
    private var content: String? = null
    private var video: String? = null

    companion object {
        const val REQUEST_KEY = "UPDATE_POST"
        const val RESULT_KEY = "UPDATE_POST"

        private const val ARG_AUTHOR = "KEY_AUTHOR"
        private const val ARG_CONTENT = "KEY_CONTENT"
        private const val ARG_VIDEO = "KEY_VIDEO"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostEditBinding.inflate(inflater, container, false)

        render(initialPost)
        requestFocusAndShowSoftInput(binding.postContentEditText)

        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun render(post: Post) {
        with(binding) {
            postAuthorEditText.setText(author ?: post.author)
            postContentEditText.setText(content ?: post.content)
            postVideoEditText.setText(video ?: post.video)
        }
    }

    private fun onOkButtonClicked() {
        val updatedPost = viewModel.updatePost(
            id = initialPost.id,
            author = binding.postAuthorEditText.text.toString(),
            content = binding.postContentEditText.text.toString(),
            video = binding.postVideoEditText.text.toString()
                .ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" }
        )!!

        // Возврат результата
        // Использовать для соседних окон
//        findNavController().previousBackStackEntry?.savedStateHandle?.set(RESULT_KEY, updatedPost)

        setFragmentResult(
            requestKey = REQUEST_KEY,
            bundleOf(RESULT_KEY to updatedPost)
        )
        findNavController().navigateUp()
    }

    private fun requestFocusAndShowSoftInput(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        view.postDelayed(Runnable {
            view.requestFocus()
            imm!!.showSoftInput(view, 0)
        }, 100)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putString(ARG_AUTHOR, binding.postAuthorEditText.text.toString())
            putString(ARG_CONTENT, binding.postContentEditText.text.toString())
            putString(ARG_VIDEO, binding.postVideoEditText.text.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            author = savedInstanceState.getString(ARG_AUTHOR)
            content = savedInstanceState.getString(ARG_CONTENT)
            video = savedInstanceState.getString(ARG_VIDEO)
        }
        super.onViewStateRestored(savedInstanceState)
    }
}