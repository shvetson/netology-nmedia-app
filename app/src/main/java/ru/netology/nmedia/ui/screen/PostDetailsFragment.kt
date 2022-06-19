package ru.netology.nmedia.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.viewModel.PostDetailsViewModel
import java.text.SimpleDateFormat

class PostDetailsFragment : Fragment(R.layout.fragment_post_details), HasCustomTitle {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostDetailsViewModel by viewModels()

    companion object {
        private const val ARG_POST_ID = "ARG_POST_ID"

        fun newInstance(postId: String): PostDetailsFragment {
            val fragment = PostDetailsFragment()
            val args = Bundle()
            args.putString(ARG_POST_ID, postId)
            fragment.arguments = args
//            fragment.arguments = bundleOf(ARG_POST to post)
            return fragment
        }
    }

    override fun getTitleRes(): Int = R.string.details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPost(requireArguments().getString(ARG_POST_ID)!!)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        viewModel.getPost().observe(viewLifecycleOwner) { post -> render(post) }

        binding.okButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

    private fun render(post: Post) {
        with(binding) {
            postAuthorTextView.text = post.author
            postCreatedTextView.text =
                SimpleDateFormat("dd MMM yyyy в hh:mm").format(post.created)
            postContentTextView.text = post.content
            postVideoTextView.text = post.video
            postLikeButton.isChecked = post.likeFlag
            postLikeButton.text = post.like.toString()
            postShareButton.text = post.share.toString()
            postViewButton.text = post.view.toString()
        }
    }
}