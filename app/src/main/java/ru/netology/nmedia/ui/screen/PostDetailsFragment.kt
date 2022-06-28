package ru.netology.nmedia.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.viewModel.PostViewModel
import java.text.SimpleDateFormat

class PostDetailsFragment(
    private val initialPost: Post
) : Fragment(R.layout.fragment_post_details), HasCustomTitle {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostViewModel by viewModels()
    private var currentPost: Post? = null

//    companion object {
//        private const val ARG_POST_ID = "ARG_POST_ID"
//
//        fun newInstance(postId: String): PostDetailsFragment {
//            val fragment = PostDetailsFragment()
//            val args = Bundle()
//            args.putString(ARG_POST_ID, postId)
//            fragment.arguments = args
//            fragment.arguments = bundleOf(ARG_POST to post)
//            return fragment
//        }
//    }

    override fun getTitleRes(): Int = R.string.details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.loadPost(requireArguments().getString(ARG_POST_ID)!!)

        setFragmentResultListener(requestKey = PostEditFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostEditFragment.REQUEST_KEY) return@setFragmentResultListener
            val updatedPost = bundle.getParcelable<Post>(PostEditFragment.RESULT_KEY)
                ?: return@setFragmentResultListener
            viewModel.onSaveClicked(updatedPost)
            Log.d("NMEDIA_App", "updated - $updatedPost")
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        render(initialPost)
        binding.okButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.optionsButton.setOnClickListener { view ->
            showPopupMenu(view)
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

    private fun showPopupMenu(view: View) {
        val context: Context = view.context
        val popupMenu by lazy {
            PopupMenu(context, view).apply {
                inflate(R.menu.options_post)
                this.menu.findItem(R.id.menu_move_up).isVisible = false
                this.menu.findItem(R.id.menu_move_down).isVisible = false
            }
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    initialPost.let { PostEditFragment.newInstance(it.id) }.let {
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(
                                R.id.fragmentContainer,
                                it
                            )
                            .commit()
                    }
                    true
                }
                R.id.menu_delete -> {
                    initialPost.let { viewModel.onDeleteClicked(it) }
                    true
                }
                else -> false
            }
        }
    }
}