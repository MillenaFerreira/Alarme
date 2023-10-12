package com.example.alarmmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alarmmanager.MusicControl

class MediaNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Para a reprodução de música usando a classe MusicControl.
        MusicControl.getInstance(context).stopMusic()

        // Exibe uma notificação de toast para indicar que a música foi interrompida.
        Toast.makeText(context, "music", Toast.LENGTH_SHORT).show()
    }
}