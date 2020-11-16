package com.example.naengbiseo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.naengbiseo.R
import kotlinx.android.synthetic.main.fragmetn_item_status.*


class ItemStatusFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmetn_item_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        youtube_button.setOnClickListener {
            var itemText=item_text.text.toString()
            var siteString="https://www.youtube.com/results?search_query="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }
        recipe10000_button.setOnClickListener {
            var itemText=item_text.text.toString()
            var siteString="https://www.10000recipe.com/recipe/list.html?q="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }
        do_eat_button.setOnClickListener {
            var itemText=item_text.text.toString()
            var siteString="https://www.haemukja.com/recipes?utf8=✓&sort=rlv&name="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }

    }
}