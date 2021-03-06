package com.example.mountpic

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment


class ScalingFragment : Fragment(R.layout.fragment_scaling) {
    private lateinit var buttonScala: Button
    private lateinit var scalingFactor: EditText

    companion object {
        val TAG = ScalingFragment::class.java.simpleName
        fun newInstance() = ScalingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_scaling, container, false)

        buttonScala = rootView.findViewById(R.id.buttonScaling)
        scalingFactor = rootView.findViewById(R.id.factor)

        buttonScala.setOnClickListener() {
            val kFactor: Double = (scalingFactor.text.toString()).toDouble()
            var image = (context as SecondPageActivity).setPicture
            image = resizePixels(image, kFactor)
            (context as SecondPageActivity).findViewById<ImageView>(R.id.image_view).setImageBitmap(image)
            (context as SecondPageActivity).setPicture = image
        }
        return rootView
    }

    fun resizePixels(data: Bitmap, factor: Double): Bitmap {
        val newWidth = (data.width * factor).toInt()
        val newHeight = (data.height * factor).toInt()
        val bitmapConvert = Bitmap.createBitmap(newWidth, newHeight, data.config)

        for (i in 0 until bitmapConvert.width) {
            for (j in 0 until bitmapConvert.height) {
                bitmapConvert.setPixel(i, j, Color.argb(Color.alpha(0), 0,0,0))
            }
        }

        for (i in 0 until bitmapConvert.width) {
            for (j in 0 until bitmapConvert.height) {
                val scrx = (i/factor).toInt()
                val scry = (j/factor).toInt()
                val p = data.getPixel(scrx, scry)
                val r = Color.red(p)
                val g = Color.green(p)
                val b = Color.blue(p)
                val a = Color.alpha(p)

                bitmapConvert.setPixel(i, j, Color.argb(a, r, g, b))
            }
        }
        return bitmapConvert
    }
}