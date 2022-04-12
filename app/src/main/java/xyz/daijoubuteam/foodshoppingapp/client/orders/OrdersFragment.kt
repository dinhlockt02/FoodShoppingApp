package xyz.daijoubuteam.foodshoppingapp.client.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.bag.OrdersBagFragment
import xyz.daijoubuteam.foodshoppingapp.client.orders.history.OrdersHistoryFragment
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment() {

    private var tabTitle = listOf<String>("1","2","3","4")
    private lateinit var binding : FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false)

//        binding.viewPagerOrders.adapter = customOrdersFragementAdapter(this.childFragmentManager, lifecycle)
//        TabLayoutMediator(binding.tabLayoutOrders, binding.viewPagerOrders) {
//                tab, pos -> tab.text = tabTitle[pos]
//
//        }

        val viewPage2 = binding.viewPagerOrders
        viewPage2.adapter = CustomOrdersFragementAdapter(requireActivity())
        val tabLayout: TabLayout = binding.tabLayoutOrders
        TabLayoutMediator(
            tabLayout, viewPage2
        ) { tab, position ->
            tab.text = (viewPage2.adapter as CustomOrdersFragementAdapter).tabTitle[position]
        }.attach()


        return binding.root
    }

//    class customOrdersFragementAdapter(fragmentManager: FragmentManager, lifeCycle : Lifecycle)
//        : FragmentStateAdapter(fragmentManager, lifeCycle) {
//        private var tabTitle = listOf<String>("1","2","3","4")
//        private var fragments = listOf<Fragment>(
//            OrdersBagFragment(),
//            OrdersHistoryFragment(),
//            OrdersOnGoingFragment()
//        )
//
//        override fun getItemCount(): Int {
//            return fragments.size
//        }
//
////
////        override fun createFragment(position: Int): Fragment {
////            Log.i("Orders", position.toString())
////            when(position) {
////                0 -> return OrdersOnGoingFragment()
////                1 -> return OrdersUpComingFragment()
////                2 -> return OrdersHistoryFragment()
////                3 -> return OrdersBagFragment()
////            }
////            return OrdersOnGoingFragment()
////        }

    }

class CustomOrdersFragementAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {
     var tabTitle = listOf<String>("1","2","3","4")
    private var fragments = listOf(
        OrdersBagFragment(),
        OrdersHistoryFragment(),
        OrdersOnGoingFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

//
//        override fun createFragment(position: Int): Fragment {
//            Log.i("Orders", position.toString())
//            when(position) {
//                0 -> return OrdersOnGoingFragment()
//                1 -> return OrdersUpComingFragment()
//                2 -> return OrdersHistoryFragment()
//                3 -> return OrdersBagFragment()
//            }
//            return OrdersOnGoingFragment()
//        }

}