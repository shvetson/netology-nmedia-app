package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.ui.contract.AppContract
import ru.netology.nmedia.ui.screen.PostDetailsFragmentDirections
import ru.netology.nmedia.ui.screen.PostsListFragmentDirections

class MainActivity : AppCompatActivity(R.layout.activity_main), AppContract {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun launchShareIntent(content: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
        }

        val shareIntent =
            Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
        startActivity(shareIntent)
    }

    override fun launchViewVideoOnYouTube(uri: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = android.net.Uri.parse(uri)
        }
        startActivity(intent)
    }

    override fun launchEditPostFromListPosts(post: Post) {
        val direction = PostsListFragmentDirections.actionPostsListFragmentToPostEditFragment(post)
        navController.navigate(direction)
    }

    override fun launchEditPostFromDetailsPost(post: Post) {
        val direction =
            PostDetailsFragmentDirections.actionPostDetailsFragmentToPostEditFragment(post)
        navController.navigate(direction)
    }

    override fun launchListPostsFromDetailsPost() {
        val direction =
            PostDetailsFragmentDirections.actionPostDetailsFragmentToPostsListFragment()
        navController.navigate(direction)
//      findNavController().popBackStack(R.id.postsListFragment, false)
//      findNavController().navigateUp()
    }

    override fun launchPostContentFromListPosts() {
        val direction =
            PostsListFragmentDirections.actionPostsListFragmentToPostContentFragment()
        navController.navigate(direction)
    }

    override fun launchPostDetailsFromListPosts(post: Post) {
        val direction =
            PostsListFragmentDirections.actionPostsListFragmentToPostDetailsFragment(post)
        navController.navigate(direction)
    }
}