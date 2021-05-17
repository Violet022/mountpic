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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_second.*
import kotlin.math.cos
import kotlin.math.sin

class RotationFragment : Fragment(R.layout.fragment_rotation) {
    private lateinit var photoPlace: ImageView
    private lateinit var makeRotation: Button
    private lateinit var rotationDegree: EditText

    companion object {
        val TAG = RotationFragment::class.java.simpleName
        fun newInstance() = RotationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_rotation, container, false)

        photoPlace = rootView.findViewById(R.id.photoToRotate)
        makeRotation = rootView.findViewById(R.id.start)
        rotationDegree = rootView.findViewById(R.id.number)

        var photo = (context as SecondPageActivity).fromUriToBitmap()
        photoPlace.setImageBitmap(photo)

        makeRotation.setOnClickListener() {
            lateinit var rotatedPhoto: Bitmap
            var angle: String = rotationDegree.text.toString()
            var rotAngle: Double = toRadian(angle.toDouble())
            rotatedPhoto = rotate(photo, rotAngle)
            photoPlace.setImageBitmap(rotatedPhoto)
        }
        return rootView
    }

    fun toRadian(x : Double): Double {
        return x * Math.PI/180.0
    }

    class Pixel(
            var x : Double,
            var y : Double
    )
    fun max(a : Double, b : Double, c : Double, d : Double): Double {
        if(a >= b && a >= c && a >= d) return a
        if(b >= a && b >= c && b >= d) return b
        if(c >= a && c >= b && c >= d) return c
        return d
    }
    fun min(a : Double, b : Double, c : Double, d : Double): Double {
        if(a <= b && a <= c && a <= d) return a
        if(b <= a && b <= c && b <= d) return b
        if(c <= a && c <= b && c <= d) return c
        return d
    }

    fun round(x : Float) : Int {
        if(x - x.toInt() < 0.5)
            return x.toInt()
        else
            return x.toInt() + 1
    }

    fun makeAMatrix(angle: Double): Array<DoubleArray> {
        val matrix: Array<DoubleArray> = Array(2) {DoubleArray(2) }
        matrix[0] = doubleArrayOf(cos(angle), -sin(angle))
        matrix[1] = doubleArrayOf(sin(angle), cos(angle))
        return matrix
    }

    fun findNewCoordinates(curPixel: Pixel, rotationMatrix: Array<DoubleArray>): Pixel {
        var newPixel: Pixel
        var lastCoordinates: Array<Double> = arrayOf(curPixel.x, curPixel.y)
        var newCoordinates: Array<Double> = Array(2) {0.0}
        for (i in 0..1) {
            for (j in 0..1) {
                newCoordinates[i] += rotationMatrix[i][j] * lastCoordinates[j]
            }
        }
        newPixel = Pixel(newCoordinates[0], newCoordinates[1])
        return newPixel
    }

    fun rotate(data : Bitmap, angle : Double): Bitmap {
        val rotationMatrix = makeAMatrix(angle)
        val pointOO = Pixel(0.0, 0.0)
        val pointOY = findNewCoordinates(Pixel(0.0, data.height.toDouble()), rotationMatrix)
        val pointXO = findNewCoordinates(Pixel(data.width.toDouble(), 0.0), rotationMatrix)
        val pointXY = findNewCoordinates(Pixel(data.width.toDouble(), data.height.toDouble()), rotationMatrix)

        val maxX = max(pointOO.x, pointOY.x, pointXO.x, pointXY.x)
        val minX = min(pointOO.x, pointOY.x, pointXO.x, pointXY.x)
        val sizeOfNewX =maxX - minX
        //val sizeOfNewX = max(pointOO.x, pointOY.x, pointXO.x, pointXY.x)
        val sizeOfNewY = max(pointOO.y, pointOY.y, pointXO.y, pointXY.y)
        var bitmapConvert = Bitmap.createBitmap(sizeOfNewX.toInt(), sizeOfNewY.toInt(), data.config)
        val dx = min(pointOO.x, pointOY.x, pointXO.x, pointXY.x)
        val dy = min(pointOO.y, pointOY.y, pointXO.y, pointXY.y)

        for(i in 0 until bitmapConvert.width){
            for(j in 0 until bitmapConvert.height){
                bitmapConvert.setPixel(i, j, Color.argb(Color.alpha(0), 0,0,0))
            }
        }
        for(i in 0 until data.width){
            for(j in 0 until data.height){
                val p = data.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                val newX = round((i * cos(angle) - j * sin(angle) -dx).toFloat())
                val newY = round((i * sin(angle) + j * cos(angle) -dy).toFloat())
                if(!(newX < 0 || newX >= bitmapConvert.width || newY < 0 || newX >= bitmapConvert.height))
                    bitmapConvert.setPixel(newX, newY, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        return bitmapConvert
    }
}



















































































































































    /*

    fun max(a : Double, b : Double, c : Double, d : Double): Double {
        if(a >= b && a >= c && a >= d) return a
        if(b >= a && b >= c && b >= d) return b
        if(c >= a && c >= b && c >= d) return c
        return d
    }
    fun min(a : Double, b : Double, c : Double, d : Double): Double {
        if(a <= b && a <= c && a <= d) return a
        if(b <= a && b <= c && b <= d) return b
        if(c <= a && c <= b && c <= d) return c
        return d
    }

    fun round(x : Float) : Int {
        if(x - x.toInt() < 0.5)
            return x.toInt()
        else
            return x.toInt() + 1
    }

    fun rotate(data : Bitmap, angle : Double): Bitmap {
        val cornerOO = Point(0.0, 0.0)
        val cornerOY = Point(-data.height * sin(angle), data.height * cos(angle))
        val cornerXO = Point(data.width * cos(angle), data.width * sin(angle))
        val cornerXY = Point(data.width * cos(angle) - data.height * sin(angle), data.width * sin(angle) + data.height * cos(angle))

        val sizeOfNewX = max(cornerOO.x, cornerOY.x, cornerXO.x, cornerXY.x)
        val sizeOfNewY = max(cornerOO.y, cornerOY.y, cornerXO.y, cornerXY.y)
        var bitmapConvert = Bitmap.createBitmap(sizeOfNewX.toInt(), sizeOfNewY.toInt(), data.config)
        val dx = min(cornerOO.x, cornerOY.x, cornerXO.x, cornerXY.x)
        val dy = min(cornerOO.y, cornerOY.y, cornerXO.y, cornerXY.y)

        for(i in 0 until bitmapConvert.width){
            for(j in 0 until bitmapConvert.height){
                bitmapConvert.setPixel(i, j, Color.argb(Color.alpha(0), 0,0,0))
            }
        }
        for(i in 0 until data.width){
            for(j in 0 until data.height){
                val p = data.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                val newX = round((i * cos(angle) - j * sin(angle) -dx).toFloat())
                val newY = round((i * sin(angle) + j * cos(angle) -dy).toFloat())
                if(!(newX < 0 || newX >= bitmapConvert.width || newY < 0 || newX >= bitmapConvert.height))
                    bitmapConvert.setPixel(newX, newY, Color.argb(Color.alpha(p), r, g, b))
            }
        }

        return bitmapConvert
    }

}*/