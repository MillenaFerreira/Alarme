package com.example.alarmmanager
import android.content.Context
import android.media.MediaPlayer

class MusicControl(private var mediaPlayer: MediaPlayer?) {
    // A classe MusicControl gerencia a reprodução e parada de música usando um objeto MediaPlayer.

    companion object {
        @Volatile
        private var INSTANCE: MusicControl? = null
        // Declaração de um objeto Singleton chamado INSTANCE para garantir que haja apenas uma instância de MusicControl.

        fun getInstance(context: Context): MusicControl {
            // Método para obter a instância única da classe MusicControl.

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    // Se não houver uma instância existente, cria uma nova instância.

                    instance = MusicControl(MediaPlayer.create(context, R.raw.music1))
                    INSTANCE = instance
                }
                return instance
                // Retorna a instância existente ou a nova instância criada.
            }
        }
    }

    fun playMusic(context: Context) {
        // Método para iniciar a reprodução da música.

        mediaPlayer = MediaPlayer.create(context, R.raw.music1)
        mediaPlayer?.start()
        // Cria uma instância de MediaPlayer com o contexto fornecido e inicia a reprodução da música.
    }

    fun stopMusic() {
        INSTANCE?.mediaPlayer?.stop()
        INSTANCE?.mediaPlayer?.seekTo(0)
    }
}
// Para a reprodução da música, se houver uma instância válida de MediaPlayer, e a reinicia para a posição



