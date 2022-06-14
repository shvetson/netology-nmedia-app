package ru.netology.nmedia.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository

class PostEditViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    fun loadPost(post: Post?) {
        if (_post.value != null) return
        try {
            _post.value = post!!
        } catch (e: PostNotFoundException) {
            e.printStackTrace()
        }
    }

//    fun deletePost() {
//        val postDetails: Post = this.postDetails.value ?: return
//        repository.delete(postDetails)
//    }
}