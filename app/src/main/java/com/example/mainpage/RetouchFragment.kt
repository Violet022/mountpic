package com.example.mainpage

import androidx.fragment.app.Fragment

class RetouchFragment : Fragment(R.layout.fragment_retouch){

    companion object{
        val TAG = RetouchFragment::class.java.simpleName
        fun newInstance() = RetouchFragment()
    }
}