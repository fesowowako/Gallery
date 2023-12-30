package app.android.gallery.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Activity.checkPermissions(permissions: Array<String>): Boolean {
    if (permissions.isEmpty()) return true
    if (!hasPermission(permissions.first())) {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            1
        )
        return false
    }
    return true
}

fun Context.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
