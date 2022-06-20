package ru.netology.nmedia.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.adapter.PostAdapter
import ru.netology.nmedia.ui.listener.PostActionListener
import ru.netology.nmedia.viewModel.PostsListViewModel

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostAdapter

    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.loadPosts()

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPost = bundle.getParcelable<Post>(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveClicked(newPost)
        }

        setFragmentResultListener(requestKey = PostEditFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostEditFragment.REQUEST_KEY) return@setFragmentResultListener
            val updatedPost = bundle.getParcelable<Post>(PostEditFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveClicked(updatedPost)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsListBinding.inflate(inflater, container, false)
        adapter = PostAdapter(object : PostActionListener {
            override fun onLikeClicked(post: Post) {
                viewModel.onLikeClicked(post)
            }

            override fun onShareClicked(post: Post) {
                viewModel.onShareClicked(post)
            }

            override fun onViewClicked(post: Post) {
                viewModel.onViewClicked(post)
            }

            override fun onRemoveClicked(post: Post) {
                viewModel.onRemoveClicked(post)
            }

            override fun onMoveClicked(post: Post, moveBy: Int) {
                viewModel.onMoveClicked(post, moveBy)
            }

            override fun onPostDetailsClicked(postId: String) {
                createFragmentByReplace(R.id.fragmentContainer, PostDetailsFragment.newInstance(postId = postId))
            }

            override fun onUpdateClicked(postId: String) {
                createFragmentByReplace(R.id.fragmentContainer, PostEditFragment.newInstance(postId = postId))
            }

            override fun onYouTubeClicked(post: Post) {
                viewModel.onYouTubeClicked(post)
            }
        })

        binding.postsRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecyclerView.layoutManager = layoutManager

        viewModel.data.observe(viewLifecycleOwner) { it ->
//            adapter.posts = it
            adapter.submitList(it)
        }

        // Отключение "мерцания" элемента списка при обновлении одного из полей,
        // но это никак не отразится на анимации при перемещении и удалении элемента списка
        val itemAnimator = binding.postsRecyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        binding.addPostButton.setOnClickListener {
            createFragmentByReplace(R.id.fragmentContainer, PostContentFragment.newInstance())
        }

        viewModel.onShareContent.observe(viewLifecycleOwner) { content ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, content)
            }

            val shareIntent =
                Intent.createChooser(
                    intent, getString(R.string.chooser_share_post)
                )
            startActivity(shareIntent)
        }

        viewModel.onViewYoutubeLink.observe(viewLifecycleOwner) { video ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(video)
            }
            startActivity(intent)
        }
        return binding.root
    }

    private fun createFragmentByReplace(fragmentContainer: Int, fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(fragmentContainer, fragment)
            .commit()
    }
}