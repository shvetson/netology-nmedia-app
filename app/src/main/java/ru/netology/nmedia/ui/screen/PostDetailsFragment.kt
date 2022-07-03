package ru.netology.nmedia.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.contract
import ru.netology.nmedia.viewModel.PostViewModel
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()

    private val initialPost
        //        get() = PostDetailsFragmentArgs.fromBundle(requireArguments()).
        get() = args.initialPost

    private lateinit var currentPost: Post
    private lateinit var safePost: Post
    private var buttonOkFlag: Boolean = false

    companion object {
        private const val ARG_POST = "ARG_POST"
        private const val ARG_BUTTON = "ARG_BUTTON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.loadPost(initialPost)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        binding.postContentTextView.movementMethod = ScrollingMovementMethod()

        viewModel.post.observe(viewLifecycleOwner, Observer<Post> {
            render(it)
            currentPost = it
        })
        viewModel.onShareContent.observe(viewLifecycleOwner) { content ->
            contract().launchShareIntent(content)
        }

        binding.optionsButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
        binding.postShareButton.setOnClickListener {
            viewModel.share(currentPost)
            viewModel.onShareContent.value = currentPost.content
            btnOkIsVisible()
        }
        binding.postLikeButton.setOnClickListener {
            viewModel.like(currentPost)
            btnOkIsVisible()
        }
        binding.okButton.setOnClickListener {
            setFragmentResult(
                requestKey = PostEditFragment.REQUEST_KEY,
                bundleOf(PostEditFragment.RESULT_KEY to currentPost)
            )
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putParcelable(ARG_POST, currentPost)
            putBoolean(ARG_BUTTON, binding.okButton.isVisible)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            safePost = savedInstanceState.getParcelable<Post>(ARG_POST)!!
            currentPost = safePost

            buttonOkFlag = savedInstanceState.getBoolean(ARG_BUTTON)
            binding.okButton.visibility = if (buttonOkFlag) View.VISIBLE else View.INVISIBLE
        }
        super.onViewStateRestored(savedInstanceState)
    }

    private fun btnOkIsVisible() {
        if (binding.okButton.isInvisible) binding.okButton.visibility = View.VISIBLE
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
                    contract().launchEditPostFromDetailsPost(initialPost)
                    true
                }
                R.id.menu_delete -> {
                    viewModel.onDeleteClicked(initialPost)
                    contract().launchListPostsFromDetailsPost()
                    true
                }
                else -> false
            }
        }
    }
}