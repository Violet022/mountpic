package com.example.mountpic

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlin.math.floor


class ScalingFragment : Fragment(R.layout.fragment_scaling){

    private lateinit var photoIm: ImageView
    private lateinit var buttonScal: Button

    companion object{
        val TAG = ScalingFragment::class.java.simpleName
        fun newInstance() = ScalingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_rotation, container, false)

        photoIm = rootView.findViewById(R.id.photoToScaling)
        buttonScal = rootView.findViewById(R.id.buttonScaling)

        var photo = (context as SecondPage).fromUriToBitmap()
        photoIm.setImageBitmap(photo)

        buttonScal.setOnClickListener() {

        }
        return rootView
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view: View = inflater.inflate(R.layout.fragment_scaling, container, false)
        val imagePic: ImageView = view.findViewById(R.id.image_view) as ImageView

        val image = (imagePic.drawable as BitmapDrawable).bitmap
        val imageWidth = image.width
        val imageHeight = image.height
        val imagePixels = IntArray(imageWidth * imageHeight)
        image.getPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        return view
    }
}*/

///алгоритхм
fun resizePixels(pixels: IntArray, ratio: Int): IntArray? {
    val temp = IntArray(imageWidth * imageHeight * ratio)
    var px: Double
    var py: Double
    for (i in 0 until imageHeight * ratio) {
        for (j in 0 until imageWidth * ratio) {
            px = floor((j * ratio).toDouble())
            py = floor((i * ratio).toDouble())
            temp[i * imageWidth * ratio + j] = pixels[(py * imageWidth + px).toInt()]
        }
    }
    return temp
}


//план
//1.получить битмап
//2.массив пикселей
//3.ролик коэффициентов со считыванием значения
//4.кнопочка
//5.алгоритм ближайшего соседа