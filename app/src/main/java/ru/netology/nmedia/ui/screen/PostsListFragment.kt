package ru.netology.nmedia.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.adapter.PostAdapter
import ru.netology.nmedia.ui.contract.contract
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

        // Использовать для соседних окон (вперед - назад)
//        val liveData =
//            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Post>(
//                PostEditFragment.RESULT_KEY
//            )
//        liveData?.observe(this, Observer {updatedPost->
//            if (updatedPost != null) {
//            viewModel.onSaveClicked(updatedPost)
//            liveData.value = null
//            }
//        })
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

            override fun onPostEditClicked(initialPost: Post) {
                contract().launchEditPostFromListPosts(initialPost)
            }
        })
        binding.postsRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecyclerView.layoutManager = layoutManager

        viewModel.data.observe(viewLifecycleOwner, Observer<List<Post>> {
            adapter.submitList(it)
        })

        // Отключение "мерцания" элемента списка при обновлении одного из полей,
        // но это никак не отразится на анимации при перемещении и удалении элемента списка
        val itemAnimator = binding.postsRecyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        // Запуск фрагмента с формой для добавления поста
        binding.addPostButton.setOnClickListener {
            contract().launchPostContentFromListPosts()
        }

        // Наблюдатель за изменением в singleEvent и запуск фрагмента по просмотру данных поста
        viewModel.navigateToPostDetailsScreenEvent.observe(
            viewLifecycleOwner, Observer { initialPost ->
                contract().launchPostDetailsFromListPosts(initialPost)
            })

        viewModel.onShareContent.observe(viewLifecycleOwner) { content ->
            contract().launchShareIntent(content)
        }

        viewModel.onViewYoutubeLink.observe(viewLifecycleOwner) { video ->
            contract().launchViewVideoOnYouTube(video)
        }
        return binding.root
    }
}