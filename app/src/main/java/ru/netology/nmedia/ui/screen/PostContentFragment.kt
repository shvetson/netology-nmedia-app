package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostContentBinding

class PostContentFragment : Fragment() {

    lateinit var binding: FragmentPostContentBinding
//    private val args by navArgs<PostContentFragmentArgs>()

    companion object {
        const val RESULT_KEY = "postNewContent"
        const val REQUEST_KEY = "requestKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPostContentBinding.bind(view)
        with(binding) {
//            editEditText.setText(args.initialContent)

            editEditText.requestFocus()
            okButton.setOnClickListener {
                onOkButtonClicked(this)
            }
        }
    }

    private fun onOkButtonClicked(binding: FragmentPostContentBinding) {
        val text = binding.editEditText.text

        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }
}