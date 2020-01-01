package com.eklitstudio.advokatkotilin.ui.user_profile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.eklitstudio.advokatkotilin.R
import kotlinx.android.synthetic.main.user_profile_fragment.*

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pref.apply {
            val ime = getString("IME", "")
            val adresa = getString("ADRESA", "")
            val mesto = getString("MESTO", "")
            user_profile_ime.setText(ime)
            user_profile_adresa.setText(adresa)
            user_profile_mesto.setText(mesto)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)

        fab_user_data.setOnClickListener{
            sacuvajProfil()
        }
    }

   fun sacuvajProfil(){
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = pref.edit()

        editor
            .putString("IME", user_profile_ime.text.toString())
            .putString("ADRESA", user_profile_adresa.text.toString())
            .putString("MESTO", user_profile_mesto.text.toString())
            .apply()

        val toast = Toast.makeText(requireContext(), "Sacuvano", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 140)
        toast.show()
    }

}
