package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alarmmanager.adapter.AlarmAdapter
import com.example.alarmmanager.database.AlarmDatabase
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.example.alarmmanager.model.Alarm
import com.example.alarmmanager.service.AlarmReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {
    // Declarando as variáveis de instância.
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: AlarmFactory
    private lateinit var adapter: AlarmAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializando o ViewModel e o adaptador para a lista de alarmes.
        val application = requireNotNull(this).application
        val dao = AlarmDatabase.getInstance(application).alarmDao
        viewModelFactory = AlarmFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        // Configurando o layout do RecyclerView e observando as alterações na lista de alarmes.
        binding.recyclerView.layoutManager = GridLayoutManager(application, 2)
        viewModel.alarms.observe(this, Observer {
            it?.let {
                adapter = AlarmAdapter(this, it)
                binding.recyclerView.adapter = adapter
            }
        })
    }

    // Método para cancelar um alarme.
    fun cancelAlarm(alarm: Alarm?) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)

        if (alarm != null) {
            alarm.checked = false
            viewModel.update(alarm)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    // Método para definir um alarme.
    fun setAlarm(alarm: Alarm? = null) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 2342, intent, PendingIntent.FLAG_IMMUTABLE)

        var timeMillis: Long = alarm?.timeInMillis ?: calendar.timeInMillis

        if (timeMillis < System.currentTimeMillis()) {
            timeMillis += 86400000L // Adiciona 24 horas em milissegundos
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }

        // Exibe um Toast para indicar que o alarme foi definido.
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()

        // Calcula a diferença de tempo entre o alarme e o momento atual e exibe um Toast informativo.
        // Atualiza o alarme se estiver sendo redefinido.
        // Se não, cria um novo alarme e insere no ViewModel.
    }

    @RequiresApi(Build.VERSION_CODES.N)
    // Método para exibir um seletor de hora (time picker) para configurar o alarme.
    fun showTimePicker(alarm: Alarm? = null) {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, "AlarmManager")

        var hour: Int
        var state: String

        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                hour = picker.hour - 12
                state = "PM"
            } else {
                hour = picker.hour
                state = "AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            if (alarm != null) {
                alarm.hour = hour
                alarm.minute = picker.minute
                alarm.state = state
                alarm.checked = false
                alarm.timeInMillis = calendar.timeInMillis
                viewModel.update(alarm)
            } else {
                val newAlarm = Alarm(hour = hour, minute = picker.minute, state = state, checked = false, timeInMillis = calendar.timeInMillis)
                viewModel.insert(newAlarm)
            }
        }
    }

    // Método para inflar o menu na barra de ação.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    // Método para lidar com os itens do menu na barra de ação.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insertBtnMenu -> {
                showTimePicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
