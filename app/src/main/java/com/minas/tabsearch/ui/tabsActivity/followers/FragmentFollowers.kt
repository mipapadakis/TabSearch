package com.minas.tabsearch.ui.tabsActivity.followers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.minas.tabsearch.databinding.TabListBinding
import com.minas.tabsearch.di.tabsModule
import com.minas.tabsearch.ui.domain.DomainUser
import com.minas.tabsearch.ui.tabsActivity.IStatusActions
import com.minas.tabsearch.ui.tabsActivity.Tabs
import com.minas.tabsearch.ui.tabsActivity.TabsActivity
import com.minas.tabsearch.ui.tabsActivity.TabsEvent
import com.minas.tabsearch.ui.tabsActivity.TabsViewModel
import com.minas.tabsearch.ui.tabsActivity.UserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.context.loadKoinModules

class FragmentFollowers : Fragment(), IStatusActions {
    private lateinit var binding: TabListBinding
    private val viewModel by activityViewModel<TabsViewModel>()
    private lateinit var adapter: UserAdapter
    private var stopRefreshJob: Job? = null

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
        viewModel.onTabChange(Tabs.Followers)
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
            Log.d("TabSearch test", "Tab 2 ${it.eventName}")
            when (it.eventName) {
                TabsEvent.LoadFollowers -> {
                    viewModel.setEventNone()
                    adapter.submitUserList(it.followersList)
                }
                TabsEvent.UpdateUser -> refresh()
                else -> {}
            }
            if (it.eventName != TabsEvent.None) stopRefresh(800)
        }
    }

    override fun sendFriendRequest(user: DomainUser) {
        showToast("Friend Request sent")
        Log.d("TabSearch test", "sendFriendRequest($user)")
        viewModel.sendFriendRequest(user)
    }

    override fun cancelFriendRequest(user: DomainUser) {
        showToast("Friend Request cancelled")
        Log.d("TabSearch test", "cancelFriendRequest($user)")
        viewModel.cancelFriendRequest(user)
    }

    override fun acceptFriendRequest(user: DomainUser) {
        showToast("Friend Request accepted")
        Log.d("TabSearch test", "acceptFriendRequest($user)")
        viewModel.acceptFriendRequest(user)
    }

    override fun declineFriendRequest(user: DomainUser) {
        showToast("Friend Request declined")
        Log.d("TabSearch test", "declineFriendRequest($user)")
        viewModel.declineFriendRequest(user)
    }

    override fun unFriend(user: DomainUser) {
        showToast("unfriended")
        Log.d("TabSearch test", "unFriend($user)")
        viewModel.unFriend(user)
    }

    private fun refresh() {
        viewModel.setEventNone()
        binding.refresher.isRefreshing = true
        viewModel.refresh()
    }

    private fun stopRefresh(delay: Int) {
        stopRefreshJob?.cancel()
        stopRefreshJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delay.toLong())
            binding.refresher.isRefreshing = false
        }
    }

    private fun showToast(str: String) = (activity as TabsActivity).showToast(str)
}