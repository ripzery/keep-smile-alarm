package com.socket9.eyealarm.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.eyealarm.R
import com.socket9.eyealarm.model.dao.Model
import com.socket9.eyealarm.viewgroup.AlarmInfoViewGroup

/**
 * Created by Euro on 3/11/16 AD.
 */
class RecyclerAdapter(val alarmCollectionList: List<Model.AlarmDao>, var alarmInfoInteractionListener: AlarmInfoInteractionListener) : RecyclerView.Adapter<RecyclerAdapter.AlarmInfoViewHolder>() {
    override fun onBindViewHolder(holder: AlarmInfoViewHolder?, position: Int) {
        holder!!.setModel(alarmCollectionList[position])
    }

    override fun onCreateViewHolder(container: ViewGroup?, position: Int): AlarmInfoViewHolder? {
        val v = LayoutInflater.from(container?.context).inflate(R.layout.item_view_alarm_list, container, false)
        return AlarmInfoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return alarmCollectionList.size
    }

    inner class AlarmInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private var alarmInfoViewGroup: AlarmInfoViewGroup

        init {
            alarmInfoViewGroup = itemView?.findViewById(R.id.alarmInfoViewGroup) as AlarmInfoViewGroup

            alarmInfoViewGroup.getEditObservable().subscribe { alarmInfoInteractionListener.onEdit(it) }

            alarmInfoViewGroup.getDeleteObservable().subscribe { alarmInfoInteractionListener.onDelete(adapterPosition) }
        }

        fun setModel(alarmDao: Model.AlarmDao) {
            alarmInfoViewGroup.setAlarmDao(alarmDao)
        }
    }

    interface AlarmInfoInteractionListener {
        fun onEdit(alarmDao: Model.AlarmDao)
        fun onDelete(index: Int)
    }
}