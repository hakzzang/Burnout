package com.hbs.burnout.ui.camera.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hbs.burnout.R
import com.hbs.burnout.utils.ActivityNavigation

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

/**
 * The sole purpose of this fragment is to request permissions and, once granted, display the
 * camera fragment to the user.
 */
class PermissionsFragment : Fragment() {

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Navigation.findNavController(requireActivity(), R.id.fragment_camera_container).navigate(
                    PermissionsFragmentDirections.actionPermissionsToCamera())
            } else {
                Toast.makeText(requireContext(),"카메라 권한이 없으므로 이번 미션을 수행하지 못해요.",Toast.LENGTH_SHORT).show()
                requireActivity().setResult(ActivityNavigation.CAMERA_TO_CHATTING)
            }
        }

    override fun onResume() {
        super.onResume()
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Navigation.findNavController(requireActivity(), R.id.fragment_camera_container).navigate(
                    PermissionsFragmentDirections.actionPermissionsToCamera())
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(),"카메라 권한이 없으면 이번 미션을 수행하지 못해요. 꼭 확인해주세요~",Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
        }
    }

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
