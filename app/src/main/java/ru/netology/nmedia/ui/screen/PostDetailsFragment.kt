package ru.netology.nmedia.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.viewModel.PostViewModel
import java.text.SimpleDateFormat

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()

    private val initialPost
//        get() = PostDetailsFragmentArgs.fromBundle(requireArguments()).
        get() = args.initialPost

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        render(initialPost)
        binding.okButton.setOnClickListener {
            findNavController().navigateUp()
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
                    val directions = PostDetailsFragmentDirections.actionPostDetailsFragmentToPostEditFragment(initialPost)
                    findNavController().navigate(directions)
                    true
                }
                R.id.menu_delete -> {
                    viewModel.onDeleteClicked(initialPost)
                    val directions = PostDetailsFragmentDirections.actionPostDetailsFragmentToPostsListFragment()
                    findNavController().navigate(directions)
//                    findNavController().popBackStack(R.id.postsListFragment, false)
//                    findNavController().navigateUp()
                    true
                }
                else -> false
            }
        }
    }
}