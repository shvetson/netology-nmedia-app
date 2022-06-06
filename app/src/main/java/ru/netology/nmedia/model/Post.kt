package ru.netology.nmedia.model

import java.util.*

data class Post(
    val id: String,
    val author: String,
    val created: Long = Date().time,
    val content: String,
    val video: String? = null,
    val like: Int,
    val likeFlag: Boolean = false,
    val share: Int,
    val view: Int
)