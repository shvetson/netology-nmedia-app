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

//    private var _posts = MutableLiveData(repository.getAll())
//    val data: LiveData<List<Post>> = _posts

//    private val listener: PostsListener = {
//        _posts.value = it
//    }

    val data by repository::data

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()

//    fun loadPosts() {
//        _posts.value = repository.getAll()
//    }





    fun onLikeClicked(post: Post) {
        repository.like(post)
    }

    fun onShareClicked(post: Post) {
        onShareContent.value = post.content
        repository.share(post)
    }

    fun onSaveClicked(post: Post) {
        repository.save(post)
    }

    fun onViewClicked(post: Post) {
        repository.view(post)
    }

    fun onMoveClicked(post: Post, moveBy: Int) {
        repository.move(post, moveBy)
    }

    fun onDeleteClicked(post: Post) {
        repository.delete(post)
    }

    fun onYouTubeClicked(post: Post) {
        onViewYoutubeLink.value = post.video!!
    }
}