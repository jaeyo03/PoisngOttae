package com.example.posingottae.ui.poseanalysis.PictureFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap

import com.example.posingottae.R


class PoseFrontAbdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.pose_front_abdominal, container, false)
        val image = view.findViewById<ImageView>(R.id.frontAbd)
        val drawable = image.drawable.toBitmap()

        image.setOnClickListener{
            Log.d("ITM",drawable.toString())
        }
        return view
    }

}