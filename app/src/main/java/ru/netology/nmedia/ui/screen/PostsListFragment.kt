package ru.netology.nmedia.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.adapter.PostAdapter
import ru.netology.nmedia.ui.listener.ScreenActionListener
import ru.netology.nmedia.viewModel.PostViewModel

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostAdapter

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPost = bundle.getParcelable<Post>(PostContentFragment.RESULT_KEY)
                ?: return@setFragmentResultListener
            viewModel.onSaveClicked(newPost)
        }

        setFragmentResultListener(requestKey = PostEditFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostEditFragment.REQUEST_KEY) return@setFragmentResultListener
            val updatedPost = bundle.getParcelable<Post>(PostEditFragment.RESULT_KEY)
                ?: return@setFragmentResultListener
            viewModel.onSaveClicked(updatedPost)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsListBinding.inflate(inflater, container, false)
        adapter = PostAdapter(viewModel, object : ScreenActionListener {
            override fun onPostDetailsClicked(post: Post) {
                viewModel.onDetailsPost(post)
            }
            override fun onPostEditClicked(postId: String) {
                findNavController().navigate(
                    R.id.action_postDetailsFragment_to_postEditFragment,
                    bundleOf(PostEditFragment.ARG_POST_ID to postId)
                )
//                launchFragment(
//                    R.id.fragmentContainer,
//                    PostEditFragment.newInstance(postId = postId)
//                )
            }
        })
        binding.postsRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecyclerView.layoutManager = layoutManager

        viewModel.data.observe(viewLifecycleOwner, Observer<List<Post>> {
            Log.d("NMEDIA_App", "List - ${it.map { post -> post.author }}")
            adapter.submitList(it)
        })

        // Отключение "мерцания" элемента списка при обновлении одного из полей,
        // но это никак не отразится на анимации при перемещении и удалении элемента списка
        val itemAnimator = binding.postsRecyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        binding.addPostButton.setOnClickListener {
//            launchFragment(R.id.fragmentContainer, PostContentFragment.newInstance())
            findNavController().navigate(R.id.action_postsListFragment_to_postContentFragment)
        }

        viewModel.navigateToPostDetailsScreenEvent.observe(
            viewLifecycleOwner,
            Observer { initialPost ->
                findNavController().navigate(
                    R.id.action_postsListFragment_to_postDetailsFragment,
                    PostDetailsFragment.createBundle(initialPost))

//                parentFragmentManager.commit {
//                    val fragment = PostDetailsFragment.createInstance(initialPost)
//                    replace(R.id.fragmentContainer, fragment)
//                    addToBackStack(null)
//                    replace(R.id.fragmentContainer, fragment)
//                }
            })

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

    private fun launchFragment(fragmentContainer: Int, fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(fragmentContainer, fragment)
            .commit()
    }
}