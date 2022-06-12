package xyz.daijoubuteam.foodshoppingapp.client.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.VerticalCardEateryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSearchBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by lazy {
        val factory = SearchViewModelFactory()
        ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setSearchTextObserver()
        setOnNavigateUpButtonClickListener()
        setupSoftKeyboardUI()
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun setupAdapter() {
        binding.searchedEateries.adapter = SearchEateryAdapter(SearchEateryAdapter.OnClickListener {
            lifecycleScope.launch {
                val result = viewModel.findFullEateryInfo(it.id)
                if(result.isFailure) {
                    Snackbar.make(requireView(), result.exceptionOrNull()?.message ?: "An error occur", Snackbar.LENGTH_LONG).show()
                }
                else {
                    val eatery = result.getOrNull()!!
                    val action = SearchFragmentDirections.actionSearchFragmentToDetailEateryFragment(eatery)
                    findNavController().navigate(action)
                }
            }
        })
        val adapter = binding.searchedEateries.adapter as SearchEateryAdapter
        viewModel.eatery.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    private fun setSearchTextObserver() {
        viewModel.searchText.observe(viewLifecycleOwner) {
            viewModel.search()
        }
    }

    private fun setOnNavigateUpButtonClickListener(){
        binding.navigateUpChevron.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSoftKeyboardUI() {
        binding.searchEditText.setOnFocusChangeListener {view, hasFocus ->
            if (!shouldShowSoftKeyboard()) {
                hideKeyboard()
            }
        }
    }

    private fun shouldShowSoftKeyboard(): Boolean {
        return binding.searchEditText.hasFocus()
    }
}