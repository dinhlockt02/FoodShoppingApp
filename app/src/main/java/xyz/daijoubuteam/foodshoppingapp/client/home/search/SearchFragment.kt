package xyz.daijoubuteam.foodshoppingapp.client.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSearchBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setOnNavigateUpButtonClickListener()
        setupSoftKeyboardUI()
        return binding.root
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