package ru.netology.nmedia.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostDetailsViewModel
import java.text.SimpleDateFormat

class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostDetailsViewModel by viewModels { factory() }

    companion object {
        private const val ARG_POST_ID = "ARG_POST_ID"

        fun newInstance(postId: String): PostDetailsFragment {
            val fragment = PostDetailsFragment()
            fragment.arguments = bundleOf(ARG_POST_ID to postId)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPost(requireArguments().getString(ARG_POST_ID))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        viewModel.postDetails.observe(viewLifecycleOwner) {
            with(binding) {
                postAuthorTextView.text = it.author
                postCreatedTextView.text =
                    SimpleDateFormat("dd MMM yyyy в hh:mm").format(it.created)
                postContentTextView.text = it.content
                postVideoTextView.text = it.video

                okButton.setOnClickListener {
                    navigator().goBack()
                }
            }
        }
        return binding.root
    }
}