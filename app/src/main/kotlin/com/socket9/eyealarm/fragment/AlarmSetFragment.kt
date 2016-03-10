package com.socket9.eyealarm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.eyealarm.R
import com.socket9.eyealarm.dialog.DatePickerDialogFragment
import com.socket9.eyealarm.dialog.TimePickerDialogFragment
import com.socket9.eyealarm.extension.log
import kotlinx.android.synthetic.main.fragment_alarm_set.*

/**
 * Created by Euro on 3/10/16 AD.
 */
class AlarmSetFragment : Fragment(){

    /** Variable zone **/
    lateinit var param1: String


    /** static method zone **/
    companion object{
        val ARG_1 = "ARG_1"

        fun newInstance(param1:String) : AlarmSetFragment {
            var bundle: Bundle = Bundle()
            bundle.putString(ARG_1, param1)
            val alarmSetFragment: AlarmSetFragment = AlarmSetFragment()
            alarmSetFragment.arguments = bundle
            return alarmSetFragment
        }

    }

    /** Lifecycle zone **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            /* if newly created */
            param1 = arguments.getString(ARG_1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_alarm_set, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    /** Method zone **/

    private fun initInstance(){
        btnPickDate.setOnClickListener { showDatePickerDialog() }
        btnPickTime.setOnClickListener { showTimePickerDialog() }
    }

    private fun showTimePickerDialog(){
        var timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.show(childFragmentManager, "timePicker");
        timePickerDialog.getTimePickedObservable().subscribe { btnPickTime.text = it.getTimeFormat() }
    }

    private fun showDatePickerDialog() {
        var datePickerDialog = DatePickerDialogFragment()
        datePickerDialog.show(childFragmentManager, "datePicker");
        datePickerDialog.getDatePickedObservable().subscribe { btnPickDate.text = it.getDateFormat() }
    }
}