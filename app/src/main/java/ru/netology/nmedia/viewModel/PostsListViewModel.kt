package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository
import ru.netology.nmedia.ui.listener.PostActionListener

class PostsListViewModel(
    application: Application
) : AndroidViewModel(application), PostActionListener {

    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data get() = repository.data

//    private val posts: MutableLiveData<List<Post>> by lazy {
//        MutableLiveData<List<Post>>().also {
//                it.value = loadPosts()
//        }
//    }
//
//    fun getPosts(): LiveData<List<Post>> {
//        Log.d("App_Tag", posts.value.toString())
//        return posts
//    }
//
//    private fun loadPosts() = repository.getAll()

//    private val posts: () -> List<Post> = repository::getAll
//    private val _posts = MutableLiveData<List<Post>>()
//    val data: LiveData<List<Post>> = _posts
//
//    fun loadPosts() {
//        if (_posts.value != null) return
//        try {
//            _posts.value = posts()
//        } catch (e: PostNotFoundException) {
//            e.printStackTrace()
//        }
//    }

    //    private val post: (String) -> Post = repository::getById

    val selected = MutableLiveData<Post>()

    fun select(postId: String) {
        selected.value = repository.getById(postId)
    }

    private val _mPost = MutableLiveData<Post>()
    val mPost: LiveData<Post> = _mPost

    fun loadPost(postId: String) {
        if (_mPost.value != null) return
        try {
            _mPost.value = repository.getById(postId)
        } catch (e: PostNotFoundException) {
            e.printStackTrace()
        }
    }

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()

    // region PostActionListener
    override fun onLikeClicked(post: Post) {
        repository.like(post)
    }

    override fun onShareClicked(post: Post) {
        onShareContent.value = post.content
        repository.share(post)
    }

    override fun onViewClicked(post: Post) {
        repository.view(post)
    }

    override fun onSaveClicked(post: Post) {
        repository.save(post)
        selected.value = repository.getById(post.id)
    }

    override fun onDeleteClicked(post: Post) {
        repository.delete(post)
    }

    override fun onMoveClicked(post: Post, moveBy: Int) {
        repository.move(post, moveBy)
    }

    override fun onYouTubeClicked(post: Post) {
        onViewYoutubeLink.value = post.video!!
    }

    // endregion PostActionListener
}