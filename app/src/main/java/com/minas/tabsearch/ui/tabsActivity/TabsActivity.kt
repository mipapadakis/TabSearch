package com.minas.tabsearch.ui.tabsActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.minas.tabsearch.R
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.databinding.ActivityTabsBinding
import com.minas.tabsearch.di.tabsModule
import com.minas.tabsearch.ui.profileActivity.ProfileActivity
import com.minas.tabsearch.ui.tabsActivity.followers.FragmentFollowers
import com.minas.tabsearch.ui.tabsActivity.following.FragmentFollowing
import com.minas.tabsearch.util.hide
import com.minas.tabsearch.util.hideKeyboard
import com.minas.tabsearch.util.isFirstRun
import com.minas.tabsearch.util.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class TabsActivity : AppCompatActivity() {
    companion object {
        private const val WHICH_TAB = "which_tab"
        fun newInstance(context: Context, whichTab: Int = 0): Intent {
            return Intent(context, TabsActivity::class.java).putExtra(WHICH_TAB, whichTab)
        }
    }
    private lateinit var binding: ActivityTabsBinding
    private val viewModel: TabsViewModel by viewModel()
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(tabsModule)
        binding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toast = Toast(this)
        setupViewPager(intent.getIntExtra(WHICH_TAB, 0))
        setupUI()
        setupViewModel()
        if(isFirstRun()) viewModel.generateUsers()
    }

    private fun setupViewModel() {
        viewModel.tabsState.subscribeToState(this) {
            when (it.eventName) {
                TabsEvent.UserClick -> {
                    viewModel.setEventNone()
                    startActivity(
                        ProfileActivity.newInstance(
                            this,
                            name = it.userClicked?.name,
                            lastName = it.userClicked?.lastName,
                            username = it.userClicked?.userName,
                            status = it.userClicked?.friendStatus ?: FriendStatus.NotFriend,
                        )
                    )
                }
                TabsEvent.Search -> {
                    viewModel.setEventNone()
                    if(it.activeTab == Tabs.Following) viewModel.searchFollowing(it.searchTerm)
                    else viewModel.searchFollowers(it.searchTerm)
                }
                TabsEvent.CancelSearchMode -> {
                    viewModel.setEventNone()
                    if(it.activeTab == Tabs.Following) viewModel.searchFollowing("")
                    else viewModel.searchFollowers(it.searchTerm)
                }
                TabsEvent.Error -> {
                    viewModel.setEventNone()
                    Log.d("TabSearch test", "Error = ${it.errorType}: ${it.errorMessage}")
                }
                else -> {}
            }
        }
    }

    private fun setupUI() {
        binding.searchButton.setOnClickListener {
            hideKeyboard()
        }
        binding.cleanSearch.setOnClickListener {
            binding.searchEt.setText("")
        }
        binding.searchEt.doAfterTextChanged {
            if(it.isNullOrEmpty()) {
                binding.cleanSearch.hide()
                viewModel.cancelSearchMode()
            }
            else {
                binding.cleanSearch.show()
                viewModel.search(it.toString())
            }
        }
    }

    private fun setupViewPager(whichTab: Int) {
        binding.viewpager.adapter = ScreenSlidePagerAdapter(this)
        binding.viewpager.currentItem = whichTab
        binding.viewpager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.following) //TODO number of following?
                else -> getString(R.string.followers) //TODO number of followers?
            }
        }.attach()

        binding.tabLayout.getTabAt(whichTab)?.select()

//        val tab0 = binding.tabLayout.getTabAt(0)
//        val tab1 = binding.tabLayout.getTabAt(1)
//TODO?        tab?.setCustomView(R.layout.custom_tab_following)
//        tab1?.setCustomView(R.layout.custom_tab_followers)
//        tab0?.select()

        binding.viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                hideKeyboard()
            }
        })
    }

    fun showToast(text: String) {
        if(text.length > 30) toast.duration = Toast.LENGTH_LONG
        else toast.duration = Toast.LENGTH_SHORT
        toast.cancel()
        toast = Toast.makeText(this, text, toast.duration)
        toast.show()
    }

    override fun onStop() {
        super.onStop()
        toast.cancel()
    }

    override fun onDestroy() {
        unloadKoinModules(tabsModule)
        super.onDestroy()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentFollowing()
                else -> FragmentFollowers()
            }
        }
    }
}
