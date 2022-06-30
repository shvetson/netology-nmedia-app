package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository
import ru.netology.nmedia.ui.listener.PostActionListener
import java.util.*

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostActionListener {

    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data get() = repository.data

    val getPost: (String) -> Post = repository::getById

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()
    val navigateToPostDetailsScreenEvent = SingleLiveEvent<Post>()

//    fun onAddPost() {
//        navigateToPostDetailsScreenEvent.call()
//    }

    fun onDetailsPost(post: Post) {
        repository.view(post)
        navigateToPostDetailsScreenEvent.value = getPost(post.id)
    }

    fun updatePost(id: String, author: String, content: String, video: String): Post? {
        if (author.isBlank() || content.isBlank()) return null
        val post = getPost(id)

        return post.copy(
            author = author,
            content = content,
            video = video.ifBlank { "https://www.youtube.com/watch?v=WhWc3b3KhnY" },
            created = Date().time
        )
    }

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

    // region PostActionListener

    override fun onLikeClicked(post: Post) {
        repository.like(post)
    }

    override fun onShareClicked(post: Post) {
        onShareContent.value = post.content
        repository.share(post)
    }

//    override fun onViewClicked(post: Post) {
//        repository.view(post)
//    }

    override fun onSaveClicked(post: Post) {
        repository.save(post)
    }

    override fun onDeleteClicked(post: Post) {
        repository.delete(post)
    }

    override fun onMoveClicked(post: Post, moveBy: Int) {
        repository.move(post, moveBy)
    }

    override fun onYouTubeClicked(post: Post) {
        onViewYoutubeLink.value = post.video!!
    }

    // endregion PostActionListener
}