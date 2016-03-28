package com.EWIT.FrenchCafe.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.Contextor
import com.EWIT.FrenchCafe.viewgroup.AlarmInfoViewGroup
import java.util.*

/**
 * Created by Euro on 3/11/16 AD.
 */
class RecyclerAdapter(var alarmCollectionList: ArrayList<Model.AlarmDao>, var alarmInfoInteractionListener: AlarmInfoInteractionListener) : RecyclerView.Adapter<RecyclerAdapter.AlarmInfoViewHolder>() {

    var lastPosition: Int = -1
    private var lastInsertedIndex: Int = -1

    /** Override zone **/

    override fun onBindViewHolder(holder: AlarmInfoViewHolder?, position: Int) {
        //        var animScaleIn = AnimationUtils.loadAnimation(Contextor.context, R.anim.anim_scale_up)
        //        holder!!.alarmInfoViewGroup.startAnimation(animScaleIn)

        if (position > lastPosition) {
            animateNewItemAdded(holder)
            lastPosition = position
        }

        if(lastInsertedIndex != -1 && lastInsertedIndex == position){
            animateNewItemAdded(holder)
            lastInsertedIndex = -1
            lastPosition++
        }

        holder!!.setModel(alarmCollectionList[position])
    }

    private fun animateNewItemAdded(holder: AlarmInfoViewHolder?) {
        var animScaleIn = AnimationUtils.loadAnimation(holder!!.alarmInfoViewGroup.context, R.anim.anim_scale_up)
        holder.alarmInfoViewGroup.startAnimation(animScaleIn)
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
        lastPosition--
    }


    fun addAtPosition(index: Int) {
        notifyItemInserted(index)
        lastInsertedIndex = index
    }

    fun setList(alarmCollectionList: ArrayList<Model.AlarmDao>) {
        lastPosition = -1
        lastInsertedIndex = -1
        this@RecyclerAdapter.alarmCollectionList = alarmCollectionList
        notifyDataSetChanged()
    }

    /** Inner class zone **/

    inner class AlarmInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var alarmInfoViewGroup: AlarmInfoViewGroup

        init {
            alarmInfoViewGroup = itemView?.findViewById(R.id.alarmInfoViewGroup) as AlarmInfoViewGroup

            alarmInfoViewGroup.getEditObservable().subscribe { alarmInfoInteractionListener.onEdit(it, adapterPosition) }

            alarmInfoViewGroup.getDeleteObservable().filter { adapterPosition != -1 }.subscribe {
                //                alarmInfoViewGroup.startAnimation(AnimationUtils.loadAnimation(alarmInfoViewGroup.context, R.anim.anim_scale_down))
                alarmInfoInteractionListener.onDelete(adapterPosition)
            }

            alarmInfoViewGroup.getSwitchObservable().subscribe { alarmInfoInteractionListener.onCheckedChange(it.first, it.second, adapterPosition) }
        }

        fun setModel(alarmDao: Model.AlarmDao) {
            alarmInfoViewGroup.setAlarmDao(alarmDao)
        }
    }

    /** Listener zone  **/

    interface AlarmInfoInteractionListener {
        fun onCheckedChange(alarmDao: Model.AlarmDao, isChecked: Boolean, index: Int)
        fun onEdit(alarmDao: Model.AlarmDao, index: Int)
        fun onDelete(index: Int)
    }
}