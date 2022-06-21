package ru.netology.nmedia.ui.listener

interface ScreenActionListener {
    fun onPostEditClicked(postId: String)
    fun onPostDetailsClicked(postId: String)
}