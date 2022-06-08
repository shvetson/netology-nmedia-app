package ru.netology.nmedia.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.util.navigator

class PostContentFragment : Fragment() {
    lateinit var binding: FragmentPostContentBinding
    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post =
            savedInstanceState?.getParcelable<Post>(KEY_POST) ?: arguments?.getParcelable(ARG_POST)
                    ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostContentBinding.inflate(inflater, container, false)

        binding.okButton.setOnClickListener {
            navigator().goBack()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_POST, post)
    }

//    private fun onOkButtonClicked(binding: FragmentPostContentBinding) {
//        val text = binding.editEditText.text
//
//        if (!text.isNullOrBlank()) {
//
//        }
//    }

    private fun get(): Post? {
        return requireArguments().getParcelable(KEY_POST)
    }

    companion object {
        const val ARG_POST = "ARG_POST"
        const val KEY_POST = "KEY_POST"

        fun newInstance(post: Post?): PostContentFragment {
            val args = Bundle()
            args.putParcelable(ARG_POST, post)
            val fragment = PostContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}