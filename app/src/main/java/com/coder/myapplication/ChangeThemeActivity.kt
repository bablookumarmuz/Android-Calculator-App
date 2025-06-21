package com.coder.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.coder.myapplication.databinding.ActivityChangeThemeBinding

class ChangeThemeActivity : AppCompatActivity() {

    lateinit var switchBinding: ActivityChangeThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchBinding = ActivityChangeThemeBinding.inflate(layoutInflater)
        setContentView(switchBinding.root)

        switchBinding.toolbar2.setNavigationOnClickListener { finish() }

        switchBinding.mySwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("switch", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("switch", false)
            }
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("switch", false)
        switchBinding.mySwitch.isChecked = isDark
    }
}
