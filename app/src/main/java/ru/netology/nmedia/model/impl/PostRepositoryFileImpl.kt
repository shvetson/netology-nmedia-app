package ru.netology.nmedia.model.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.javafaker.Faker
import ru.netology.nmedia.PostNotFoundException
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.repositoty.PostRepository
import java.util.*

typealias PostsListener = (posts: List<Post>) -> Unit

class PostRepositoryFileImpl : PostRepository {
    private var posts: MutableList<Post> = mutableListOf()
    private val listeners: MutableSet<PostsListener> = mutableSetOf()

    init {
        val faker = Faker.instance()

        val generatedPosts: List<Post> = (1..100).map {
            Post(
                id = UUID.randomUUID().toString(),
                author = faker.name().name(),
                content = faker.lorem().characters(),
                like = Random().nextInt(1500),
                share = Random().nextInt(1500),
                view = Random().nextInt(15000)
            )
        }
        posts = generatedPosts.toMutableList()
    }

//    private val data = MutableLiveData<Post>()
//    override fun get(): LiveData<Post> {
//        return data
//    }

    override fun getAll(): List<Post> {
        return posts
    }

    override fun getById(postId: String): Post {
        return posts.firstOrNull { it.id == postId } ?: throw PostNotFoundException()
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

    override fun delete(post: Post) {
        val curIndex = posts.indexOfFirst {
            it.id == post.id
        }
        if (curIndex != -1) {
            posts = ArrayList(posts)
            posts.removeAt(curIndex)
            notifyChanges()
        }
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
//        val currentPost = checkNotNull(data.value) { "Data value should be not null!"}


        val curIndex = posts.indexOfFirst { it.id == post.id }
        if (curIndex != -1) {
            val updatedPost: Post = post.copy(like = getLikeCount(post), likeFlag = !post.likeFlag)
            updatePostsList(curIndex, updatedPost)

//            data.value = updatedPost
        }

    }

    private fun getLikeCount(post: Post) =
        if (post.likeFlag) post.like - 1 else post.like + 1

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

    private fun updatePostsList(position: Int, post: Post) {
        posts = ArrayList(posts)
        posts[position] = post
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