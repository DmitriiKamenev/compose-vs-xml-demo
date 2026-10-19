package com.example.composevsxml.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

val TAB_TITLES = listOf("Главная", "Защита", "Избранное")

/** Одна и та же логика и состояние для обеих реализаций интерфейса. */
class DemoViewModel : ViewModel() {

    private val _notifications = MutableStateFlow(true)
    val notifications: StateFlow<Boolean> = _notifications.asStateFlow()

    private val _timeBlock = MutableStateFlow(false)
    val timeBlock: StateFlow<Boolean> = _timeBlock.asStateFlow()

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _tab = MutableStateFlow(0)
    val tab: StateFlow<Int> = _tab.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    fun onNotificationsChanged(value: Boolean) {
        _notifications.value = value
        _status.value = if (value) "Уведомления включены" else "Уведомления выключены"
    }

    /** Включение отклоняется: состояние остается false, UI должен вернуть переключатель. */
    fun onTimeBlockChanged(value: Boolean) {
        if (value) _status.value = "Функция временно недоступна"
    }

    fun increment() {
        _count.value++
    }

    fun selectTab(index: Int) {
        _tab.value = index
    }
}
