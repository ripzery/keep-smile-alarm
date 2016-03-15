package com.EWIT.FrenchCafe.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.viewgroup.AlarmInfoViewGroup
import java.util.*

/**
 * Created by Euro on 3/11/16 AD.
 */
class RecyclerAdapter(var alarmCollectionList: ArrayList<Model.AlarmDao>, var alarmInfoInteractionListener: AlarmInfoInteractionListener) : RecyclerView.Adapter<RecyclerAdapter.AlarmInfoViewHolder>() {

    /** Override zone **/

    override fun onBindViewHolder(holder: AlarmInfoViewHolder?, position: Int) {
        //        var animScaleIn = AnimationUtils.loadAnimation(Contextor.context, R.anim.anim_scale_up)
        //        holder!!.alarmInfoViewGroup.startAnimation(animScaleIn)
        holder!!.setModel(alarmCollectionList[position])
    }

    override fun onCreateViewHolder(container: ViewGroup?, position: Int): AlarmInfoViewHolder? {
        val v = LayoutInflater.from(container?.context).inflate(R.layout.item_view_alarm_list, container, false)
        return AlarmInfoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return alarmCollectionList.size
    }

    /** Method zone **/


    fun updateAtPosition(index: Int) {
        notifyItemChanged(index)
    }

    fun removeAtPosition(index: Int) {
        notifyItemRemoved(index)
    }

    fun setList(alarmCollectionList: ArrayList<Model.AlarmDao>) {
        this@RecyclerAdapter.alarmCollectionList = alarmCollectionList
        notifyDataSetChanged()
    }

    /** Inner class zone **/

    inner class AlarmInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var alarmInfoViewGroup: AlarmInfoViewGroup

        init {
            alarmInfoViewGroup = itemView?.findViewById(R.id.alarmInfoViewGroup) as AlarmInfoViewGroup

            alarmInfoViewGroup.setOnClickListener({

            })

            alarmInfoViewGroup.getEditObservable().subscribe { alarmInfoInteractionListener.onEdit(it, adapterPosition) }

            alarmInfoViewGroup.getDeleteObservable().filter { adapterPosition != -1 }.subscribe {
                alarmInfoViewGroup.startAnimation(AnimationUtils.loadAnimation(alarmInfoViewGroup.context, R.anim.anim_scale_down))
                alarmInfoInteractionListener.onDelete(adapterPosition)
            }
        }

        fun setModel(alarmDao: Model.AlarmDao) {
            alarmInfoViewGroup.setAlarmDao(alarmDao)
        }
    }

    /** Listener zone  **/

    interface AlarmInfoInteractionListener {
        fun onEdit(alarmDao: Model.AlarmDao, index: Int)
        fun onDelete(index: Int)
    }
}