package com.example.posingottae.ui.poseanalysis


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.R
import com.example.posingottae.cameraActivity
import com.example.posingottae.databinding.FragmentPoseBinding

class PoseFragment : Fragment() {

private var _binding: FragmentPoseBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View {
        val poseViewModel = ViewModelProvider(this).get(PoseViewModel::class.java)

        _binding = FragmentPoseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPose

        poseViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
        }

        val poses = arrayOf("Choose Pose!" ,"Front","Back","Side","Muscular")
        val poseSpinner : Spinner = binding.choosePoseSpinner
        val poseImageContainer = binding.imageContainer

        poseSpinner.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,poses)
        poseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateImages(poseImageContainer ,poses[position] )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        val goPose = binding.goPose
        goPose.setOnClickListener {
            startActivity(Intent(activity,cameraActivity::class.java))
        }
        return root
    }

    private fun updateImages(container: LinearLayout, category: String) {
        container.removeAllViews() // 기존 이미지 삭제

        val images = getImagesForCategory(category) // 카테고리에 따른 이미지 가져오기

        val imageParmas = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8,0,8,0)
        }

        for (image in images) {
            val imageView = ImageView(requireContext())
            imageView.layoutParams = imageParmas
            imageView.setImageResource(image)
            container.addView(imageView)
        }
    }

    private fun getImagesForCategory(category: String): List<Int> {
        // 여기서 카테고리에 따라 이미지의 리소스 ID 목록을 반환합니다.
        // 예를 들어, 팔 동작 이미지의 리소스 ID를 리스트로 반환
        return when (category) {
            "Front" -> listOf(R.drawable.pose_front_spread, R.drawable.pose_front_double_biceps,R.drawable.pose_line_up,R.drawable.pose_abdominal)
            "Back" -> listOf(R.drawable.pose_back_double, R.drawable.pose_back_lat)
            "Side" -> listOf(R.drawable.pose_side_chest)
            "Muscular" -> listOf(R.drawable.pose_most_muscular,R.drawable.pose_most_muscular2)
            else -> emptyList()
        }
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}