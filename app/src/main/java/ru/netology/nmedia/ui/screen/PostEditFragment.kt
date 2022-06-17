package ru.netology.nmedia.ui.screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_edit.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostEditBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostEditViewModel

class PostEditFragment : Fragment(), HasCustomTitle {
    private lateinit var binding: FragmentPostEditBinding
    private val viewModel: PostEditViewModel by viewModels {factory()}

//    Не работает тк в конструктор viewModel передается аргумент - репозиторий, без него можно
//    private val viewModel by viewModels<PostEditViewModel>()

    private var author: String? = null
    private var content: String? = null
    private var video: String? = null
    private var flagBundleSave = false

    override fun getTitleRes(): Int = R.string.title_edit_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireArguments().getParcelable<Post>(ARG_POST)?.let { postEditViewModel.loadPost(it) }
        viewModel.loadPost(requireArguments().getString(ARG_POST_ID)!!)

//        init | restore state
        if (savedInstanceState != null) {
            author = savedInstanceState.getString(KEY_AUTHOR)
            content = savedInstanceState.getString(KEY_CONTENT)
            video = savedInstanceState.getString(KEY_VIDEO)
            flagBundleSave = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostEditBinding.inflate(inflater, container, false)

        viewModel.data.observe(viewLifecycleOwner, Observer<Post> { post -> render(post) })
        requestFocusAndShowSoftInput(binding.postContentEditText)

        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            navigator().goBack()
        }
        return binding.root
    }

    private fun render(post: Post) {
        with(binding) {

            if (flagBundleSave) {
                postAuthorEditText.setText(author)
                postContentEditText.setText(content)
                postVideoEditText.setText(video)
            } else {
                postAuthorEditText.setText(post.author)
                postContentEditText.setText(post.content)
                postVideoEditText.setText(post.video)
            }
        }
    }

    private fun onOkButtonClicked() {
        viewModel.savePost(
            author = binding.postAuthorEditText.text.toString(),
            content = binding.postContentEditText.text.toString(),
            video = binding.postVideoEditText.text.toString()
        )
    }

    companion object {
        private const val ARG_POST_ID = "ARG_POST_ID"
        private const val KEY_AUTHOR = "KEY_AUTHOR"
        private const val KEY_CONTENT = "KEY_CONTENT"
        private const val KEY_VIDEO = "KEY_VIDEO"

        fun newInstance(postId: String): PostEditFragment {
            val fragment = PostEditFragment()
            val args = Bundle()
            args.putString(ARG_POST_ID, postId)
            fragment.arguments = args
            return fragment
        }
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
        outState.putString(KEY_AUTHOR, binding.postAuthorEditText.text.toString())
        outState.putString(KEY_CONTENT, binding.postContentEditText.text.toString())
        outState.putString(KEY_VIDEO, binding.postVideoEditText.text.toString())
    }
}