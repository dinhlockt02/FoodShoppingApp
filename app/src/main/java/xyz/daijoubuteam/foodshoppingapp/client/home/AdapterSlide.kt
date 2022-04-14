package xyz.daijoubuteam.foodshoppingapp.client.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.SlideItem

class AdapterSlide(
    private val Mcontext: Context,
    private val listSlideItem: List<SlideItem>
) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sliderLayout = inflater.inflate(R.layout.item_slide, null)
        container.addView(sliderLayout)
        return sliderLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return listSlideItem.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }
}