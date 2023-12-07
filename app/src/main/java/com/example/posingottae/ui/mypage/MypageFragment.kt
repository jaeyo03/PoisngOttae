package com.example.posingottae.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.R
import com.example.posingottae.databinding.FragmentMypageBinding
import com.example.posingottae.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        textViewUsername = view.findViewById(R.id.textViewUsername)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        logoutButton = view.findViewById(R.id.logoutBtn)

        fetchUserData()

        val logoutButton: Button = binding.logoutBtn
        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Log Out", Toast.LENGTH_SHORT).show()
            // LogInActivity로 이동
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun fetchUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            // Firestore에서 사용자 데이터 가져오기
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(user.uid)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val username = document.getString("username")
                        val email = user.email

                        // 가져온 정보를 화면에 표시
                        textViewUsername.text = "Username: $username"
                        textViewEmail.text = "Email: $email"
                    } else {
                        // 사용자 문서가 없는 경우 처리
                    }
                }
                .addOnFailureListener { exception ->
                    // 데이터 가져오기 실패 처리
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}