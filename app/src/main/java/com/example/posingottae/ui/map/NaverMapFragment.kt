package com.example.posingottae.ui.map

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posingottae.R
import com.example.posingottae.databinding.FragmentMapBinding
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons


class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = childFragmentManager
        //네이버지도 객체 생성
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 네이버 지도의 옵션 관련 행동은 모두 여기서 진행
    override fun onMapReady(naverMap: NaverMap) {

        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        val cameraPosition = CameraPosition(
            LatLng(33.38, 126.55),  // 위치 지정
            9.0 // 줌 레벨
        )
        naverMap.cameraPosition = cameraPosition


        //실시간 위치 마킹
        naverMap.addOnLocationChangeListener { location ->
            val currentLocate = Marker()
            currentLocate.icon = MarkerIcons.BLACK
            currentLocate.iconTintColor = Color.RED
            currentLocate.position = LatLng(locationSource.lastLocation!!.latitude,
                locationSource.lastLocation!!.longitude)
            currentLocate.captionText = "현위치"
            currentLocate.map = naverMap
        }



        val bodyChannel = Marker()
        bodyChannel.captionText = "바디채널 공릉역점"
        bodyChannel.position = LatLng(37.6260131, 127.0724217)
        bodyChannel.map = naverMap

        val spoAny = Marker()
        spoAny.captionText = "스포애니 공릉동점"
        spoAny.position = LatLng(37.6270183, 127.07681)
        spoAny.map = naverMap

        val gorillaGym =  Marker()
        gorillaGym.captionText = "고릴라멀티짐 중계점"
        gorillaGym.position = LatLng(37.6483303,127.076905)
        gorillaGym.map =  naverMap

        val hGym = Marker()
        hGym.captionText = "에이치짐 공릉점"
        hGym.position = LatLng(37.6271626,127.0799852)
        hGym.map = naverMap

        val brustarGym = Marker()
        brustarGym.captionText = "부르스타짐 공릉점"
        brustarGym.position = LatLng(37.6219188,127.0752955)
        brustarGym.map = naverMap

        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return "가격: 50,000원/한 달"
            }
        }

        val infoWindow2 = InfoWindow()
        infoWindow2.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow2: InfoWindow): CharSequence {
                return "가격: 40,000원/한 달"
            }
        }

        val infoWindow3 = InfoWindow()
        infoWindow3.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow3: InfoWindow): CharSequence {
                return "가격: 35,000원/한 달"
            }
        }

        val infoWindow4 = InfoWindow()
        infoWindow4.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow4: InfoWindow): CharSequence {
                return "가격: 22,000원/한 달"
            }
        }

        val infoWindow5 = InfoWindow()
        infoWindow5.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow5: InfoWindow): CharSequence {
                return "가격: 48,000원/한 달"
            }
        }



        // 마커를 클릭하면:
        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close()
            }
            true
        }

        val listener2 = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow2.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow2.close()
            }
            true
        }

        val listener3 = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow3.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow3.close()
            }
            true
        }

        val listener4 = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow4.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow4.close()
            }
            true
        }

        val listener5 = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow5.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow5.close()
            }
            true
        }



        bodyChannel.onClickListener = listener
        spoAny.onClickListener = listener2
        brustarGym.onClickListener = listener3
        gorillaGym.onClickListener = listener4
        hGym.onClickListener = listener5

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isLocationButtonEnabled =true
        uiSettings.isScaleBarEnabled = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}


