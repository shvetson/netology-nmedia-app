package ru.netology.nmedia.ui.listener

import ru.netology.nmedia.model.Post

interface ScreenActionListener {
    fun onPostEditClicked(initialPost: Post)
    fun onPostDetailsClicked(post: Post)
}