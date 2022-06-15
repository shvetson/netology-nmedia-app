package ru.netology.nmedia

import android.app.Application
import ru.netology.nmedia.model.impl.PostRepositoryFileImpl
import ru.netology.nmedia.model.repositoty.PostRepository

class App : Application() {
    val repository : PostRepository = PostRepositoryFileImpl()
}