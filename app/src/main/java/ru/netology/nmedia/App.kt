package ru.netology.nmedia

import android.app.Application
import ru.netology.nmedia.model.repositoty.PostRepository
import ru.netology.nmedia.model.repositoty.PostRepositoryFileImpl

class App : Application() {
    val repository : PostRepository = PostRepositoryFileImpl()
}