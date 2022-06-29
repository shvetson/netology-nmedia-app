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
import androidx.lifecycle.Observer
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostEditBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.viewModel.PostEditViewModel


class PostEditFragment : Fragment(R.layout.fragment_post_edit), HasCustomTitle {
    private lateinit var binding: FragmentPostEditBinding
    private val viewModel: PostEditViewModel by viewModels()

    private val postId: String by lazy {
        requireArguments().getString(ARG_POST_ID).toString()
    }

    private var author: String? = null
    private var content: String? = null
    private var video: String? = null

    companion object {
        const val ARG_POST_ID = "ARG_POST_ID"

        const val REQUEST_KEY = "REQUEST_KEY"
        const val RESULT_KEY = "RESULT_KEY"

        private const val ARG_AUTHOR = "KEY_AUTHOR"
        private const val ARG_CONTENT = "KEY_CONTENT"
        private const val ARG_VIDEO = "KEY_VIDEO"

//        fun newInstance(postId: String): PostEditFragment {
//            val fragment = PostEditFragment()
//            val args = Bundle()
//            args.putString(ARG_POST_ID, postId)
//            fragment.arguments = args
//            return fragment
//        }
    }

    override fun getTitleRes(): Int = R.string.title_edit_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPost(postId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostEditBinding.inflate(inflater, container, false)
        viewModel.data.observe(viewLifecycleOwner, Observer<Post> {
            render(it)
        })
        requestFocusAndShowSoftInput(binding.postContentEditText)

        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            requireActivity().supportFragmentManager.popBackStack()
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
            author = binding.postAuthorEditText.text.toString(),
            content = binding.postContentEditText.text.toString(),
            video = binding.postVideoEditText.text.toString()
                .ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" }
        )

        setFragmentResult(
            requestKey = REQUEST_KEY,
            bundleOf(RESULT_KEY to updatedPost)
        )
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