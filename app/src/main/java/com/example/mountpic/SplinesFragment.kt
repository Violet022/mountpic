package com.example.mountpic

import android.Manifest
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mountpic.R.id.canvasZone
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.properties.Delegates


public class SplinesFragment : Fragment(R.layout.fragment_splines){

    var coordinates: MutableList<Point> = mutableListOf()

    companion object{
        val TAG = SplinesFragment::class.java.simpleName
        fun newInstance() = SplinesFragment()

    }


    private val storagePermission1: Array<String> = kotlin.arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val storagePermission2: Array<String> = kotlin.arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        lateinit var btnSet: Button

        btnSet = view?.findViewById(R.id.buttonSpline)!!


        btnSet.setOnClickListener {
            fromPolylineToSpline(coordinates)

        }


        val rootView: View = inflater.inflate(R.layout.fragment_splines, container, false)
        val relativeLayout = rootView.findViewById<View>(canvasZone) as RelativeLayout
        relativeLayout.addView(DrawingView(activity))
        return rootView
    }


    fun fromPolylineToSpline(coordinates: MutableList<Point>): Array<Float>{

        val numberOfVertexes = coordinates.size
        val numberOfSplines = numberOfVertexes - 1
        val lastIndex = 4 * numberOfVertexes - 4
        var matrix: Array<Array<Float>> = Array(4 * numberOfVertexes - 4, {Array(lastIndex + 1, {0F})})


        //инициализация матрицы

        var degree = 1
        //условие 1 - сплайны проходят через узловые точки
        for(i in 0 until numberOfSplines){
            matrix[2 * i][4 * i] = 1F
            val delta = coordinates[i + 1].x - coordinates[i].x
            for(j in 4*i..(4*i + 4)) {
                matrix[2 * i + 1][j] = delta.pow(degree)
                degree++
            }
            degree = 1

            //свободный член
            matrix[2 * i][lastIndex] = coordinates[i].y
            matrix[2 * i + 1][lastIndex] = coordinates[i + 1].y

        }

        var alreadyInMatrix = 2 * numberOfVertexes - 2

        //условие 2.1 - первые производные сплайнов в точках стыковки равны
        var j = 1
        for(i in 0 until numberOfSplines){

            matrix[alreadyInMatrix + i][j] = 1F
            val delta = coordinates[i + 1].x - coordinates[i].x
            for(k in 1..2)
                matrix[alreadyInMatrix + i][j + k] = (1 + k) * delta.pow(k)
            j += 4
            matrix[alreadyInMatrix + i][j] = -1F

        }

        alreadyInMatrix += numberOfVertexes - 2
        j = 2

        //условие 2.2 - вторые производные сплайнов в точках стыковки равны
        for(i in 0 until numberOfSplines){

            matrix[alreadyInMatrix + i][j] = 2F
            val delta = coordinates[i + 1].x - coordinates[i].x
            matrix[alreadyInMatrix + i][j + 1] = (6) * delta
            j += 4
            matrix[alreadyInMatrix + i][j] = -2F

        }

        alreadyInMatrix += numberOfVertexes - 2

        //условие 3 - вторые производные в крайних точках равны нулю
        matrix[alreadyInMatrix][2] = 1F
        matrix[alreadyInMatrix + 1][lastIndex - 2] = 2F
        matrix[alreadyInMatrix + 1][lastIndex - 1] = coordinates[numberOfVertexes - 1].x - coordinates[numberOfVertexes - 2].x


        //конец инициализации матрицы


        //решение системы линейных уравнений с помощью метода Гаусса

        //Прямой ход, приведение к верхнетреугольному виду
        val numberOfUnknowns = 4 * numberOfVertexes - 3
        val numberOfEquations = 4 * numberOfVertexes - 4

        var tmp: Float
        var answer = Array(numberOfUnknowns, {0F})
        var k: Int


        for (i in 0 until numberOfEquations)
        {
            j = numberOfEquations
            tmp = matrix[i][i];
            while(j >= i) {
                matrix[i][j] /= tmp
                j--
            }

            for (l in (i+1) until numberOfEquations)
            {
                tmp = matrix[l][i];

                k = numberOfEquations
                while(k >= i){
                    matrix[j][k] -= tmp * matrix[i][k]
                    k--
                }

            }
        }

        //обратный ход
        answer[numberOfEquations - 1] = matrix[numberOfEquations - 1][numberOfEquations]

        j = numberOfEquations - 2
        while(j >= 0){
            answer[j] = matrix[j][numberOfEquations];
            for (i in (j+1) until numberOfEquations)
                answer[j] -= matrix[j][i] * answer[i]
            j--
        }

        return answer
    }



    class Point(coordinate_x: Float, coordinate_y: Float){
        val x = coordinate_x
        val y = coordinate_y
    }

    class DrawingView(context: Context?) : View(context) {

        lateinit var drawPath: Path
        lateinit var drawPaint: Paint
        lateinit var canvasPaint: Paint
        lateinit var drawCanvas: Canvas
        var paintColor = Color.RED
        lateinit var canvasBitmap: Bitmap



        override fun onDraw(canvas: Canvas){
            setupDrawing()
            canvas.drawBitmap(canvasBitmap, 0F, 0F, canvasPaint)
            canvas.drawPath(drawPath, drawPaint)

        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val touchX: Float = event.getX()
            val touchY: Float = event.getY()
            val point = Point(touchX, touchY)

            when(event.action){

                MotionEvent.ACTION_DOWN -> drawPath.moveTo(touchX, touchY)
                MotionEvent.ACTION_UP -> {
                    drawCanvas.drawPath(drawPath, drawPaint)
                    SplinesFragment().coordinates.add(point)
                }
                else -> {
                    return false
                }

            }
            invalidate()
            return true
        }



        private fun setupDrawing(){
            drawPath = Path()
            drawPaint = Paint()
            drawPaint.color = paintColor
            drawPaint.strokeWidth = 5F
            drawPaint.style = Paint.Style.STROKE
            drawPaint.strokeJoin = Paint.Join.ROUND
            drawPaint.strokeCap = Paint.Cap.ROUND
            canvasPaint = Paint(Paint.DITHER_FLAG)


        }

    }


}


