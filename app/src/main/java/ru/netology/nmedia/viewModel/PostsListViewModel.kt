package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.impl.PostsListener
import ru.netology.nmedia.model.repositoty.PostRepository

class PostsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)

    private val _posts = MutableLiveData<List<Post>>()
    val data: LiveData<List<Post>> = _posts

    private val listener: PostsListener = {
        _posts.value = it
    }

//    val data by repository::data

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()

//    fun loadPosts() {
//        _posts.value = repository.getAll()
//    }

    init {
        loadPosts()
    }

    private fun loadPosts() {
        repository.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener(listener)
    }

    fun onLikeClicked(post: Post) {
        repository.like(post)
    }

    fun onShareClicked(post: Post) {
        onShareContent.value = post.content
        repository.share(post)
    }

//    fun onSaveClicked(content: String) {
//        if (content.isBlank()) return
//
//        val post = currentPost.value?.copy(
//            content = content,
//            created = Date().time,
//        ) ?: Post(
//            id = "",
//            author = "Нетология",
//            content = content,
//            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
//            like = 0,
//            share = 0,
//            view = 0
//        )
//        repository.save(post)
//        currentPost.value = null
//    }

    fun onSaveClicked(post: Post) {
        repository.save(post)
    }

    fun onViewClicked(post: Post) {
        repository.view(post)
    }

    fun onMoveClicked(post: Post, moveBy: Int) {
        repository.move(post, moveBy)
    }

    fun onRemoveClicked(post: Post) = repository.delete(post)

    fun onUpdateClicked(post: Post) {
        //repository.save(post)
    }

    fun onPostDetailsClicked(post: Post) {
    }

    fun onYouTubeClicked(post: Post) {
        onViewYoutubeLink.value = post.video!!
    }
}