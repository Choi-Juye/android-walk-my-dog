package com.chloedewyes.walkmydog.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentTrackingBinding
import com.chloedewyes.walkmydog.other.Constants
import com.chloedewyes.walkmydog.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.chloedewyes.walkmydog.other.Constants.ACTION_STOP_SERVICE
import com.chloedewyes.walkmydog.other.Constants.MAP_ZOOM
import com.chloedewyes.walkmydog.service.TrackingService
import com.chloedewyes.walkmydog.service.TrackingUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class TrackingFragment : Fragment(R.layout.fragment_tracking), EasyPermissions.PermissionCallbacks  {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private var map: GoogleMap? = null

    private var isTracking = false

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
            if(TrackingUtility.hasLocationPermissions(requireContext())){
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }else {
                requestPermissions()
            }
        }

        binding.btnStop.setOnClickListener {
            sendCommandToService(ACTION_STOP_SERVICE)
        }

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, {
            updateUI(it)
        })

        TrackingService.trackingLocation.observe(viewLifecycleOwner, { trackingLocation ->
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    trackingLocation,
                    MAP_ZOOM
                )
            )
        })
    }

    private fun updateUI(isTracking: Boolean) {
        this.isTracking = isTracking
        if(isTracking) {
            binding.btnStart.visibility = View.GONE
            binding.clWalkLayout.visibility = View.VISIBLE

        } else {
            binding.btnStart.visibility = View.VISIBLE
            binding.clWalkLayout.visibility = View.GONE
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

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
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