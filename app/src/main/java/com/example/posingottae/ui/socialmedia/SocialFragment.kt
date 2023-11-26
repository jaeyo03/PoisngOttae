package com.example.posingottae.ui.socialmedia

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.databinding.FragmentSocialBinding


class SocialFragment : Fragment() {

private var _binding: FragmentSocialBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val socialViewModel =
            ViewModelProvider(this).get(SocialViewModel::class.java)

    _binding = FragmentSocialBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textSocial
    socialViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}