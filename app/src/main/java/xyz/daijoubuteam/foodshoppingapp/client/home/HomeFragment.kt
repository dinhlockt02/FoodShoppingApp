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
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CuisineAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.SlideAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentHomeBinding
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.SlideItem
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var listPopularEatery : ArrayList<Eatery>
    private lateinit var listCategories : ArrayList<Category>
    private lateinit var listCuisines : ArrayList<Cuisine>

    private lateinit var page: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var listItems : ArrayList<SlideItem>

    init {
        initListCategories()
        initialListEatery()
        initialCuisineList()
        initialListItems()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSliderTimer(2000,3000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home , container, false)

        //recycler categories
        binding.recyCategories.adapter = CategoryAdapter()
        val adapterCategory = binding.recyCategories.adapter as CategoryAdapter
        adapterCategory.submitList(listCategories)


        //recycler popular eatery
        binding.recyPopularEatery.adapter = EateryAdapter()
        val adapterPopuplarEatery = binding.recyPopularEatery.adapter as EateryAdapter
        adapterPopuplarEatery.submitList(listPopularEatery)

        //recycler eatery
        binding.recyNearByEatery.adapter = EateryAdapter()
        val adapterNearByEatery = binding.recyNearByEatery.adapter as EateryAdapter
        adapterNearByEatery.submitList(listPopularEatery)

        //List cuisines
        binding.recyPopularCuisine.adapter = CuisineAdapter()
        val bindingCuisineAdapter = binding.recyPopularCuisine.adapter as CuisineAdapter
        bindingCuisineAdapter.submitList(listCuisines)

        //Slider
        page = binding.myPager
        tabLayout = binding.myTablayout


        val slideAdapter: SlideAdapter = SlideAdapter(this.requireContext(), listItems)
        page.adapter = slideAdapter

        // The_slide_timer
        tabLayout.setupWithViewPager(page, true)



        return binding.root
    }

    private fun initialListItems() {
        listItems = ArrayList<SlideItem>()
        listItems.add(
            SlideItem(
                1,
                R.drawable.img_rectangle12,
            )
        )
        listItems.add(
            SlideItem(
                2,
                R.drawable.img_rectangle12,
            )
        )
        listItems.add(
            SlideItem(
                3,
                R.drawable.img_rectangle12,
            )
        )
        listItems.add(
            SlideItem(
                4,
                R.drawable.img_rectangle12,
            )
        )
    }

    private fun initialCuisineList() {
        listCuisines = ArrayList<Cuisine>()
        listCuisines.add(Cuisine(1, "New Zealander", R.drawable.img_newzealand))
        listCuisines.add(Cuisine(2, "Italian", R.drawable.img_italian))
        listCuisines.add(Cuisine(3, "Greece", R.drawable.img_greece))
    }

    private fun initListCategories() {
        //List categories
        listCategories = ArrayList<Category>()
        listCategories.add(Category(1, "Promos", R.drawable.img_discount))
        listCategories.add(Category(2, "Meal", R.drawable.img_steak))
        listCategories.add(Category(3, "Drink", R.drawable.img_cocktail))
        listCategories.add(Category(4, "Fastfood", R.drawable.img_burger))
        listCategories.add(Category(5, "Snack", R.drawable.img_macaroons))
    }

    private fun initialListEatery() {
        listPopularEatery = ArrayList<Eatery>()
        listPopularEatery.add(Eatery(1, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15))
        listPopularEatery.add(Eatery(3, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15_2))
        listPopularEatery.add(Eatery(3, "Mon 1", "ABC CDE", 15, 2.0, 5, R.drawable.img_rectangle15))
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
                if (page.currentItem < listItems.size - 1) {
                    page.currentItem = page.currentItem + 1
                } else page.currentItem = 0
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