package com.example.posingottae.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.R
import com.example.posingottae.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback


class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = childFragmentManager
        //네이버지도 객체 생성
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMap
        mapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 네이버 지도의 옵션 관련 행동은 모두 여기서 진행
    override fun onMapReady(naverMap: NaverMap) {
        val cameraPosition = CameraPosition(
            LatLng(33.38, 126.55),  // 위치 지정
            9.0 // 줌 레벨
        )
        naverMap.cameraPosition = cameraPosition

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isLocationButtonEnabled =true //위치에서 뭘더 조정해야 제대로 작동..
        uiSettings.isScaleBarEnabled = true
    }
}