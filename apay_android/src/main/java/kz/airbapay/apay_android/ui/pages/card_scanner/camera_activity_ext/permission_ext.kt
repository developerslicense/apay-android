package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity

internal fun allPermissionsGranted(grantResults: IntArray): Boolean {
    for (result in grantResults) {
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

internal fun CameraActivity.hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

internal fun CameraActivity.requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(
                this,
                "Camera permission is required for this demo",
                Toast.LENGTH_LONG
            )
                .show()
        }
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CameraActivity.PERMISSIONS_REQUEST)
    }
}