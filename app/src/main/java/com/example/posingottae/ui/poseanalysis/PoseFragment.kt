package com.example.posingottae.ui.poseanalysis


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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

        // ViewPage Adapter를 먼저 설정
        val pageNum = 4
        val myPager = binding.poseViewPager
        val pagerAdapter = PagerAdapter(this,pageNum)
        myPager.adapter = pagerAdapter

        val poses = arrayOf("Choose Pose!" ,"Front","Back","Side","Muscular")
        val poseSpinner : Spinner = binding.choosePoseSpinner


        poseSpinner.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,poses)
        poseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choosedPose = poses[position]
                val newPagerAdapter = PagerAdapter(this@PoseFragment, pageNum)
                newPagerAdapter.setFragments(choosedPose)
                myPager.adapter = newPagerAdapter

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


        // Indicator 설정 , 동그라미 생기는거
        val rIndicator = binding.indicatorRound
        rIndicator.setViewPager(myPager)
        rIndicator.createIndicators(pageNum,0)

        myPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        myPager.currentItem = 0 // 시작 지점
        myPager.offscreenPageLimit = 4 // 최대 이미지 수

        var fragmentInfo = ""

        myPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffsetPixels == 0) {
                    myPager.currentItem = position
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                rIndicator.animatePageSelected(position % pageNum)
            }
        })

        val goPose = binding.goPose
        goPose.setOnClickListener {
            val intent = Intent(activity,cameraActivity::class.java)
            intent.putExtra("selectedPhotoID",fragmentInfo)
            startActivity(intent)
        }
        return root
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}



