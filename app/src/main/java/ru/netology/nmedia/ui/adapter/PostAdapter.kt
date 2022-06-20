package ru.netology.nmedia.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
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
import ru.netology.nmedia.util.Utils.formatValue
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

internal class PostAdapter(
    private val actionListener: PostActionListener
//) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(), View.OnClickListener {
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DiffCallback), View.OnClickListener {

    //3. Прописать список постов
//    var posts: List<Post> by Delegates.observable(emptyList()) {_, oldPosts, newPosts ->
//        notifyDataSetChanged()
//    }

//    var posts: List<Post> = emptyList()
//        //Прописать DiffUtil
//        set(newValue) {
//            val diffCallback = PostsDiffCallback(field, newValue)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            field = newValue
//            diffResult.dispatchUpdatesTo(this)
//        }

    //1. Определить ViewHolder
    inner class PostViewHolder(
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
                actionListener.onLikeClicked(post)
            }
            R.id.post_share_button -> {
                actionListener.onShareClicked(post)
            }
            R.id.post_view_button -> {
                actionListener.onViewClicked(post)
            }
            R.id.video_image_view -> {
                actionListener.onYouTubeClicked(post)
            }
            else -> {
                actionListener.onPostDetailsClicked(post.id)
            }
        }
    }

    //4.1 Имплементация трех методов
    //Для ListAdapter этот метод не нужен
//    override fun getItemCount(): Int = posts.size

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
//        val post = posts[position]
//        holder.bind(post)
        holder.bind(getItem(position))
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context: Context = view.context
        val post: Post = view.tag as Post

//        val position = posts.indexOfFirst { it.id == post.id }
        val position = currentList.indexOfFirst { it.id == post.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.menu_item_move_up))
            .apply {
                isEnabled = position > 0
            }
        popupMenu.menu.add(
            0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.menu_item_move_down)
        ).apply {
//            isEnabled = position < posts.size - 1
            isEnabled = position < itemCount - 1
        }
        popupMenu.menu.add(0, ID_UPDATE, Menu.NONE, context.getString(R.string.menu_item_edit))
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.menu_item_delete))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onMoveClicked(post, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onMoveClicked(post, 1)
                }
                ID_UPDATE -> {
                    actionListener.onUpdateClicked(post.id)
                }
                ID_REMOVE -> {
                    actionListener.onRemoveClicked(post)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_UPDATE = 4
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

class PostsDiffCallback(
    private val oldList: List<Post>,
    private val newList: List<Post>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost: Post = oldList[oldItemPosition]
        val newPost: Post = newList[newItemPosition]
        return oldPost.id == newPost.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost: Post = oldList[oldItemPosition]
        val newPost: Post = newList[newItemPosition]
        return oldPost == newPost
    }
}