package com.example.alarmmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmmanager.Dao.AlarmDao
import com.example.alarmmanager.model.Alarm
import kotlinx.coroutines.launch

class MainViewModel(val dao: AlarmDao) : ViewModel() {
    // Declaração de uma classe ViewModel para gerenciar operações relacionadas a alarmes.

    val alarms = dao.getAll()
    // Uma variável LiveData que armazena a lista de alarmes obtida do banco de dados.

    fun insert(alarm: Alarm) {
        // Método para inserir um novo alarme no banco de dados.
        viewModelScope.launch {
            // Inicia uma operação em uma thread de segundo plano usando o viewModelScope.
            dao.insertAlarm(alarm)
            // Chama o método insertAlarm do dao para inserir o alarme no banco de dados.
        }
    }

    fun update(alarm: Alarm) {
        // Método para atualizar um alarme existente no banco de dados.
        viewModelScope.launch {
            // Inicia uma operação em uma thread de segundo plano usando o viewModelScope.
            dao.updateAlarm(alarm)
            // Chama o método updateAlarm do dao para atualizar o alarme no banco de dados.
        }
    }
}
