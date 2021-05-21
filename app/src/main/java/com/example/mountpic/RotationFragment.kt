package com.example.mountpic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class RotationFragment : Fragment(R.layout.fragment_rotation) {

    companion object {
        val TAG = RotationFragment::class.java.simpleName
        fun newInstance() = RotationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}