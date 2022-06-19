package ru.netology.nmedia.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository

class PostContentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)

    fun newPost(author: String, content: String, video: String): Post? {
        if (author.isBlank() || content.isBlank()) return null

        return Post(
            id = "",
            author = author,
            content = content,
            video = video.ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" },
            like = 0,
            share = 0,
            view = 0
        )
    }
}