package com.yterletskyi.happyfriend.common.x

import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun Fragment.requestContactsPermission(
    onGranted: () -> Unit,
    onDeclined: () -> Unit = {
        Toast.makeText(context, "Please grant permission", Toast.LENGTH_SHORT).show()
    },
) {
    val permission = ActivityResultContracts.RequestPermission()
    registerForActivityResult(permission) {
        if (it) {
            onGranted()
        } else {
            onDeclined()
        }
    }.launch(Manifest.permission.READ_CONTACTS)
}