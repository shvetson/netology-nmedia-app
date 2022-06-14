package ru.netology.nmedia.ui.listener

import ru.netology.nmedia.model.Post

interface PostActionListener {
    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onViewClicked(post: Post)

    fun onRemoveClicked(post: Post)
    fun onMoveClicked(post: Post, moveBy: Int)
    fun onPostDetailsClicked(post: Post)
    fun onUpdateClicked(post: Post)

//    fun onSaveClicked(content: String)
//    fun onEditClicked(post: Post)
//    fun onAddClicked()

    fun onYouTubeClicked(post: Post)
}