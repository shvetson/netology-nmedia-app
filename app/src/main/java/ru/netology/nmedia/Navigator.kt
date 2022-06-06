package ru.netology.nmedia

import ru.netology.nmedia.model.Post

interface Navigator {
    fun showDetails(post : Post)
    fun goBack()
    fun toast(messageRes : Int)
}