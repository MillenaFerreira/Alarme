package com.example.alarmmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmmanager.Dao.AlarmDao

class AlarmFactory(private val alarmDao: AlarmDao) : ViewModelProvider.Factory {
    // O construtor da classe recebe um objeto AlarmDao como parâmetro.
    // Isso permite que a fábrica saiba como criar ViewModels que dependem de AlarmDao.

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Este método é chamado para criar uma instância de ViewModel quando necessário.

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            // Verifica se a classe solicitada (modelClass) é uma subclasse de MainViewModel.
            // Se for o caso, cria e retorna uma instância de MainViewModel.

            return MainViewModel(alarmDao) as T
            // Cria uma instância de MainViewModel, passando o alarmDao como argumento,
            // e a converte para o tipo genérico T para que corresponda à classe solicitada.
        }

        // Se a classe solicitada não for reconhecida, lança uma exceção com uma mensagem de erro.
        throw IllegalArgumentException("Unknown view model")
    }
}
