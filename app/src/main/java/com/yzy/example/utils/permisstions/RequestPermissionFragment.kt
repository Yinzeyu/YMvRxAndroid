package com.yzy.example.utils.permisstions

import android.os.Bundle
import androidx.fragment.app.Fragment

class RequestPermissionFragment : Fragment{
    private lateinit var permissions:Array<String>
    private var listener: RequestPermissionsListener? = null
    companion object{
        private const val INTENT_TO_START = "INTENT_TO_START"
        private const val REQUEST_CODE = 24
        fun newInstance(vararg permissions:String): RequestPermissionFragment {
            val bundle= Bundle()
            bundle.putStringArray(INTENT_TO_START,permissions)
            val fragment= RequestPermissionFragment()
            fragment.arguments=bundle
            return fragment
        }
    }

    constructor(){
        retainInstance = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            permissions= it.getStringArray(INTENT_TO_START) as Array<String>
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermissions(permissions, REQUEST_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_CODE){
            listener?.let {
                it.onRequestPermissions(requestCode,permissions, grantResults)
            }
            removeFragment()
        }
    }
    fun  setListener(listener: RequestPermissionsListener): RequestPermissionFragment {
        this.listener=listener
        return this
    }
    private fun removeFragment(){
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }
    interface RequestPermissionsListener{
        fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }
}