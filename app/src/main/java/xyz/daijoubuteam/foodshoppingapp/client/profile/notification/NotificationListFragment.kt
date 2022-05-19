package xyz.daijoubuteam.foodshoppingapp.client.profile.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentNotificationListBinding

class NotificationListFragment : Fragment() {

    private lateinit var binding: FragmentNotificationListBinding
    private lateinit var notificationListTabAdapter: NotificationListTabAdapter
    private val viewmodel: NotificationListViewModel by lazy {
        val factory = NotificationListViewModelFactory()
        ViewModelProvider(this, factory)[NotificationListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationListBinding.inflate(inflater, container, false)
        setupMessageObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationListTabAdapter = NotificationListTabAdapter(this)
        binding.pager.adapter = notificationListTabAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = notificationListTabAdapter.tabs[position]
        }.attach()
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "Notification"
    }

    private fun setupMessageObserver(){
        viewmodel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewmodel.onShowMessageComplete()
            }
        }
    }


    private inner class NotificationListTabAdapter(fragment: Fragment): FragmentStateAdapter(fragment){


        val allNotification = "All notification";
        val readNotification = "NotRead notification";

        val tabs = listOf<String>(allNotification, readNotification)

        override fun getItemCount(): Int = tabs.size

        override fun createFragment(position: Int): Fragment {
            return if(tabs[position] == allNotification) {
                NotificationListTabFragment(viewmodel.notifications, viewmodel);
            } else {
                NotificationListTabFragment(viewmodel.notReadNotification, viewmodel);
            }
        }

    }
}