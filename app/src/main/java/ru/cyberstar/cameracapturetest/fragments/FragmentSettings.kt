package ru.cyberstar.cameracapturetest.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_setting_list.view.*
import ru.cyberstar.cameracapturetest.R
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.get
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.prefs
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.set
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_DEFAULT
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_KEY

class FragmentSettings : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var speedEditText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_list, container, false)
        val speed: Int? = prefs()[FPS_KEY, FPS_DEFAULT]
        speedEditText = view.speedEditText
        speedEditText.text = speed.toString()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        speedEditText.text?.let {
            val speed:Int = it.toString().toInt()
            prefs()[FPS_KEY] = speed
        }
        super.onDetach()
    }


    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() = FragmentSettings()
    }
}
