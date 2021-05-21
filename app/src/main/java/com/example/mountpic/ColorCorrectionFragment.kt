package com.example.mountpic

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

public class ColorCorrectionFragment : Fragment(R.layout.fragment_color_correction){

    companion object{
        val TAG = ColorCorrectionFragment::class.java.simpleName
        fun newInstance() = ColorCorrectionFragment()
    }

    private var HIGHEST_COLOR_VALUE = 255
    private var LOWEST_COLOR_VALUE = 0
    private val storagePermission1: Array<String> = kotlin.arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val storagePermission2: Array<String> = kotlin.arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_color_correction, container, false)

        lateinit var btnNegative: Button
        lateinit var btnGrey: Button
        lateinit var btnSketch: Button
        lateinit var btnSave: Button

        btnNegative = view.findViewById(R.id.buttonNegative)
        btnGrey = view.findViewById(R.id.buttonGrey)
        btnSketch = view.findViewById(R.id.buttonSketch)
        btnSave = view.findViewById(R.id.buttonSave)

        //lateinit var image: ImageView
        lateinit var image: Bitmap
        lateinit var bitMap: Bitmap
        lateinit var newBitMap: Bitmap

        btnNegative.setOnClickListener(){
            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!
            bitMap = (context as SecondPageActivity).setPicture
            newBitMap = setNegativeFilter(bitMap)
            (context as SecondPageActivity).findViewById<ImageView>(R.id.image_view).setImageBitmap(newBitMap)
            //setNegativeFilter(bitMap)

            //newBitMap = setNegativeFilter(bitMap)
        }

        /*btnGrey.setOnClickListener(){

            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!
            //bitMap = (context as SecondPageActivity).setPicture
            //setGreyFilter(bitMap)

            //newBitMap = setGreyFilter(bitMap)
        }*/

        /*btnSketch.setOnClickListener(){

            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!

            newBitMap = setSketchFilter(bitMap)

        }*/

        //ActivityCompat.requestPermissions(context as SecondPageActivity, storagePermission1, 1)
        //ActivityCompat.requestPermissions(context as SecondPageActivity, storagePermission2, 1)

        /*btnSave.setOnClickListener() {
            saveToGallery(newBitMap)

        }*/

        return view
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var btnNegative: Button
        lateinit var btnGrey: Button
        lateinit var btnSketch: Button
        lateinit var btnSave: Button

        btnNegative = view.findViewById(R.id.buttonNegative)
        btnGrey = view.findViewById(R.id.buttonGrey)
        btnSketch = view.findViewById(R.id.buttonSketch)
        btnSave = view.findViewById(R.id.buttonSave)

        //lateinit var image: ImageView
        lateinit var image: Bitmap
        lateinit var bitMap: Bitmap
        lateinit var newBitMap: Bitmap

        btnNegative.setOnClickListener(){

            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!
            image = (context as SecondPageActivity).setPicture
            newBitMap = setNegativeFilter(image)

            //newBitMap = setNegativeFilter(bitMap)
        }

        btnGrey.setOnClickListener(){

            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!
            bitMap = (context as SecondPageActivity).setPicture
            setGreyFilter(bitMap)

            //newBitMap = setGreyFilter(bitMap)
        }

        btnSketch.setOnClickListener(){

            //image = view.findViewById(R.id.image_view)

            //bitMap = (context as SecondPageActivity).fromImageToBitmap(image)!!

            newBitMap = setSketchFilter(bitMap)

        }

        ActivityCompat.requestPermissions(context as SecondPageActivity, storagePermission1, 1)
        ActivityCompat.requestPermissions(context as SecondPageActivity, storagePermission2, 1)

        btnSave.setOnClickListener() {
            saveToGallery(newBitMap)

        }

}*/
    /*private fun saveToGallery(bitmap: Bitmap){
        var outputStream: FileOutputStream? = null
        val file: File = Environment.getExternalStorageDirectory()
        val dir = File(file.absolutePath + "/MyPics")
        dir.mkdirs()

        val filename: String = String.format("%d.png", System.currentTimeMillis())
        val outFile = File(dir, filename)

        try{
            outputStream = FileOutputStream(outFile)
        }

        catch (e: Exception){
            e.printStackTrace()
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        try{
            outputStream?.flush()
        }

        catch (e: Exception){
            e.printStackTrace()
        }

        try{
            outputStream?.close()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    public fun setGreyFilter(oldBitmap: Bitmap) {
        //var newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true)

        //var imageHeight = newBitmap.height
        //var imageWidth = newBitmap.width
        var imageHeight = oldBitmap.height
        var imageWidth = oldBitmap.width

        for(i in 0..imageWidth){

            for(j in 0..imageHeight){

                var oldPixel = oldBitmap.getPixel(i, j)

                var oldRed = Color.red(oldPixel)
                var oldGreen = Color.green(oldPixel)
                var oldBlue = Color.blue(oldPixel)
                var oldAlpha = Color.alpha(oldPixel)


                var intensity = (oldRed + oldGreen + oldBlue) / 3

                var newRed = intensity
                var newGreen = intensity
                var newBlue = intensity


                var newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue)
                oldBitmap.setPixel(i, j, newPixel)

            }
        }

        //return newBitmap
    }*/


    fun setNegativeFilter(oldBitmap: Bitmap): Bitmap{

        //var newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true)
        var newBitmap = Bitmap.createBitmap(oldBitmap.width, oldBitmap.height, oldBitmap.config)

        var imageHeight = newBitmap.height
        var imageWidth = newBitmap.width
        //var imageHeight = oldBitmap.height
        //var imageWidth = oldBitmap.width

        for(i in 0..imageWidth){
            for(j in 0..imageHeight){

                var oldPixel = oldBitmap.getPixel(i, j)

                var oldRed = Color.red(oldPixel)
                var oldGreen = Color.green(oldPixel)
                var oldBlue = Color.blue(oldPixel)

                var newRed = HIGHEST_COLOR_VALUE - oldRed
                var newGreen = HIGHEST_COLOR_VALUE - oldGreen
                var newBlue = HIGHEST_COLOR_VALUE - oldBlue
                var alpha = Color.alpha(oldPixel)

                //var newPixel = Color.argb(alpha, newRed, newGreen, newBlue)
                //newBitmap.setPixel(i, j, newPixel)
                newBitmap.setPixel(i, j, Color.argb(alpha, newRed, newGreen, newBlue))

            }
        }
        return newBitmap
    }


    /*public fun setSketchFilter(oldBitmap: Bitmap): Bitmap {

        var newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true)

        var imageHeight = newBitmap.height
        var imageWidth = newBitmap.width

        for(i in 0..imageWidth){

            for(j in 0..imageHeight){

                var oldPixel = oldBitmap.getPixel(i, j)

                var oldRed = Color.red(oldPixel)
                var oldGreen = Color.green(oldPixel)
                var oldBlue = Color.blue(oldPixel)
                var oldAlpha = Color.alpha(oldPixel)

                var intensity = (oldRed + oldGreen + oldBlue) / 3

                var newPixel = 0
                val INTENSITY_FACTOR = 120

                if (intensity > INTENSITY_FACTOR){
                    newPixel = Color.argb(oldAlpha, HIGHEST_COLOR_VALUE, HIGHEST_COLOR_VALUE, HIGHEST_COLOR_VALUE)
                }
                else if (intensity > 70){
                    newPixel = Color.argb(oldAlpha, 150, 150, 150)
                }
                else{
                    newPixel = Color.argb(oldAlpha, LOWEST_COLOR_VALUE, LOWEST_COLOR_VALUE, LOWEST_COLOR_VALUE)
                }

                newBitmap.setPixel(i, j, newPixel)

            }
        }

        return newBitmap
    }*/
}

