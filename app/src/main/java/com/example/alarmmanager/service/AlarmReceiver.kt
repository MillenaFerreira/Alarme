package com.example.alarmmanager.service

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarmmanager.MainActivity
import com.example.alarmmanager.MusicControl
import com.example.alarmmanager.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent1: Intent?) {
        // Mostra uma notificação de toast quando o BroadcastReceiver é acionado.
        Toast.makeText(context, "response done", Toast.LENGTH_SHORT).show()

        if (context != null) {
            // Inicia a reprodução de música usando uma classe MusicControl.
            MusicControl.getInstance(context).playMusic(context)
        }

        // Cria um Intent para desativar a notificação.
        val dismissIntent = Intent(context, MediaNotificationReceiver::class.java)
        val piDismiss = PendingIntent.getBroadcast(context,
            2,
            dismissIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        // Obtém o serviço de notificação do sistema.
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Cria um Intent para abrir a atividade MainActivity quando a notificação for tocada.
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent1!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Cria um PendingIntent para a atividade MainActivity.
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cria e configura a notificação usando NotificationCompat.Builder.
        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm Manager")
            .setContentText("try using alarm manager")
            .setAutoCancel(true)  // Fecha a notificação ao clicar nela.
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(Notification.PRIORITY_HIGH)
            .addAction(
                R.drawable.ic_launcher_background, "Stop", piDismiss
            )
            .setContentIntent(pendingIntent)
            .build()

        // Notifica o gerenciador de notificações para exibir a notificação.
        notificationManager.notify(1, builder)

        // Define uma função local para parar a reprodução de música usando a classe MusicControl.
        fun closeMusic(){
            MusicControl.getInstance(context).stopMusic()
        }
    }

    companion object {
        const val CHANNEL_ID = "alarmManager_channel"
    }
}
