package com.example.posingottae.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.posingottae.PoseRoomDB.PoseDB
import com.example.posingottae.PoseRoomDB.PoseData
import com.example.posingottae.R
import com.example.posingottae.databinding.FragmentMypageBinding
import com.example.posingottae.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var logoutButton: Button
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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

        coroutineScope.launch {
            val poses = withContext(Dispatchers.IO){
                PoseDB.getInstance(requireContext()).poseDataDao().getAll()
            }

            for (pose in poses){
                val tableRow = TableRow(requireContext())
                // PoseName 추가
                val poseNameTextView = TextView(requireContext())
                poseNameTextView.text = pose.poseName
                tableRow.addView(poseNameTextView)

                // UserLeftArm 추가
                val userLeftArmTextView = TextView(requireContext())
                userLeftArmTextView.text= String.format("%.4f",pose.userLeftArm)
                tableRow.addView(userLeftArmTextView)

                // UserRightArm 추가
                val userRightArmTextView = TextView(requireContext())
                userRightArmTextView.text = String.format("%.4f",pose.userRightArm)
                tableRow.addView(userRightArmTextView)

                // UserLeftLeg 추가
                val userLeftLegTextView = TextView(requireContext())
                userLeftLegTextView.text = String.format("%.4f",pose.userLeftLeg)
                tableRow.addView(userLeftLegTextView)

                // UserRightLeg 추가
                val userRightLegTextView = TextView(requireContext())
                userRightLegTextView.text = String.format("%.4f",pose.userRightLeg)
                tableRow.addView(userRightLegTextView)

                // TableLayout에 TableRow 추가
                binding.tablePose.addView(tableRow)
            }

        }



        fetchUserData()

        val logoutButton: Button = binding.logoutBtn
        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Log Out", Toast.LENGTH_SHORT).show()
            // LogInActivity로 이동
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        val editProfileButton: Button = binding.editProfileBtn
        editProfileButton.setOnClickListener {
            // 현재 프래그먼트를 EditProfileFragment로 대체합니다.
            findNavController().navigate(R.id.navigation_edit_profile)
        }




        parentFragmentManager.setFragmentResultListener("EDIT_PROFILE_REQUEST", this) { _, bundle ->
            // 프로필이 업데이트되었는지 확인하고, 업데이트된 경우에만 데이터를 새로고침합니다.
            if (bundle.getBoolean("profileUpdated", false)) {
                fetchUserData()
            }
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