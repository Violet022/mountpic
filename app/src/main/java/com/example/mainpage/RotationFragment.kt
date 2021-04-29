package com.example.mainpage

import androidx.fragment.app.Fragment

class RotationFragment : Fragment(R.layout.fragment_rotation) {

    companion object {
        val TAG = RotationFragment::class.java.simpleName
        fun newInstance() = RotationFragment()
    }
}