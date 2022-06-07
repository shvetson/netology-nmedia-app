package ru.netology.nmedia.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
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
) : Parcelable