package ru.netology.nmedia.model.repositoty

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.impl.PostsListener

interface PostRepository {
    val data: LiveData<List<Post>>

    fun getAll(): List<Post>
    fun getById(postId: String): Post

    fun delete(post: Post)
    fun move(post: Post, moveBy: Int)
    fun save(post: Post)

    fun like(post: Post)
    fun share(post: Post)
    fun view(post: Post)

    fun addListener(listener: PostsListener)
    fun removeListener(listener: PostsListener)
}