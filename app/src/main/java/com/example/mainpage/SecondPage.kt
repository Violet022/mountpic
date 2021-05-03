package com.example.mainpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mainpage.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.content_second.*

class SecondPage : AppCompatActivity(R.layout.activity_second){

    private val viewBinding by viewBinding(ActivitySecondBinding::bind, R.id.drawerLayout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewBinding.navMenu.setNavigationItemSelectedListener { item ->
            viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
            when(item.itemId){
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

    private fun selectScreen(tag: String, fragment: Fragment) {
        supportFragmentManager.commit {
            val active = findActiveFragment()
            val target = supportFragmentManager.findFragmentByTag(tag)


            if(active != null && target != null && active == target) return@commit

            if(active != null){
                hide(active)
            }

            if(target == null){
                add(R.id.fragmentContainer, fragment, tag)
            }
            else{
                show(target)
            }
        }
    }



    private fun findActiveFragment() = supportFragmentManager.fragments.find { it.isVisible }

}