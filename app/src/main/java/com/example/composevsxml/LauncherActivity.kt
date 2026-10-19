package com.example.composevsxml

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.composevsxml.compose.ComposeDemoActivity
import com.example.composevsxml.views.XmlDemoActivity

/** Стартовый экран: открывает одну и ту же страницу в двух реализациях. */
class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
        }
        root.addView(Button(this).apply {
            text = "Jetpack Compose"
            setOnClickListener { startActivity(Intent(context, ComposeDemoActivity::class.java)) }
        })
        root.addView(Button(this).apply {
            text = "XML + View System"
            setOnClickListener { startActivity(Intent(context, XmlDemoActivity::class.java)) }
        })
        setContentView(root)
    }
}
