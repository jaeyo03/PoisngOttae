package com.example.posingottae.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.posingottae.R
import com.example.posingottae.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // 이 코드를 추가
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭 시 현재 Fragment를 닫고 이전 Fragment로 돌아감
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val btnSave = binding.btnSave
        val editTextUsername = binding.editTextUsername
//        val editTextEmail = binding.editTextEmail


        btnSave.setOnClickListener {
            // Firestore에서 사용자 데이터를 업데이트합니다.
            currentUser?.let { user ->
                val updatedUsername = editTextUsername.text.toString()
//                val updatedEmail = editTextEmail.text.toString()

                val db = FirebaseFirestore.getInstance()
                val userRef = db.collection("users").document(user.uid)

                userRef.update(mapOf(
                    "username" to updatedUsername,
//                    "email" to updatedEmail
                )).addOnSuccessListener {
                    // EditProfileFragment에서 결과를 반환합니다.
                    setFragmentResult("EDIT_PROFILE_REQUEST", Bundle().apply {
                        putBoolean("profileUpdated", true)
                    })
                    requireActivity().supportFragmentManager.popBackStack()

                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
