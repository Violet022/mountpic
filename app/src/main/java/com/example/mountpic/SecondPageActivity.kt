package com.example.mountpic

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mountpic.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.content_second.*
import java.io.IOException

//lateinit var picture: Uri

class SecondPageActivity : AppCompatActivity() {

    private val viewBinding by viewBinding(ActivitySecondBinding::bind, R.id.drawerLayout)
    lateinit var gotPicture: Uri
    lateinit var setPicture: Bitmap



    fun fromImageToBitmap(image: ImageView): Bitmap? {

        val bitmapDrawable: BitmapDrawable = image.drawable as BitmapDrawable

        return bitmapDrawable.bitmap

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setSupportActionBar(toolbar)

        //получение изображения из галереи по Uri
        if (intent?.extras?.get(this@SecondPageActivity.getString(R.string.extraForStorage)) != null) {
            gotPicture = intent?.extras?.get(this@SecondPageActivity.getString(R.string.extraForStorage)) as Uri
            setPicture = fromUriToBitmap(gotPicture)
            viewBinding.imageView.setImageBitmap(setPicture)
            //viewBinding.imageView.setImageURI(picture)
        }

        //получение изображения из камеры по Uri
        if (intent?.extras?.get(this@SecondPageActivity.getString(R.string.extraForCamera)) != null) {
            gotPicture = intent?.extras?.get(this@SecondPageActivity.getString(R.string.extraForCamera)) as Uri
            //viewBinding.imageView.setImageURI(picture)
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewBinding.navMenu.setNavigationItemSelectedListener { item ->
            viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId){
                R.id.actionRotation -> {
                    selectScreen(RotationFragment.TAG, RotationFragment.newInstance())
                    true
                }

                R.id.actionColorCorrection -> {
                    selectScreen(ColorCorrectionFragment.TAG, ColorCorrectionFragment.newInstance())
                    true
                }

                R.id.actionFaceRecognition -> {
                    selectScreen(FaceRecognitionFragment.TAG, FaceRecognitionFragment.newInstance())
                    true
                }

                R.id.actionFiltration -> {
                    selectScreen(FiltrationFragment.TAG, FiltrationFragment.newInstance())
                    true
                }

                R.id.actionMasking -> {
                    selectScreen(MaskingFragment.TAG, MaskingFragment.newInstance())
                    true
                }

                R.id.actionRetouch -> {
                    selectScreen(RetouchFragment.TAG, RetouchFragment.newInstance())
                    true
                }

                R.id.actionScaling -> {
                    selectScreen(ScalingFragment.TAG, ScalingFragment.newInstance())
                    true
                }

                R.id.actionSplines -> {
                    selectScreen(SplinesFragment.TAG, SplinesFragment.newInstance())
                    true
                }

                else -> false
            }
        }
    }

    fun fromUriToBitmap (picture: Uri): Bitmap {
        lateinit var bitmapPicture: Bitmap
        try {
            bitmapPicture = MediaStore.Images.Media.getBitmap(this.contentResolver, picture)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapPicture
    }

    private fun selectScreen(tag: String, fragment: Fragment) {
        supportFragmentManager.commit {
            val active = findActiveFragment()
            val target = supportFragmentManager.findFragmentByTag(tag)

            if (active != null && target != null && active == target) return@commit

            if (active != null) {
                hide(active)
            }

            if (target == null) {
                add(R.id.fragmentContainer, fragment, tag)
            } else {
                show(target)
            }
        }
    }

    private fun findActiveFragment() = supportFragmentManager.fragments.find { it.isVisible }
}