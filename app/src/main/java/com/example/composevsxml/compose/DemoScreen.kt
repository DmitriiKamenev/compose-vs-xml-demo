package com.example.composevsxml.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composevsxml.shared.DemoViewModel
import com.example.composevsxml.shared.TAB_TITLES

@Composable
fun DemoScreen(vm: DemoViewModel) {
    val notifications by vm.notifications.collectAsState()
    val timeBlock by vm.timeBlock.collectAsState()
    val count by vm.count.collectAsState()
    val tab by vm.tab.collectAsState()
    val status by vm.status.collectAsState()
    val context = LocalContext.current

    Scaffold(bottomBar = { DemoBottomBar(selected = tab, onSelect = vm::selectTab) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Настройки", style = MaterialTheme.typography.headlineMedium)
            SettingRow(Icons.Default.Star, "Премиум", "Доступ к дополнительным функциям") {
                Toast.makeText(context, "Премиум", Toast.LENGTH_SHORT).show()
            }
            SettingRow(Icons.Default.Info, "О приложении", "Версия 1.0.0") {
                Toast.makeText(context, "О приложении", Toast.LENGTH_SHORT).show()
            }
            SwitchRow("Уведомления", "Показывать оповещения", notifications, vm::onNotificationsChanged)
            SwitchRow("Блокировка по времени", "Пока недоступно", timeBlock, vm::onTimeBlockChanged)
            Button(onClick = vm::increment) { Text("Нажатий: $count") }
            Text("Раздел: ${TAB_TITLES[tab]} | $status")
        }
    }
}

@Composable
fun SettingRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun DemoBottomBar(selected: Int, onSelect: (Int) -> Unit) {
    NavigationBar {
        listOf(Icons.Default.Home, Icons.Default.Lock, Icons.Default.Star).forEachIndexed { i, icon ->
            NavigationBarItem(
                selected = selected == i,
                onClick = { onSelect(i) },
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(TAB_TITLES[i]) }
            )
        }
    }
}
