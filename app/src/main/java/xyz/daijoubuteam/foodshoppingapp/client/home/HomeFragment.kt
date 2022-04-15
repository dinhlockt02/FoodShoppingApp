package xyz.daijoubuteam.foodshoppingapp.client.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentHomeBinding
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.SlideItem
import java.util.*

class HomeFragment : Fragment() {
    var page: ViewPager? = null
    var tabLayout: TabLayout? = null
    var listItems: ArrayList<SlideItem>? = null
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        //List popular eateries and near by eateries
        val listPopularEatery : ArrayList<Eatery> = ArrayList()
        listPopularEatery.add(Eatery(1, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15))
        listPopularEatery.add(Eatery(3, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15_2))
        listPopularEatery.add(Eatery(3, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15))

        val adapterEatery: AdapterEatery = AdapterEatery()
        adapterEatery.setData(listPopularEatery)
        binding.recyPopularEatery.adapter = adapterEatery
        binding.recyPopularEatery.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyNearByEatery.adapter = adapterEatery
        binding.recyNearByEatery.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        //List categories
        val listCategories : ArrayList<Category> = ArrayList()
        listCategories.add(Category(1, "Promos", R.drawable.img_discount))
        listCategories.add(Category(2, "Meal", R.drawable.img_steak))
        listCategories.add(Category(3, "Drink", R.drawable.img_cocktail))
        listCategories.add(Category(4, "Fastfood", R.drawable.img_burger))
        listCategories.add(Category(5, "Snack", R.drawable.img_macaroons))

        val adapterCategory: AdapterCategory = AdapterCategory()
        adapterCategory.setData(listCategories)
        binding.recyCategories.adapter = adapterCategory
        binding.recyCategories.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        //List cuisines
        val listCuisines : ArrayList<Cuisine> = ArrayList()
        listCuisines.add(Cuisine(1, "New Zealander", R.drawable.img_newzealand))
        listCuisines.add(Cuisine(2, "Italian", R.drawable.img_italian))
        listCuisines.add(Cuisine(3, "Greece", R.drawable.img_greece))

        val adapterCuisine: AdapterCuisine = AdapterCuisine()
        adapterCuisine.setData(listCuisines)
        binding.recyPopularCuisine.adapter = adapterCuisine
        binding.recyPopularCuisine.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        //Slider
        page = binding.myPager
        tabLayout = binding.myTablayout

        listItems = ArrayList()
        listItems!!.add(
            SlideItem(
                1,
                R.drawable.img_rectangle12,
            )
        )
        listItems!!.add(
            SlideItem(
                2,
                R.drawable.img_rectangle12,
            )
        )
        listItems!!.add(
            SlideItem(
                3,
                R.drawable.img_rectangle12,
            )
        )
        listItems!!.add(
            SlideItem(
                4,
                R.drawable.img_rectangle12,
            )
        )

        val adapterSlide: AdapterSlide = AdapterSlide(this.requireContext(), listItems!!)
        page!!.adapter = adapterSlide

        // navigate to profile
        setupOnAvatarClickListener()

        // The_slide_timer
        tabLayout!!.setupWithViewPager(page, true)
        setSliderTimer(2000,3000)
        return binding.root
    }

    private lateinit var sliderTimerCorroutine: Job

    override fun onDestroyView() {
        super.onDestroyView()
        sliderTimerCorroutine.cancel()
    }

    private fun setSliderTimer(delay: Long, period: Long){
        sliderTimerCorroutine = lifecycleScope.launch {
            delay(delay)
            while(true){
                if (page!!.currentItem < listItems!!.size - 1) {
                    page!!.currentItem = page!!.currentItem + 1
                } else page!!.currentItem = 0
                delay(period)
            }
        }
    }


    private fun setupOnAvatarClickListener(){
        binding.fragmentHomeAvatar.setOnClickListener {
            val activity = this.activity as? MainActivity
            activity?.setMenuSelectedItem(R.id.profileFragment)
        }
    }
}