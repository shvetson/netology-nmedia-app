package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository

class PostDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)

    private val _post : MutableLiveData<Post> by lazy {
        MutableLiveData<Post>()
    }

    fun getPost(): LiveData<Post> {
        return _post
    }

    fun loadPost(postId: String) {
        if (_post.value != null) return
        try {
            _post.value = repository.getById(postId)
        } catch (e: PostNotFoundException) {
            e.printStackTrace()
        }
    }
}