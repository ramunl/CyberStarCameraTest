package ru.cyberstar.cameracapturetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.cyberstar.cameracapturetest.fragments.CameraFragment
import ru.cyberstar.cameracapturetest.fragments.FragmentSettings

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        return@OnNavigationItemSelectedListener showSelectedFragment(item.itemId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", navigation.selectedItemId)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val selectedItemId = savedInstanceState?.getInt("selectedItemId") ?: R.id.navigation_camera
        showSelectedFragment(selectedItemId)
    }

    private fun showSelectedFragment(selectedItemId: Int): Boolean {
        return when (selectedItemId) {
            R.id.navigation_camera -> {
                showCamera()
                true
            }
            R.id.navigation_settings -> {
                showSettings()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun showCamera() {
        replaceFragment(R.id.fragmentContainer, CameraFragment.newInstance(), FragmentTag.CAMERA_FRAGMENT_TAG)
    }

    private fun showSettings() {
        replaceFragment(R.id.fragmentContainer, FragmentSettings.newInstance(), FragmentTag.SETTINGS_FRAGMENT_TAG)
    }
}
