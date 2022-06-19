package ru.netology.nmedia.ui.contract

import androidx.fragment.app.Fragment
import ru.netology.nmedia.model.Post

//fun Fragment.navigator() : Navigator {
//    return requireActivity() as Navigator
//}

interface Navigator {
    fun showDetails(postId : String)
    fun showNewPost()
    fun showEditPost(postId: String)
    fun goBack()
    fun toast(messageRes : Int)
}