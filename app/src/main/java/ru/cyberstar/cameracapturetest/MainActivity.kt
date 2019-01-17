package ru.cyberstar.cameracapturetest

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.cyberstar.cameracapturetest.fragments.CameraFragment
import ru.cyberstar.cameracapturetest.fragments.FragmentSettings

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                showArticlesFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showSettingsFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showArticlesFragment() {
        replaceFragment(R.id.fragmentContainer, CameraFragment.newInstance(), FragmentId.CAMERA_FRAGMENT_ID)
    }
    private fun showSettingsFragment() {
        replaceFragment(R.id.fragmentContainer, FragmentSettings.newInstance(1), FragmentId.SETTINGS_FRAGMENT_ID)
    }
}
