package ru.cyberstar.cameracapturetest.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting_list.view.*
import ru.cyberstar.cameracapturetest.R
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_DEFAULT
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_KEY
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.get
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.prefs
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.set

class FragmentSettings : Fragment() {

    lateinit var fpsEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_list, container, false)
        val fps: Int? = prefs()[FPS_KEY, FPS_DEFAULT]
        fpsEditText = view.fpsEditText
        view.fpsEditText.setText(fps.toString())
        return view
    }

    override fun onDetach() {
        fpsEditText.text?.let {
            val speed: Int = it.toString().toInt()
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
