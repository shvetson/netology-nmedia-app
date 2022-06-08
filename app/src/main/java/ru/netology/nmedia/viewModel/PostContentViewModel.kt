package ru.netology.nmedia.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository
import java.util.*

class PostContentViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _postContent = MutableLiveData<String>()
    val postContent: LiveData<String> = _postContent

    fun onSaveClicked(content: String) {
        if (content.isBlank()) return
        _postContent.value = content

        val post = Post(
            id = "",
            author = "Нетология",
            content = content,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            like = 0,
            share = 0,
            view = 0
        )
        repository.save(post)
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
}