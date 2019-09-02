package com.testapp.appfortesting.screens.webview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_web_view.*
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.Uri
import android.webkit.CookieManager
import android.widget.Toast
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.webkit.CookieSyncManager


class WebViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)
        val IS_DATA_CLEARED = sharedPreferences.getBoolean("IS_DATA_CLEARED", true)

        if (IS_DATA_CLEARED) {
            //DATA IS CLEARED
            val appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = appSharedPrefs.edit()
            editor.putBoolean("IS_DATA_CLEARED", false)
            editor.apply()
            Toast.makeText(context, "Data is cleared", Toast.LENGTH_LONG).show()
        } else {
            //DATA IS NOT CLEARED
            Toast.makeText(context, "Data is not cleared", Toast.LENGTH_LONG).show()
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://github.com")
        //CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true)
        webView.webViewClient = MyBrowser()
    }
    private inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mailto:") || url.startsWith(
                    "mms:"
                ) || url.startsWith("mmsto:") || url.startsWith("market:")
            ) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            } else {
                view.loadUrl(url)
                return true
            }
        }
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            //Users will be notified in case there's an error (i.e. no internet connection)
            Toast.makeText(activity, "Oh no! $description", Toast.LENGTH_SHORT).show()
        }

        override fun onPageFinished(view: WebView, url: String) {
            CookieSyncManager.getInstance().sync()
        }
    }
}
