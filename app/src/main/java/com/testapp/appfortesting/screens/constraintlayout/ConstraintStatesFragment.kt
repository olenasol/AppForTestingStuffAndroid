package com.testapp.appfortesting.screens.constraintlayout

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.cl_states_start.*

class ConstraintStatesFragment : Fragment() {

    val handler = Handler()

    companion object {
        fun newInstance(): ConstraintStatesFragment {
            return ConstraintStatesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cl_states_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainConstrainLayout.loadLayoutDescription(R.xml.cl_states)
        var changed = false
        postDelayed(handler,  {
            mainConstrainLayout.setState(R.id.loading1,0, 0)
        }, null, 3000L)
        postDelayed(handler, {
            mainConstrainLayout.setState(if (changed) R.id.start1 else R.id.end1, 0, 0)
            btnStart.setText("End")
            changed = !changed
        }, null, 6000L)

    }
}