package xyz.daijoubuteam.foodshoppingapp.utils.BindingAdapter

import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import xyz.daijoubuteam.foodshoppingapp.model.Gender
import com.google.android.material.textfield.TextInputLayout
import xyz.daijoubuteam.foodshoppingapp.R

object GenderDropdownList {
    private fun setupGenderExposedDropdownMenu(view: TextInputLayout){
        val items = listOf(Gender.MALE.gender, Gender.FEMALE.gender, Gender.OTHER.gender)
        val adapter = ArrayAdapter(view.context, R.layout.item_gender_exposed_dropdown_menu, items)
        (view.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }



    @BindingAdapter("gender")
    @JvmStatic fun setGender(view: TextInputLayout, gender: Gender?){
        gender?.let{
            if(view.editText?.text.toString() != it.gender){
                view.editText?.text = Editable.Factory.getInstance().newEditable(it.gender)
            }
        }
        setupGenderExposedDropdownMenu(view)
    }

    @InverseBindingAdapter(attribute = "gender", event = "attrGenderChanged")
    @JvmStatic fun getGender(view: TextInputLayout) : Gender {
        return when(view.editText?.text.toString()){
            Gender.MALE.gender -> Gender.MALE
            Gender.FEMALE.gender -> Gender.FEMALE
            else -> Gender.OTHER
        }
    }

    @BindingAdapter("attrGenderChanged")
    @JvmStatic fun setListener (view: TextInputLayout, attrChange: InverseBindingListener?){
        (view.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            attrChange?.onChange()
        }
    }
}