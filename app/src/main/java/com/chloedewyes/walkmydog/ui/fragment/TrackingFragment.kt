package com.chloedewyes.walkmydog.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentTrackingBinding
import com.chloedewyes.walkmydog.db.Walk
import com.chloedewyes.walkmydog.other.Constants
import com.chloedewyes.walkmydog.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.chloedewyes.walkmydog.other.Constants.ACTION_STOP_SERVICE
import com.chloedewyes.walkmydog.other.Constants.MAP_ZOOM
import com.chloedewyes.walkmydog.other.Constants.POLYLINE_COLOR
import com.chloedewyes.walkmydog.other.Constants.POLYLINE_WIDTH
import com.chloedewyes.walkmydog.service.Polyline
import com.chloedewyes.walkmydog.service.TrackingService
import com.chloedewyes.walkmydog.util.TrackingUtility
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.text.SimpleDateFormat
import java.util.*


class TrackingFragment : Fragment(R.layout.fragment_tracking), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    private var map: GoogleMap? = null

    private var isTracking = false

    private var pathPoints = mutableListOf<Polyline>()
    private var curTimeInMillis = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { googleMap ->
            map = googleMap
        }

        binding.btnStart.setOnClickListener {
            if (TrackingUtility.hasLocationPermissions(requireContext())) {
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            } else {
                requestPermissions()
            }
        }

        binding.btnStop.setOnClickListener {
            binding.btnStop.visibility = View.GONE
            binding.tvTimer.visibility = View.GONE
            binding.btnSave.visibility = View.VISIBLE

            sendCommandToService(ACTION_STOP_SERVICE)
        }

        binding.btnSave.setOnClickListener {
            saveToDb()
            binding.clWalkLayout.visibility = View.GONE
            binding.btnSave.visibility = View.GONE
            binding.tvTimer.visibility = View.VISIBLE
            binding.btnStart.visibility = View.VISIBLE
        }

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, {
            updateUI(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis)
            binding.tvTimer.text = formattedTime
        })
    }

    private fun saveToDb() {
        map?.snapshot { bitmap ->

            if (bitmap != null) {
                viewModel.insertMap(bitmap)
            }

            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            val dataTimestamp = dateFormat.format(Calendar.getInstance().timeInMillis)

            val walk = Walk(bitmap.toString(), dataTimestamp)

            viewModel.insertWalk(walk)

        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }


    private fun zoomToSeeWholeTrack(): Boolean {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (point in polyline) {
                bounds.include(point)
            }
        }
        val width = binding.mapView.width
        val height = binding.mapView.height

        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )

        return true
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun updateUI(isTracking: Boolean) {
        this.isTracking = isTracking
        if (isTracking) {
            binding.btnStart.visibility = View.GONE
            binding.clWalkLayout.visibility = View.VISIBLE

        } else {
            zoomToSeeWholeTrack()
        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) { // 권한이 있는 경우에는 권한을 요청하지 않습니다.
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "'어야가자' 이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "'어야가자' 이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }


    private fun sendCommandToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also { Intent ->
            Intent.action = action
            requireContext().startService(Intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


}