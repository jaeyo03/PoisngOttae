package com.example.posingottae.ui.poseanalysis

import android.util.Log
import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.posingottae.ui.poseanalysis.PictureFragment.BlankFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBackdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBacklatFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontAbdFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontTricepsFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontspreadFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment1
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment2
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseSideFragment


class PagerAdapter(fa: PoseFragment, var mCount: Int) : FragmentStateAdapter(fa!!) {
    private var fragmentItems : List<Fragment> = listOf()
    fun setFragments(pose: String){
        fragmentItems = when(pose){
            "Front" -> listOf(PoseFrontAbdFragment(),PoseFrontFragment(),PoseFrontdbFragment(),PoseFrontspreadFragment(),
                PoseFrontTricepsFragment())
            "Muscular" -> listOf(PoseMusFragment1(),PoseMusFragment2())
            "Side" -> listOf(PoseSideFragment())
            "Back" -> listOf(PoseBackdbFragment(),PoseBacklatFragment())
            else -> listOf(BlankFragment())
        }
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentItems[position]
    }

    override fun getItemCount(): Int {
        return fragmentItems.size
    }

}