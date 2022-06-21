package ru.netology.nmedia.ui.listener

import ru.netology.nmedia.model.Post

interface PostActionListener {
    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onViewClicked(post: Post)

    fun onDeleteClicked(post: Post)
    fun onSaveClicked(post: Post)
    fun onEditClicked(post: Post)

    fun onMoveClicked(post: Post, moveBy: Int)

    fun onPostDetailsClicked(postId: String)
    fun onUpdateClicked(postId: String)

//    fun onAddClicked()

    fun onYouTubeClicked(post: Post)
}