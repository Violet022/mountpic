package com.example.mountpic

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class App : AppCompatActivity() {
    private lateinit var placeForPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        placeForPhoto = findViewById(R.id.placeForPhoto)

        var pic = intent?.extras?.get("BitmapImage") as Bitmap
        placeForPhoto.setImageBitmap(pic)
        //photo.text = intent.getStringExtra("BitmapImage")
        //photo.setImageBitmap(getIntent().getExtra("BitmapImage")
    }
}