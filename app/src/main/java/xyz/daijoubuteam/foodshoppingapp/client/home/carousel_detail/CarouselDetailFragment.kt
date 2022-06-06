package xyz.daijoubuteam.foodshoppingapp.client.home.carousel_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentCarouselDetailBinding
import xyz.daijoubuteam.foodshoppingapp.model.Event

class CarouselDetailFragment : Fragment() {
    private lateinit var binding: FragmentCarouselDetailBinding
    private lateinit var eventSelected: Event
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carousel_detail, container, false)
    }
}