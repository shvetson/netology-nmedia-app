package ru.netology.nmedia.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.listener.PostActionListener
import ru.netology.nmedia.ui.listener.ScreenActionListener
import ru.netology.nmedia.util.Utils.formatValue
import java.text.SimpleDateFormat

internal class PostAdapter(
    private val postActionListener: PostActionListener,
    private val screenActionListener: ScreenActionListener
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DiffCallback), View.OnClickListener {

    //1. Определить ViewHolder
    class PostViewHolder(
        private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        //2. Добавить функцию по отрисовке
        @SuppressLint("SimpleDateFormat")
        fun bind(post: Post) {
            with(binding) {

                // Прописываем в тэгах какой элемент списка был выбран
                itemView.tag = post
                optionsButton.tag = post
                postLikeButton.tag = post
                postShareButton.tag = post
                postViewButton.tag = post
                videoImageView.tag = post

                postAuthorTextView.text = post.author
                postCreatedTextView.text =
                    SimpleDateFormat("dd MMM yyyy в hh:mm").format(post.created)
                postContentTextView.text = post.content
                postLikeButton.isChecked = post.likeFlag
                postLikeButton.text = formatValue(post.like)
                postShareButton.text = formatValue(post.share)
                postViewButton.text = formatValue(post.view)

                if (post.video == null) videoImageView.visibility = View.INVISIBLE
                else videoImageView.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(view: View) {
        // Считываем из тэга какой элемент списка был выбран
        val post = view.tag as Post

        when (view.id) {
            R.id.options_button -> {
                showPopupMenu(view)
            }
            R.id.post_like_button -> {
                postActionListener.onLikeClicked(post)
            }
            R.id.post_share_button -> {
                postActionListener.onShareClicked(post)
            }
            R.id.post_view_button -> {
                postActionListener.onViewClicked(post)
            }
            R.id.video_image_view -> {
                postActionListener.onYouTubeClicked(post)
            }
            else -> {
                screenActionListener.onPostDetailsClicked(post)
            }
        }
    }

    //4.1 Имплементация трех методов
    //Для ListAdapter этот метод не нужен
    //override fun getItemCount(): Int = posts.size

    //4.2 Имплементация трех методов
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)

        // Инициализация слушателей
        binding.optionsButton.setOnClickListener(this)
        binding.root.setOnClickListener(this)
        binding.postLikeButton.setOnClickListener(this)
        binding.postShareButton.setOnClickListener(this)
        binding.postViewButton.setOnClickListener(this)
        binding.videoImageView.setOnClickListener(this)

        return PostViewHolder(binding)
    }

    //4.3 Имплементация трех методов
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //5. Прописываем меню после создания макета
    private fun showPopupMenu(view: View) {
        val context: Context = view.context
        val post: Post = view.tag as Post
        val position = currentList.indexOfFirst { it.id == post.id }

        val popupMenu by lazy {
            PopupMenu(context, view).apply {
                inflate(R.menu.options_post)
                this.menu.findItem(R.id.menu_move_down).isEnabled = position < itemCount - 1
                this.menu.findItem(R.id.menu_move_up).isEnabled = position > 0
            }
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    screenActionListener.onPostEditClicked(post.id)
                    true
                }
                R.id.menu_delete -> {
                    postActionListener.onDeleteClicked(post)
                    true
                }
                R.id.menu_move_up -> {
                    postActionListener.onMoveClicked(post, -1)
                    true
                }
                R.id.menu_move_down -> {
                    postActionListener.onMoveClicked(post, 1)
                    true
                }
                else -> false
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}