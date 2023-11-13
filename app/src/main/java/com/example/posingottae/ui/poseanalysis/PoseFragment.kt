package com.example.posingottae.ui.poseanalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.databinding.FragmentPoseBinding

class PoseFragment : Fragment() {

private var _binding: FragmentPoseBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
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
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}