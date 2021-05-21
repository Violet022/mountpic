package com.example.mountpic

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

private const val CAMERA_REQUEST_CODE = 1
private const val STORAGE_REQUEST_CODE = 2
private const val IMAGE_CAMERA_CODE = 3
private const val IMAGE_STORAGE_CODE = 4
private const val FILE_NAME = "photo.jpg"

class MainActivity : AppCompatActivity() {
    private lateinit var btnSettings: Button
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var photoFile: File

    private val cameraPermissions: Array<String> = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val storagePermission: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSettings = findViewById(R.id.settings)
        btnCamera = findViewById(R.id.cameraBtn)
        btnGallery = findViewById(R.id.galleryBtn)

        btnSettings.setOnClickListener() {
            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS) //ACTION_APPLICATION_DETAILS_SETTINGS
            startActivityForResult(intent, 100)
        }

        btnGallery.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                if (shouldShowRequestPermissionRationale(storagePermission[0])) {
                    showDialog(storagePermission[0])
                } else {
                    ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
                }
            }
        }

        btnCamera.setOnClickListener {
            if (checkCameraPermission()) {
                makeAPhoto()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try {
            startActivityForResult(intent, IMAGE_STORAGE_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun setPic (pic: Uri?) {
        val randomIntent = Intent(this, SecondPageActivity::class.java)
        randomIntent.putExtra(this@MainActivity.getString(R.string.extraForStorage), pic)
        startActivity(randomIntent)
    }

    private fun checkCameraPermission(): Boolean {
        val cameraResult = (ContextCompat.checkSelfPermission(this, cameraPermissions[0])
                == PackageManager.PERMISSION_GRANTED)
        val storageResult = (ContextCompat.checkSelfPermission(this, cameraPermissions[1])
                == PackageManager.PERMISSION_GRANTED)
        return cameraResult && storageResult
    }

    private fun requestCameraPermission() {
        var denyInPast: Boolean = false

        for (permission in cameraPermissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                showDialog(permission)
                denyInPast = true
            }
        }
        if (!denyInPast) {
            ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
        }
    }

    private fun showDialog(permission: String) {
        val builder = AlertDialog.Builder(this)
        lateinit var direction: String

        builder.apply {
            when (permission) {
                cameraPermissions[0] -> {
                    direction = "камере"
                    setMessage(context.getString(R.string.messageForCamera))
                    setPositiveButton("ОК") {dialog, which ->
                        ActivityCompat.requestPermissions(this@MainActivity, cameraPermissions, CAMERA_REQUEST_CODE)
                    }
                }
                cameraPermissions[1] -> {
                    direction = "хранилищу"
                    setMessage(context.getString(R.string.messageForStorage))
                    setPositiveButton("ОК") {dialog, which ->
                        ActivityCompat.requestPermissions(this@MainActivity, cameraPermissions, CAMERA_REQUEST_CODE)
                    }
                }
                storagePermission[0] -> {
                    direction = "галерее"
                    setMessage(context.getString(R.string.messageForStorage))
                    setPositiveButton("ОК") {dialog, which ->
                        ActivityCompat.requestPermissions(this@MainActivity, storagePermission, STORAGE_REQUEST_CODE)
                    }
                }
            }
            setTitle("Нет доступа к $direction")
            setNeutralButton("Отмена") {dialog, which ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) makeAPhoto()
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
            }
        }
    }

    private fun makeAPhoto() {
        photoFile = getPhotoFile(FILE_NAME)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileProvider = FileProvider.getUriForFile(this, this@MainActivity.getString(R.string.authorityName), photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            try {
                startActivityForResult(takePictureIntent, IMAGE_CAMERA_CODE)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = Uri.fromFile(photoFile)
            setThePhoto(takenImage)
        }

        if (requestCode == IMAGE_STORAGE_CODE && resultCode == Activity.RESULT_OK) {
            val picUri = data?.data
            setPic(picUri)
        }
    }

    private fun setThePhoto(pic: Uri?) {
        val setPictureIntent = Intent(this, SecondPageActivity::class.java)
        setPictureIntent.putExtra(this@MainActivity.getString(R.string.extraForCamera), pic)
        startActivity(setPictureIntent)
    }
}



