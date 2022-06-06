package xyz.daijoubuteam.foodshoppingapp.client.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {

    private val viewModel: SavedViewModel by lazy {
        val factory = SavedViewModelFactory()
        ViewModelProvider(this, factory)[SavedViewModel::class.java]
    }

    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater,container, false)
        setupFavoriteEateryAdapter()
        return binding.root
    }

    private fun setupFavoriteEateryAdapter() {
        val adapter = SavedEateriesAdapter(SavedEateriesAdapter.OnClickListener {
            val action = SavedFragmentDirections.actionSavedFragmentToDetailEateryFragment(it)
            findNavController().navigate(action)
        }, viewModel)
        binding.favoritesEateriesRecyclerView.adapter =adapter
        viewModel.favoriteEateries.observe(viewLifecycleOwner) {
            Timber.i("CHANGED ${it.toString()}")
            adapter.submitList(it)
        }
    }

}