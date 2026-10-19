# compose-vs-xml-demo

Одна и та же страница Android-приложения, реализованная двумя способами: **Jetpack Compose** и **XML + View System**.
Репозиторий — иллюстрация к статье «Сравнительный анализ Jetpack Compose и XML при разработке пользовательского интерфейса Android-приложения ReelsBlocker».

## Что в демо

Экран «Настройки»: две кликабельные строки (иконка, заголовок, подзаголовок), два переключателя,
кнопка-счетчик, строка статуса и нижняя навигация из трех пунктов.

Состояние и логика общие для обеих реализаций — `shared/DemoViewModel.kt` (StateFlow). Отличается только слой интерфейса:

| | Jetpack Compose | XML + View System |
|---|---|---|
| Экран | `compose/DemoScreen.kt` | `res/layout/activity_xml_demo.xml`, `item_setting_row.xml`, `item_switch_row.xml`, `res/menu/bottom_nav.xml` |
| Связывание и обработчики | внутри composable-функций | `views/XmlDemoActivity.kt` (ViewBinding) |

Один из переключателей («Блокировка по времени») намеренно **отклоняет** включение: ViewModel оставляет значение `false`.
В Compose переключатель остается выключенным сам, поскольку состояние — единственный источник истины;
в XML-варианте его нужно вернуть вручную (см. `bindSwitch` в `XmlDemoActivity.kt`).

## Сравнение кода: строка настроек с переключателем

**Compose** (`DemoScreen.kt`):

```kotlin
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
// использование (состояние берется из ViewModel):
SwitchRow("Уведомления", "Показывать оповещения", notifications, vm::onNotificationsChanged)
```

**XML + View System**: разметка `item_switch_row.xml` (32 строки), а также код связывания в `XmlDemoActivity.kt`:

```kotlin
row.switchView.setOnClickListener {
    onChange(row.switchView.isChecked)
    row.switchView.isChecked = current() // ViewModel мог отклонить изменение
}
// ...и подписка на состояние с учетом жизненного цикла:
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch { vm.notifications.collect { binding.rowNotifications.switchView.isChecked = it } }
    }
}
```

## Подсчет строк кода

```
python3 tools/count_loc.py
```

Считаются логические строки (без пустых, комментариев, `import`/`package`, `<?xml ?>`); векторные иконки, `LauncherActivity`, общий `DemoViewModel`, манифест и сборка не учитываются.

| | Compose | XML + View System |
|---|---|---|
| Файлов | 2 | 5 |
| Строк кода | 79 | 187 |

> **Важно.** Это учебный пример «с чистого листа» на стандартном оформлении Material 3, где Compose использует цикл для пунктов навигации,
> а XML описывает каждый элемент явно. Он иллюстрирует различия в подходах, но **не является репрезентативным измерением**:
> в реальном приложении ReelsBlocker (см. статью) разница заметно меньше, а для нижней навигации XML-вариант оказался короче.

## Запуск

1. Откройте проект в Android Studio.
2. Выполните Sync и запустите конфигурацию `app` на эмуляторе или устройстве (minSdk 24).
3. На стартовом экране выберите реализацию: **Jetpack Compose** или **XML + View System**.

Версии инструментов (AGP, Kotlin, Compose BOM) заданы в `gradle/libs.versions.toml`.
