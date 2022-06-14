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
import ru.netology.nmedia.databinding.FragmentPostEditBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostContentViewModel
import ru.netology.nmedia.viewModel.PostEditViewModel
import java.text.SimpleDateFormat

class PostEditFragment : Fragment(), HasCustomTitle {
    private lateinit var binding: FragmentPostEditBinding
    private val viewModel: PostEditViewModel by viewModels { factory() }

    override fun getTitleRes(): Int = R.string.title_edit_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPost(requireArguments().getParcelable<Post>(ARG_POST))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostEditBinding.inflate(inflater, container, false)

        viewModel.post.observe(viewLifecycleOwner) {
            with(binding) {
                postAuthorEditText.setText(it.author)
                postContentEditText.setText(it.content)

                requestFocusAndShowSoftInput(binding.postAuthorEditText)

                binding.okButton.setOnClickListener {

                    navigator().goBack()
                }
            }
        }
        return binding.root
    }

//    private fun onOkButtonClicked() {
//        viewModel.onSaveClicked(
//            author = binding.postAuthorEditText.text.toString(),
//            content = binding.postContentEditText.text.toString(),
//            video = binding.postVideoEditText.text.toString()
//        )
//    }

    companion object {
        private const val ARG_POST = "ARG_POST"

        fun newInstance(post: Post): PostEditFragment {
            val fragment = PostEditFragment()
            val args = Bundle()
            args.putParcelable(ARG_POST, post)
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
}