package ru.netology.nmedia.model.impl

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository
import java.util.*

typealias PostsListener = (posts: List<Post>) -> Unit

class PostRepositoryFileImpl(
    private val application: Application
) : PostRepository {

    private val listeners: MutableSet<PostsListener> = mutableSetOf()

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private companion object {
        const val FILE_NAME = "posts.json"
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Данные не должны быть null"
        }
        set(newValue) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(newValue))
            }
            data.value = newValue
        }
    override val data: MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.isFile) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it, type) }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun getAll(): List<Post> {
        return posts
    }

    override fun getById(postId: String): Post {
        return posts.firstOrNull { it.id == postId } ?: throw PostNotFoundException()
    }

    override fun delete(post: Post) {
//        val curIndex = posts.indexOfFirst {
//            it.id == post.id
//        }
//        if (curIndex != -1) {
//            posts = ArrayList(posts)
//            (posts as ArrayList<Post>).removeAt(curIndex)
//            notifyChanges()
//        }

        posts = posts.filter{it.id != post.id}
        notifyChanges()
    }

    override fun save(post: Post) {
        posts = ArrayList(posts)

        if (post.id == "") {
            insert(post)
        } else {
            update(post)
        }
        notifyChanges()
    }

    private fun insert(post: Post) {
        posts = (listOf(post.copy(id = UUID.randomUUID().toString())) + posts) as MutableList<Post>
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        } as MutableList<Post>
    }

//    Удалить после проверки работы функции save - update
//    override fun updateDate(post: Post) {
//        val curIndex = posts.indexOfFirst { it.id == post.id }
//        if (curIndex == -1) return
//        val newPost = post.copy(created = Date().time)
//        posts = ArrayList(posts)
//        posts[curIndex] = newPost
//        notifyChanges()
//    }

    override fun like(post: Post) {
//        val curIndex = posts.indexOfFirst { it.id == post.id }
//        if (curIndex != -1) {
//            val updatedPost: Post = post.copy(like = getLikeCount(post), likeFlag = !post.likeFlag)
//            updatePostsList(curIndex, updatedPost)
//        }

        data.value = posts.map {
            if (it.id != post.id) it
            else it.copy(like = getLikeCount(post), likeFlag = !post.likeFlag)
        }
    }

    private fun getLikeCount(post: Post) =
        if (post.likeFlag) post.like - 1 else post.like + 1

    override fun share(post: Post) {
        val curIndex = posts.indexOfFirst { it.id == post.id }
        if (curIndex != -1) {
            val updatedPost: Post = post.copy(share = post.share + 1)
            updatePostsList(curIndex, updatedPost)
        }
    }

    override fun view(post: Post) {
        val curIndex = posts.indexOfFirst { it.id == post.id }
        if (curIndex != -1) {
            val updatedPost: Post = post.copy(view = post.view + 1)
            updatePostsList(curIndex, updatedPost)
        }
    }

    override fun move(post: Post, moveBy: Int) {
        val curIndex = posts.indexOfFirst { it.id == post.id }
        if (curIndex == -1) return
        val newIndex = curIndex + moveBy
        if (newIndex < 0 || newIndex >= posts.size) return
        // Создаем новый список, в котором производятся определенные изменения и отсылаем в адаптер для сравнния со старым, ранее переданный
        // Решение проблемы с мутабельностью списка
        // Но, если идет работа с данными из сети / БД, то возможно это не потребуется тк запросы возвращают новые списки
        posts = ArrayList(posts)
        Collections.swap(posts, curIndex, newIndex)
        notifyChanges()
    }

    private fun updatePostsList(position: Int, post: Post) {
        posts = ArrayList(posts)
        (posts as ArrayList<Post>)[position] = post
        notifyChanges()
    }

    override fun addListener(listener: PostsListener) {
        listeners.add(listener)
        listener.invoke(posts)
    }

    override fun removeListener(listener: PostsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(posts) }
    }
}