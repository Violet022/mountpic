package com.example.mainpage

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 1
    private val STORAGE_REQUEST_CODE = 2
    private val IMAGE_CAMERA_CODE = 3

    private lateinit var btnCamera: Button

    val cameraPermissions: Array<String> = arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCamera = findViewById(R.id.camera_btn)

        btnCamera.setOnClickListener {
            if(checkCameraPermission()){
                makeAPhoto()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean{
        val cameraResult = (ContextCompat.checkSelfPermission(this, cameraPermissions[0])
                == PackageManager.PERMISSION_GRANTED)
        val storageResult = (ContextCompat.checkSelfPermission(this, cameraPermissions[1])
                == PackageManager.PERMISSION_GRANTED)
        return cameraResult && storageResult
    }

    private fun requestCameraPermission(){
        var denyInPast: Boolean = false

        for(permission in cameraPermissions){
            if(shouldShowRequestPermissionRationale(permission)){
                showDialog(permission)
                denyInPast = true
            }
        }
        if(!denyInPast) {
            ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
        }
    }

    private fun showDialog(permission: String){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            when (permission){
                cameraPermissions[0] -> {
                    setMessage("Предоставьте [название] доступ к камере на вашем устройстве для возможности создать изображение.")
                    setTitle("Нет доступа к камере")
                }
                cameraPermissions[1] -> {
                    setMessage("Предоставьте [название] доступ к хранилищу на вашем устройстве для возможности использования существующих изображений и сохранения отредактированных.")
                    setTitle("Нет доступа к хранилищу")
                }
            }
            setPositiveButton("ОК"){dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, cameraPermissions, CAMERA_REQUEST_CODE)
            }
            setNeutralButton("Отмена"){dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if(cameraAccepted && storageAccepted){
                        makeAPhoto()
                    }
                }
            }
        }
    }

    private fun makeAPhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, IMAGE_CAMERA_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            val thumbnailBitmap = data?.extras?.get("data") as Bitmap
            setThePhoto(thumbnailBitmap)
            //photo.setImageBitmap(thumbnailBitmap)
        }
    }

    private fun setThePhoto(pic: Bitmap){
        val setPictureIntent = Intent(this, App::class.java)
        setPictureIntent.putExtra("BitmapImage", pic)
        startActivity(setPictureIntent)
    }
}



    }
}
