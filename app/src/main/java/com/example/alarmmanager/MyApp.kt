package com.example.alarmmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.alarmmanager.service.AlarmReceiver

class MyApp : Application() {
    // Classe que estende Application e é usada para inicializar o canal de notificação do aplicativo.

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Método privado para criar o canal de notificação do aplicativo.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Verifica se o dispositivo está executando uma versão do Android que suporta canais de notificação (Android Oreo e posterior).

            val channel = NotificationChannel(
                AlarmReceiver.CHANNEL_ID, // ID do canal, provavelmente definido em outro lugar
                "Alarm Manager", // Nome do canal
                NotificationManager.IMPORTANCE_DEFAULT // Importância do canal (IMPORTANCE_DEFAULT é uma importância média)
            )

            channel.description = "Channel for Alarm Manager" // Descrição do canal

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Obtém uma referência ao gerenciador de notificações do sistema.

            notificationManager.createNotificationChannel(channel)
            // Cria o canal de notificação no sistema.
        }
    }
}
