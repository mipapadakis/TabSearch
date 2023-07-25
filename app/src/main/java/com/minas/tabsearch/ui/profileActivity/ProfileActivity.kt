package com.minas.tabsearch.ui.profileActivity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.friendStatusToString
import com.minas.tabsearch.databinding.ActivityProfileBinding
import com.minas.tabsearch.util.GenerateRandomUsers

class ProfileActivity : AppCompatActivity() {
    companion object {
        private const val NAME = "name"
        private const val LAST_NAME = "last-name"
        private const val USERNAME = "username"
        private const val STATUS = "status"
        fun newInstance(
            context: Context?,
            name: String?,
            lastName: String?,
            username: String?,
            status: FriendStatus
        ): Intent {
            return Intent(context, ProfileActivity::class.java)
                .putExtra(NAME, name)
                .putExtra(LAST_NAME, lastName)
                .putExtra(USERNAME, username)
                .putExtra(STATUS, friendStatusToString(status))
        }
    }
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra(NAME) ?: NAME
        val lastName = intent.getStringExtra(LAST_NAME) ?: LAST_NAME
        val fullName = "$name $lastName"
        binding.name.text = fullName
        binding.username.text = intent.getStringExtra(USERNAME)
        binding.status.text = intent.getStringExtra(STATUS)
        binding.avatar.setImageDrawable(
            BitmapDrawable(
                resources,
                GenerateRandomUsers.getRandomProfilePic(0, name, lastName)
            )
        )
    }
}