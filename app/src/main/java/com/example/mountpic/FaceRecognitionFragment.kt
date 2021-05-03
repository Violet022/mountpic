package com.example.mountpic

import androidx.fragment.app.Fragment

class FaceRecognitionFragment : Fragment(R.layout.fragment_face_recognition){

    companion object{
        val TAG = FaceRecognitionFragment::class.java.simpleName
        fun newInstance() = FaceRecognitionFragment()
    }
}