package com.minas.tabsearch.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.minas.tabsearch.R
import com.minas.tabsearch.databinding.ActivityTabsBinding
import com.minas.tabsearch.di.userListModule
import com.minas.tabsearch.ui.UserListViewModel
import com.minas.tabsearch.ui.fragments.FragmentFollowers
import com.minas.tabsearch.ui.fragments.FragmentFollowing
import com.minas.tabsearch.util.Tabs
import com.minas.tabsearch.util.UiEvent
import com.minas.tabsearch.util.UserListEvent
import com.minas.tabsearch.util.hideKeyboard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class TabsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabsBinding
    private val viewModel: UserListViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(userListModule)
        binding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setUpViewPager()
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    Log.i("TabSearch test: UI state", "eventName = ${it.eventName}")
                    when (it.eventName) {
                        UiEvent.OnTabChange -> {}
                        UiEvent.OnChangeFriendStatus -> {}
                        UiEvent.OnUserClick -> {}
                        UiEvent.Search -> {}
                        UiEvent.CancelSearchMode -> {}
                        UiEvent.PopBackStack -> {}
                        UiEvent.Error -> {}
                        else -> {}
                    }
                }
                viewModel.listState.collectLatest {
                    Log.i("TabSearch test: list state", "eventName = ${it.eventName}")
                    when (it.eventName) {
                        UserListEvent.LoadFollowing -> {}
                        UserListEvent.LoadFollowers -> {}
                        UserListEvent.SearchFollowing -> {}
                        UserListEvent.SearchFollowers -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpViewPager() {
        binding.viewpager.adapter = ScreenSlidePagerAdapter(this)
        binding.viewpager.currentItem = 0
        binding.viewpager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->

            tab.text = when (position) {
                0 -> getString(R.string.following) //TODO number of following?
                else -> getString(R.string.followers) //TODO number of followers?
            }
        }.attach()

        binding.tabLayout.getTabAt(0)?.select()

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
                if (position == 0) {
                    viewModel.onTabChange(Tabs.Following)
// TODO?                   if(viewModel.inSearchMode()) {
//                        viewModel.searchFollowing()
//                    }
                } else {
                    viewModel.onTabChange(Tabs.Followers)
                }
            }
        })
    }

    override fun onDestroy() {
        unloadKoinModules(userListModule)
        super.onDestroy()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> FragmentFollowing()
                else -> FragmentFollowers()
            }
        }
    }
}
