package ru.netology.nmedia.model.repositoty

import ru.netology.nmedia.model.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun getId(postId: String): Post

    fun delete(post: Post)
    fun move(post: Post, moveBy: Int)
    fun save(post: Post)

    fun updateDate(post: Post) // TODO

    fun like(post: Post)
    fun share(post: Post)
    fun view(post: Post)

    fun addListener(listener: PostsListener)
    fun removeListener(listener: PostsListener)
}