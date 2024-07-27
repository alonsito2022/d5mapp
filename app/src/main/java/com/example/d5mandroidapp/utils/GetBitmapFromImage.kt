package com.example.d5mandroidapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

val displayMetrics = DisplayMetrics()
// on below line we are creating a function to get bitmap
// from image and passing params as context and an int for drawable.
fun getBitmapFromImage(context: Context, drawable: Int): Bitmap {

    // on below line we are getting drawable
    val db = ContextCompat.getDrawable(context, drawable)

    // in below line we are creating our bitmap and initializing it.
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )

    // on below line we are
    // creating a variable for canvas.
    val canvas = Canvas(bit)

    // on below line we are setting bounds for our bitmap.
    db.setBounds(0, 0, canvas.width, canvas.height)

    // on below line we are simply
    // calling draw to draw our canvas.
    db.draw(canvas)

    // on below line we are
    // returning our bitmap.
    return bit
}

@RequiresApi(Build.VERSION_CODES.R)
fun createDrawableFromView(context: Context, markerView: View): BitmapDescriptor {
    val display = (context as Activity).display
    display?.getRealMetrics(displayMetrics)
    markerView.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    markerView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
    markerView.buildDrawingCache()
    val bitmap = Bitmap.createBitmap(
        markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    markerView.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}