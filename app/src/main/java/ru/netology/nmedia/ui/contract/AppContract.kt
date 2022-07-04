package ru.netology.nmedia.ui.contract

import androidx.fragment.app.Fragment
import ru.netology.nmedia.model.Post

fun Fragment.contract(): AppContract = requireActivity() as AppContract

interface AppContract {
    fun launchShareIntent(content: String)
    fun launchViewVideoOnYouTube(uri: String)
    fun launchEditPostFromListPosts(post: Post)
    fun launchPostContentFromListPosts()
    fun launchPostDetailsFromListPosts(post: Post)
    fun launchEditPostFromDetailsPost(post: Post)
}