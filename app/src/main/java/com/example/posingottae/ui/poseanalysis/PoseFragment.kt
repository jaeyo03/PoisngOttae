package com.example.posingottae.ui.poseanalysis


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val poseViewModel =
                ViewModelProvider(this).get(PoseViewModel::class.java)

        _binding = FragmentPoseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPose
        poseViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
        }

        val btn2 = binding.goPose
        btn2.setOnClickListener {
            startActivity(Intent(activity,cameraActivity::class.java))
        }
        return root
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}