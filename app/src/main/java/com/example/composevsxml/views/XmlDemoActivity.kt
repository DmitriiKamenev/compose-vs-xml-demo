package com.example.composevsxml.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.composevsxml.R
import com.example.composevsxml.databinding.ActivityXmlDemoBinding
import com.example.composevsxml.databinding.ItemSettingRowBinding
import com.example.composevsxml.databinding.ItemSwitchRowBinding
import com.example.composevsxml.shared.DemoViewModel
import com.example.composevsxml.shared.TAB_TITLES
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class XmlDemoActivity : AppCompatActivity() {
    private val vm: DemoViewModel by viewModels()
    private lateinit var binding: ActivityXmlDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXmlDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindRow(binding.rowPremium, R.drawable.ic_star, "Премиум", "Доступ к дополнительным функциям")
        bindRow(binding.rowAbout, R.drawable.ic_info, "О приложении", "Версия 1.0.0")
        bindSwitch(binding.rowNotifications, "Уведомления", "Показывать оповещения", vm::onNotificationsChanged) {
            vm.notifications.value
        }
        bindSwitch(binding.rowTimeBlock, "Блокировка по времени", "Пока недоступно", vm::onTimeBlockChanged) {
            vm.timeBlock.value
        }
        binding.counterButton.setOnClickListener { vm.increment() }
        binding.bottomNav.setOnItemSelectedListener { item ->
            vm.selectTab(
                when (item.itemId) {
                    R.id.tab_home -> 0
                    R.id.tab_lock -> 1
                    else -> 2
                }
            )
            true
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.notifications.collect { binding.rowNotifications.switchView.isChecked = it } }
                launch { vm.timeBlock.collect { binding.rowTimeBlock.switchView.isChecked = it } }
                launch { vm.count.collect { binding.counterButton.text = "Нажатий: $it" } }
                launch {
                    combine(vm.tab, vm.status) { tab, status -> "Раздел: ${TAB_TITLES[tab]} | $status" }
                        .collect { binding.statusText.text = it }
                }
            }
        }
    }

    private fun bindRow(row: ItemSettingRowBinding, @DrawableRes icon: Int, title: String, subtitle: String) {
        row.icon.setImageResource(icon)
        row.title.text = title
        row.subtitle.text = subtitle
        row.root.setOnClickListener { Toast.makeText(this, title, Toast.LENGTH_SHORT).show() }
    }

    private fun bindSwitch(
        row: ItemSwitchRowBinding,
        title: String,
        subtitle: String,
        onChange: (Boolean) -> Unit,
        current: () -> Boolean
    ) {
        row.title.text = title
        row.subtitle.text = subtitle
        row.switchView.setOnClickListener {
            onChange(row.switchView.isChecked)
            row.switchView.isChecked = current() // ViewModel мог отклонить изменение
        }
    }
}
