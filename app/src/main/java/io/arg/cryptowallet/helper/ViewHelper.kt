package io.arg.cryptowallet.helper

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.arg.cryptowallet.R

class ViewHelper {

    companion object {

        /**
         * Init a toolbar of an activity
         * @property activity the activity
         * @property toolbar the toolbar
         */
        fun initToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
            activity.setSupportActionBar(toolbar)
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_36)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            toolbar.setNavigationOnClickListener { v: View? -> activity.onBackPressed() }
            activity.supportActionBar?.title = activity.getString(R.string.app_name)
        }

    }

}