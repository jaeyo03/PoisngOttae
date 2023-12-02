package com.example.posingottae.ui.socialmedia

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import com.example.posingottae.R



class SocialFragment : Fragment() {
    private lateinit var usernameTextView: TextView
    private lateinit var editText: EditText
    private lateinit var btn_submit: Button
    private lateinit var quitImageButton: ImageButton

    fun setUsernameText(username: String){
        if(::usernameTextView.isInitialized){
            usernameTextView.text = username
        }else{

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_social, container, false)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        editText = view.findViewById(R.id.edt_message)
        btn_submit = view.findViewById(R.id.btn_submit)
        quitImageButton = view.findViewById(R.id.quitImageBtn)

        quitImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments?.let{
            val username = it.getString(USERNAME_KEY)
            username?.let{
                usernameTextView.text = it
            }
        }
        return view
    }

    fun updateUsername(username: String){
        if(::usernameTextView.isInitialized){
            usernameTextView.text = username
        }else{

        }
    }

    companion object{
        const val USERNAME_KEY = "username"

        fun newInstance(username: String): SocialFragment {
            val fragment = SocialFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME_KEY, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}