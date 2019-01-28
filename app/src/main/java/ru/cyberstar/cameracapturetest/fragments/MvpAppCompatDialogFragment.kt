package ru.cyberstar.cameracapturetest.fragments


import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpDelegate

/**
 * Date: 17-Dec-16
 * Time: 21:55
 *
 * @author Konstantin Tckhovrebov
 */
class MvpAppCompatDialogFragment : AppCompatDialogFragment() {

    private var mIsStateSaved: Boolean = false
    private var mMvpDelegate: MvpDelegate<out MvpAppCompatDialogFragment>? = null

    /**
     * @return The [MvpDelegate] being used by this Fragment.
     */
    private val mvpDelegate: MvpDelegate<*>
        get() {
            if (mMvpDelegate == null) {
                mMvpDelegate = MvpDelegate(this)
            }

            return mMvpDelegate as MvpDelegate<out MvpAppCompatDialogFragment>
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        mIsStateSaved = false

        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mIsStateSaved = true

        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()

        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mvpDelegate.onDetach()
        mvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity!!.isFinishing) {
            mvpDelegate.onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            mvpDelegate.onDestroy()
        }
    }
}
