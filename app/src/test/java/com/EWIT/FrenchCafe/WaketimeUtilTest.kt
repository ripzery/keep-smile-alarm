package com.EWIT.FrenchCafe

import com.EWIT.FrenchCafe.extension.getTimeFormat
import com.EWIT.FrenchCafe.extension.mCalendar
import com.EWIT.FrenchCafe.extension.toAlarmDao
import com.EWIT.FrenchCafe.model.dao.Model
import com.EWIT.FrenchCafe.util.WaketimeUtil
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by Euro on 3/25/16 AD.
 */

class WaketimeUtilTest {

    private fun initAlarmDao(): Model.AlarmDao{
        val today: Calendar = mCalendar()
        val datePicked: Model.DatePicked = Model.DatePicked(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        val timeWaked: Model.TimeWake = Model.TimeWake(today.get(Calendar.HOUR_OF_DAY) + 1, today.get(Calendar.MINUTE))
        val alarmDao:Model.AlarmDao = Model.AlarmDao(datePicked, timeWaked, listOf())

        return alarmDao
    }

    @Test fun alarmDaoToCalendarIsCorrect(){
        val today: Calendar = mCalendar()
        val alarmDao = initAlarmDao()

        today.timeInMillis += 60 * 60000
        System.out.println(alarmDao.toCalendar().getTimeFormat())
        Assert.assertEquals(today.getTimeFormat(), alarmDao.toCalendar().getTimeFormat())
    }

    @Test fun calculateSummationNoRepeatTimePastIsCorrect(){
        val today: Calendar = mCalendar()
        var alarmDao = initAlarmDao()
        val hour = alarmDao.timeWake.hourOfDay - 5

        today.set(Calendar.HOUR_OF_DAY, hour)
        alarmDao.timeWake.hourOfDay = hour

        Assert.assertNotEquals(WaketimeUtil.getAlarmDaoUniqueId(alarmDao), today.toAlarmDao().toCalendar().timeInMillis)

        today.set(Calendar.DAY_OF_MONTH, alarmDao.datePicked.dayOfMonth)
        val tomorrow = today

        System.out.println(alarmDao )
        System.out.println(tomorrow.toAlarmDao())

        Assert.assertEquals(WaketimeUtil.getAlarmDaoUniqueId(alarmDao), WaketimeUtil.getAlarmDaoUniqueId(tomorrow.toAlarmDao()))
    }

    @Test fun calculateSummationNoRepeatTimeNotPastIsCorrect(){
        val today: Calendar = mCalendar()
        var alarmDao = initAlarmDao()
        val hour = alarmDao.timeWake.hourOfDay + 1

        today.set(Calendar.HOUR_OF_DAY, hour)
        alarmDao.timeWake.hourOfDay = hour

//        Assert.assertEquals(WaketimeUtil.getWakeTimeSum(alarmDao), today.toAlarmDao().toCalendar().timeInMillis / 1000)

        today.set(Calendar.DAY_OF_MONTH, alarmDao.datePicked.dayOfMonth+1)
        val tomorrow = today


        Assert.assertNotEquals(WaketimeUtil.getAlarmDaoUniqueId(alarmDao), WaketimeUtil.getAlarmDaoUniqueId(tomorrow.toAlarmDao()))
    }

    @Test fun calculateSummationRepeatTimeIsCorrect() {
        val today: Calendar = mCalendar()
        var alarmDao = initAlarmDao()
        val hour = alarmDao.timeWake.hourOfDay - 5

        today.set(Calendar.HOUR_OF_DAY, hour)
        alarmDao.timeWake.hourOfDay = hour
        alarmDao.repeatDay = listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.SATURDAY, Calendar.SUNDAY)

//        System.out.println(alarmDao)
//        System.out.println(today.toAlarmDao(listOf(Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.SATURDAY, Calendar.SUNDAY)))

        Assert.assertNotEquals(WaketimeUtil.getAlarmDaoUniqueId(alarmDao), WaketimeUtil.getAlarmDaoUniqueId(today.toAlarmDao(listOf(Calendar.MONDAY))))
        Assert.assertEquals(WaketimeUtil.getAlarmDaoUniqueId(alarmDao), WaketimeUtil.getAlarmDaoUniqueId(today.toAlarmDao(listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.SATURDAY, Calendar.SUNDAY))))
    }


    @Test fun collectionPlusIsWork(){
        var alarmDao = initAlarmDao()
        alarmDao.repeatDay = alarmDao.repeatDay.plus(1)

        Assert.assertEquals(1, alarmDao.repeatDay.size)
        Assert.assertEquals(1, alarmDao.repeatDay[0])

        alarmDao.repeatDay = alarmDao.repeatDay.plus(3)
        Assert.assertEquals(2, alarmDao.repeatDay.size)
        Assert.assertEquals(3, alarmDao.repeatDay[1])

    }

    @Test fun repeatTodayIsCorrect(){
        val today: Calendar = mCalendar()
        var alarmDao = initAlarmDao()
        val hour = alarmDao.timeWake.hourOfDay + 5

        alarmDao.repeatDay = alarmDao.repeatDay.plus(Calendar.FRIDAY)
        alarmDao.timeWake.hourOfDay = hour
        today.set(Calendar.HOUR_OF_DAY, hour)

        Assert.assertEquals(true, WaketimeUtil.isAlarmToday(alarmDao.repeatDay.toIntArray()))
    }

    @Test fun repeatNotTodayIsCorrect(){
        val today: Calendar = mCalendar()
        var alarmDao = initAlarmDao()
        val hour = alarmDao.timeWake.hourOfDay + 5

        alarmDao.repeatDay = listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.SATURDAY, Calendar.SUNDAY)

        alarmDao.timeWake.hourOfDay = hour
        today.set(Calendar.HOUR_OF_DAY, hour)

        Assert.assertEquals(false, WaketimeUtil.isAlarmToday(alarmDao.repeatDay.toIntArray()))
    }
}