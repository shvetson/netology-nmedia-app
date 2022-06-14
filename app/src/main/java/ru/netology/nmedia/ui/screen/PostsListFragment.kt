package ru.netology.nmedia.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.adapter.PostAdapter
import ru.netology.nmedia.ui.listener.PostActionListener
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostsListViewModel

class PostsListFragment : Fragment() {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostAdapter

    private val viewModel: PostsListViewModel by viewModels { factory() }

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

            override fun onPostDetailsClicked(post: Post) {
                navigator().showDetails(post)
            }

            override fun onUpdateClicked(post: Post) {
                viewModel.onUpdateClicked(post)
            }

            override fun onYouTubeClicked(post: Post) {
                viewModel.onYouTubeClicked(post)
            }
        })

        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.posts = it
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecyclerView.layoutManager = layoutManager
        binding.postsRecyclerView.adapter = adapter

        // Отключение "мерцания" элемента списка при обновлении одного из полей,
        // но это никак не отразится на анимации при перемещении и удалении элемента списка
        val itemAnimator = binding.postsRecyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
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

        binding.addPostButton.setOnClickListener {
            navigator().showNewPost()
        }
        return binding.root
    }


//        setFragmentResultListener(
//            requestKey = PostContentFragment.REQUEST_KEY
//        ) { requestKey, bundle ->
//            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
//            val newPostContent = bundle.getString(PostContentFragment.RESULT_KEY)
//                ?: return@setFragmentResultListener
//            viewModel.onSaveClicked(newPostContent)
//        }

}