package com.example.mountpic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_second.view.*
import kotlin.math.cos
import kotlin.math.sin

class RotationFragment : Fragment(R.layout.fragment_rotation) {
    private lateinit var makeRotation: Button
    private lateinit var rotationDegree: EditText

    companion object {
        val TAG = RotationFragment::class.java.simpleName
        fun newInstance() = RotationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_rotation, container, false)

        makeRotation = rootView.findViewById(R.id.start)
        rotationDegree = rootView.findViewById(R.id.number)

        var photo = (context as SecondPageActivity).setPicture

        makeRotation.setOnClickListener() {
            lateinit var rotatedPhoto: Bitmap
            var angle: String = rotationDegree.text.toString()
            var rotAngle: Double = toRadian(angle.toDouble())
            rotatedPhoto = rotate(photo, rotAngle)
            (context as SecondPageActivity).findViewById<ImageView>(R.id.image_view).setImageBitmap(rotatedPhoto)
            (context as SecondPageActivity).setPicture = rotatedPhoto
        }
        return rootView
    }

    class Pixel(
            var x: Double,
            var y: Double
    )

    fun toRadian(x: Double): Double {
        return x * Math.PI/180.0
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

    fun max(a: Double, b: Double, c: Double, d: Double): Double {
        if(a >= b && a >= c && a >= d) return a
        if(b >= a && b >= c && b >= d) return b
        if(c >= a && c >= b && c >= d) return c
        return d
    }
    fun min(a: Double, b: Double, c: Double, d: Double): Double {
        if(a <= b && a <= c && a <= d) return a
        if(b <= a && b <= c && b <= d) return b
        if(c <= a && c <= b && c <= d) return c
        return d
    }

    fun widthOfRotatedBPicture (OO: Pixel, OY: Pixel, XO: Pixel, XY: Pixel): Double {
        val maxX = max(OO.x, OY.x, XO.x, XY.x)
        val minX = min(OO.x, OY.x, XO.x, XY.x)
        return maxX - minX
    }
    fun heightOfRotatedPicture (OO: Pixel, OY: Pixel, XO: Pixel, XY: Pixel): Double {
        val maxY = max(OO.y, OY.y, XO.y, XY.y)
        val minY = min(OO.y, OY.y, XO.y, XY.y)
        return maxY - minY
    }

    fun round(x: Float): Int {
        if (x - x.toInt() < 0.5)
            return x.toInt()
        else
            return x.toInt() + 1
    }

    fun rotate(pic: Bitmap, angle: Double): Bitmap {
        val rotationMatrix = makeAMatrix(angle)
        val pointOO = findNewCoordinates(Pixel(0.0, 0.0), rotationMatrix)
        val pointOY = findNewCoordinates(Pixel(0.0, pic.height.toDouble()), rotationMatrix)
        val pointXO = findNewCoordinates(Pixel(pic.width.toDouble(), 0.0), rotationMatrix)
        val pointXY = findNewCoordinates(Pixel(pic.width.toDouble(), pic.height.toDouble()), rotationMatrix)

        val widthOfRotatedPic = widthOfRotatedBPicture(pointOO, pointOY, pointXO, pointXY)
        val heightOfRotatedPic = heightOfRotatedPicture(pointOO, pointOY, pointXO, pointXY)

        var rotatedPicture = Bitmap.createBitmap(widthOfRotatedPic.toInt(), heightOfRotatedPic.toInt(), pic.config)
        val dx = min(pointOO.x, pointOY.x, pointXO.x, pointXY.x)
        val dy = min(pointOO.y, pointOY.y, pointXO.y, pointXY.y)

        for (i in 0 until rotatedPicture.width) {
            for (j in 0 until rotatedPicture.height) {
                rotatedPicture.setPixel(i, j, Color.argb(Color.alpha(0), 0,0,0))
            }
        }

        for (i in 0 until pic.width) {
            for (j in 0 until pic.height) {
                val pixel = pic.getPixel(i, j)
                var r = Color.red(pixel)
                var g = Color.green(pixel)
                var b = Color.blue(pixel)
                var a = Color.alpha(pixel)

                val newX = round((i * cos(angle) - j * sin(angle) -dx).toFloat())
                val newY = round((i * sin(angle) + j * cos(angle) -dy).toFloat())
                if (!(newX < 0 || newX >= rotatedPicture.width || newY < 0 || newY >= rotatedPicture.height))
                    rotatedPicture.setPixel(newX, newY, Color.argb(a, r, g, b))
            }
        }
        return rotatedPicture
    }
}



