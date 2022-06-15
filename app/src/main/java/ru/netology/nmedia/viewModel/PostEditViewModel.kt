package ru.netology.nmedia.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository
import java.util.*

class PostEditViewModel(
    private val repository: PostRepository
) : ViewModel() {

//    val data by repository::data

    private val _post = MutableLiveData<Post>()
    val data: LiveData<Post> = _post
//    val data = repository.get()


    fun loadPost(post: Post) {
//        if (_post.value != null) return
//        try {
            _post.value = post
//        } catch (e: PostNotFoundException) {
//            e.printStackTrace()
//        }
    }

    fun savePost(author: String, content: String, video: String) {
        if (author.isBlank() || content.isBlank()) return

        val postUpdated = (data.value as Post).copy(
            author = author,
            content = content,
            video = video.ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" },
            created = Date().time
        )
        repository.save(postUpdated )
    }

//    fun deletePost() {
//        val postDetails: Post = this.postDetails.value ?: return
//        repository.delete(postDetails)
//    }
}