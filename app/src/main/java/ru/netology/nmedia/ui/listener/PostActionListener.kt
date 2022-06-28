package ru.netology.nmedia.ui.listener

import ru.netology.nmedia.model.Post

interface PostActionListener {
    //Обработка кликов на элементе списка
    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onYouTubeClicked(post: Post)

    //Обработка кликов на меню
    fun onDeleteClicked(post: Post)
    fun onMoveClicked(post: Post, moveBy: Int)

    fun onSaveClicked(post: Post)
}