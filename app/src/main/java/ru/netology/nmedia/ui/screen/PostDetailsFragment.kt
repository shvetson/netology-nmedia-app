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
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostDetailsViewModel
import java.text.SimpleDateFormat

class PostDetailsFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostDetailsViewModel by viewModels { factory() }

    override fun getTitleRes(): Int = R.string.details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPost(requireArguments().getParcelable<Post>(ARG_POST))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        viewModel.postDetails.observe(viewLifecycleOwner) {
            with(binding) {
                postAuthorTextView.text = it.author
                postCreatedTextView.text =
                    SimpleDateFormat("dd MMM yyyy в hh:mm").format(it.created)
                postContentTextView.text = it.content
                postVideoTextView.text = it.video
                postLikeButton.isChecked = it.likeFlag
                postLikeButton.text = it.like.toString()
                postShareButton.text = it.share.toString()
                postViewButton.text = it.view.toString()

                okButton.setOnClickListener {
                    navigator().goBack()
                }
            }
        }
        return binding.root
    }

    companion object {
        private const val ARG_POST = "ARG_POST"

        fun newInstance(post: Post): PostDetailsFragment {
            val fragment = PostDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_POST, post)
            fragment.arguments = args
//            fragment.arguments = bundleOf(ARG_POST to post)
            return fragment
        }
    }
}