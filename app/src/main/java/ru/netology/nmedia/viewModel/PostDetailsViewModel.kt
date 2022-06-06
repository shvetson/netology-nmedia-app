package ru.netology.nmedia.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository

class PostDetailsViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _postDetails = MutableLiveData<Post>()
    val postDetails: LiveData<Post> = _postDetails

    fun loadPost(postId: String?) {
        if (_postDetails.value != null) return
        try {
            _postDetails.value = repository.getId(postId!!)
        } catch (e: PostNotFoundException) {
            e.printStackTrace()
        }
    }

    fun deletePost() {
        val postDetails: Post = this.postDetails.value ?: return
        repository.delete(postDetails)
    }
}