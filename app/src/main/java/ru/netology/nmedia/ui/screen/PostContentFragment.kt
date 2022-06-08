package ru.netology.nmedia.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.ui.contract.HasCustomTitle
import ru.netology.nmedia.util.factory
import ru.netology.nmedia.util.navigator
import ru.netology.nmedia.viewModel.PostContentViewModel
import ru.netology.nmedia.viewModel.PostDetailsViewModel

class PostContentFragment : Fragment(), HasCustomTitle {
    lateinit var binding: FragmentPostContentBinding
    private val viewModel: PostContentViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostContentBinding.inflate(inflater, container, false)
        binding.okButton.setOnClickListener {
            onOkButtonClicked()
            navigator().goBack()
        }
        return binding.root
    }

    private fun onOkButtonClicked() {
        val contentPost = binding.editEditText.text

        if (!contentPost.isNullOrBlank()) {
            viewModel.onSaveClicked(contentPost.toString())
        }
    }

    companion object {
        fun newInstance(): PostContentFragment {
            return PostContentFragment()
        }
    }

    override fun getTitleRes(): Int = R.string.title_new_post
}