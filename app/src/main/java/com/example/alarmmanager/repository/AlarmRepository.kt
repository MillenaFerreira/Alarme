package com.example.alarmmanager.repository

import android.content.Context
import com.example.alarmmanager.database.AlarmDatabase
import com.example.alarmmanager.model.Alarm

class AlarmRepository (context: Context) {

    private val db = AlarmDatabase.getInstance(context = context)

    suspend fun save(alarm: Alarm) {
        return db.alarmDao.insertAlarm(alarm)
    }

//    fun findAlarmes(): List<Alarm>{
//        return db.alarmDao.getAll()
//    }

}