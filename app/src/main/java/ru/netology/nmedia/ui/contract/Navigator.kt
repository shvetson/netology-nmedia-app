package ru.netology.nmedia.ui.contract

import androidx.fragment.app.Fragment
import ru.netology.nmedia.model.Post

//fun Fragment.navigator() : Navigator {
//    return requireActivity() as Navigator
//}

interface Navigator {
    fun showDetails(post : Post)
    fun showNewPost()
    fun showEditPost(post: Post)
    fun goBack()
    fun toast(messageRes : Int)
}