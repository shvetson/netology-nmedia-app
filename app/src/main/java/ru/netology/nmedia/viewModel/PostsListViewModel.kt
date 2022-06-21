package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository
import ru.netology.nmedia.ui.listener.PostActionListener

class PostsListViewModel(application: Application
) : AndroidViewModel(application), PostActionListener {

    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data by repository::data

    val onShareContent = SingleLiveEvent<String>()
    val onViewYoutubeLink = SingleLiveEvent<String>()

    // region PostActionListener
    override fun onLikeClicked(post: Post) {
        repository.like(post)
    }

    override fun onShareClicked(post: Post) {
        onShareContent.value = post.content
        repository.share(post)
    }

    override fun onViewClicked(post: Post) {
        repository.view(post)
    }

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