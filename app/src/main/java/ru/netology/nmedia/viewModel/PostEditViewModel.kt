package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository
import java.util.*

class PostEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    private val post: (String) -> Post = repository::getById

    private val _post: MutableLiveData<Post> by lazy {
        MutableLiveData<Post>()
    }
    val data: LiveData<Post> = _post

    fun loadPost(postId: String) {
        if (_post.value != null) return
        try {
            _post.value = post(postId)
        } catch (e: PostNotFoundException) {
            e.printStackTrace()
        }
    }

    fun updatePost(author: String, content: String, video: String): Post? {
        if (author.isBlank() || content.isBlank()) return null

        return (_post.value as Post).copy(
            author = author,
            content = content,
            video = video.ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" },
            created = Date().time
        )
    }
}