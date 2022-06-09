package xyz.daijoubuteam.foodshoppingapp.client.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.history.OrdersHistoryFragment
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingFragment
import xyz.daijoubuteam.foodshoppingapp.client.orders.upcoming.OrdersUpComingFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment() {
    private lateinit var binding : FragmentOrdersBinding
    private lateinit var customOrdersFragementAdapter: CustomOrdersFragementAdapter
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customOrdersFragementAdapter = CustomOrdersFragementAdapter(this)
        viewPager2 = binding.pager
        viewPager2.adapter = customOrdersFragementAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = customOrdersFragementAdapter.tabTitle[position]
        }.attach()

    }

    class CustomOrdersFragementAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
        var tabTitle = listOf<String>("UpComing","OnGoing","History")
        private val arrayList: ArrayList<Fragment> = ArrayList()
        init {
            arrayList.add(OrdersUpComingFragment())
            arrayList.add(OrdersOnGoingFragment())
            arrayList.add(OrdersHistoryFragment())
        }

        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
//            return when(position){
//                0 -> OrdersUpComingFragment()
//                1 -> OrdersOnGoingFragment()
//                else -> OrdersHistoryFragment()
//            }
            return arrayList[position]
        }

    }
}
