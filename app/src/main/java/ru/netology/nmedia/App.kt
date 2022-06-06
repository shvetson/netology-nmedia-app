package ru.netology.nmedia

import android.app.Application
import ru.netology.nmedia.model.repositoty.PostRepositoryFileImpl

class App : Application() {
    val repository = PostRepositoryFileImpl()
}