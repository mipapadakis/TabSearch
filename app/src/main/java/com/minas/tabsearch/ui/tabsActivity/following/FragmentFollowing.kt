package com.minas.tabsearch.ui.tabsActivity.following

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.databinding.TabListBinding
import com.minas.tabsearch.di.tabsModule
import com.minas.tabsearch.ui.profileActivity.ProfileActivity
import com.minas.tabsearch.ui.tabsActivity.IStatusActions
import com.minas.tabsearch.ui.tabsActivity.Tabs
import com.minas.tabsearch.ui.tabsActivity.TabsActivity
import com.minas.tabsearch.ui.tabsActivity.TabsEvent
import com.minas.tabsearch.ui.tabsActivity.TabsViewModel
import com.minas.tabsearch.ui.tabsActivity.UserAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.context.loadKoinModules

class FragmentFollowing : Fragment(), IStatusActions {
    private lateinit var binding: TabListBinding
    private val viewModel by activityViewModel<TabsViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TabListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(tabsModule)
        setupViewModel()
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onTabChange(Tabs.Following)
        refresh()
    }

    private fun setupAdapter() {
        adapter = UserAdapter(this, onItemClick = { user -> viewModel.onUserClick(user) })
        binding.recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.recyclerView.adapter = adapter
        binding.refresher.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupViewModel() {
        viewModel.tabsState.subscribeToState(viewLifecycleOwner) {
            when (it.eventName) {
                TabsEvent.OnTabChange -> {}
                TabsEvent.OnUserClick -> {
                    startActivity(
                        ProfileActivity.newInstance(
                            context,
                            name = it.user?.name,
                            lastName = it.user?.lastName,
                            username = it.user?.userName,
                            status = it.user?.friendStatus ?: FriendStatus.NotFriend,
                        )
                    )
                }
                TabsEvent.Search -> {
                    viewModel.searchFollowing(it.searchTerm)
                }
                TabsEvent.CancelSearchMode -> {
                    viewModel.searchFollowing("")
                }
                TabsEvent.LoadFollowing, TabsEvent.SearchFollowing -> {
                    adapter.submitList(it.followingList)
                }
                TabsEvent.Error -> {
                    Log.d("TabSearch test", "Error = ${it.errorType}: ${it.errorMessage}")
                }
                TabsEvent.LoadFollowers, TabsEvent.SearchFollowers -> {
                    Log.d("TabSearch test", "followersList (${it.followersList.size} items): ${it.followersList}")
                }
                else -> {}
            }
            if (it.eventName != TabsEvent.None) {
                Log.d(
                    "TabSearch test: FragmentFollowing UI state",
                    "FragmentFollowing UiEvent = ${it.eventName}"
                )
                binding.refresher.isRefreshing = false
                viewModel.setEventNone()
            }
        }
    }

    override fun sendFriendRequest(user: User) {
        showToast("Friend Request sent")
        Log.d("TabSearch test", "sendFriendRequest($user)")
        viewModel.sendFriendRequest(user)
    }

    override fun cancelFriendRequest(user: User) {
        showToast("Friend Request cancelled")
        Log.d("TabSearch test", "cancelFriendRequest($user)")
        viewModel.cancelFriendRequest(user)
    }

    override fun acceptFriendRequest(user: User) {
        showToast("Friend Request accepted")
        Log.d("TabSearch test", "acceptFriendRequest($user)")
        viewModel.acceptFriendRequest(user)
    }

    override fun declineFriendRequest(user: User) {
        showToast("Friend Request declined")
        Log.d("TabSearch test", "declineFriendRequest($user)")
        viewModel.declineFriendRequest(user)
    }

    override fun unFriend(user: User) {
        showToast("unfriended")
        Log.d("TabSearch test", "unFriend($user)")
        viewModel.unFriend(user)
    }

    private fun refresh() {
        viewModel.setEventNone()
        binding.refresher.isRefreshing = true
        viewModel.refresh()
    }

    private fun showToast(str: String) = (activity as TabsActivity).showToast(str)
}