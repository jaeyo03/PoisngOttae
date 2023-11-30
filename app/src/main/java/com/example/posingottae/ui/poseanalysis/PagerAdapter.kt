package com.example.posingottae.ui.poseanalysis

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.posingottae.ui.poseanalysis.PictureFragment.BlankFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBackdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBacklatFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontAbdFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontspreadFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment1
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment2
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseSideFragment


class PagerAdapter(fa: PoseFragment, var mCount: Int) : FragmentStateAdapter(fa!!) {
    private var selectedPose =""
    private var fragmentItems : List<Fragment> = listOf()

    fun setFragments(pose: String){
        fragmentItems = when(pose){
            "Front" -> listOf(PoseFrontAbdFragment(),PoseFrontFragment(),PoseFrontdbFragment(),PoseFrontspreadFragment())
            "Muscular" -> listOf(PoseMusFragment1(),PoseMusFragment2())
            "Side" -> listOf(PoseSideFragment())
            "Back" -> listOf(PoseBackdbFragment(),PoseBacklatFragment())
            else -> listOf(BlankFragment())
        }
        notifyDataSetChanged()
    }
    fun setSelectedPose(pose:String){
        selectedPose =pose
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
//        val index = getRealPosition(position)
        return fragmentItems[position]
    }

    override fun getItemCount(): Int {
        return fragmentItems.size
    }

//    private fun getRealPosition(position: Int): Int {
//        return position % mCount
//    }
}