package ru.netology.nmedia.ui.listener

import ru.netology.nmedia.model.Post

interface ScreenActionListener {
    fun onPostEditClicked(postId: String)
    fun onPostDetailsClicked(post: Post)
}