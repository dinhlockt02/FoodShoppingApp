package xyz.daijoubuteam.foodshoppingapp.client.profile.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentNotificationListTabBinding
import xyz.daijoubuteam.foodshoppingapp.model.Notification

class NotificationListTabFragment(
    private val notificationList: LiveData<List<Notification>>,
    private val viewModel: NotificationListViewModel
) : Fragment() {

    private lateinit var binding: FragmentNotificationListTabBinding
    private lateinit var adapter: NotificationListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationListTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = NotificationListItemAdapter(NotificationListItemAdapter.OnClickListener {
            viewModel.markNotificationAsRead(it)
        })
        binding.notificationRecyclerview.adapter = adapter
        notificationList.observe(viewLifecycleOwner){
            if(it != null){
                Timber.i("CHANGED")
                adapter.submitList(it.toList())
            }
        }
    }
}