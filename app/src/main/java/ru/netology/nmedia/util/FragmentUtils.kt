package ru.netology.nmedia.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.App
import ru.netology.nmedia.ui.contract.Navigator
import ru.netology.nmedia.viewModel.PostContentViewModel
import ru.netology.nmedia.viewModel.PostDetailsViewModel
import ru.netology.nmedia.viewModel.PostEditViewModel
import ru.netology.nmedia.viewModel.PostsListViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            PostsListViewModel::class.java -> {
                PostsListViewModel(app.repository)
            }
            PostDetailsViewModel::class.java -> {
                PostDetailsViewModel(app.repository)
            }
            PostContentViewModel::class.java -> {
                PostContentViewModel(app.repository)
            }
            PostEditViewModel::class.java -> {
                PostEditViewModel(app.repository)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator