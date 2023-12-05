package com.example.posingottae.ui.mypage

import android.content.Intent
import android.widget.Button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import com.example.posingottae.databinding.FragmentMypageBinding
import com.example.posingottae.login.Login
import com.google.firebase.auth.FirebaseAuth

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mypageViewModel =
            ViewModelProvider(this).get(MypageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMypage
        mypageViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val logoutButton: Button = binding.logoutBtn
        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Log Out", Toast.LENGTH_SHORT).show()
            // LogInActivity로 이동
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}