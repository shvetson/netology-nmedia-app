package ru.netology.nmedia.viewModel

import SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository
import ru.netology.nmedia.model.repositoty.PostsListener
import java.util.*

class PostsListViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val listener: PostsListener = {
        _posts.value = it
    }

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()

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

    fun onViewClicked(post: Post) {
        repository.view(post)
    }

    fun onMoveClicked(post: Post, moveBy: Int) {
        repository.move(post, moveBy)
    }

    fun onRemoveClicked(post: Post) = repository.delete(post)

    fun onUpdateDateClicked(post: Post) {
        repository.updateDate(post)
    }

    fun onPostDetailsClicked(post: Post) {
    }

    fun onYouTubeClicked(post: Post) {
        onViewYoutubeLink.value = post.video!!
    }
}