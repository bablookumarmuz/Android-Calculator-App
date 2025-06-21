package com.coder.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.coder.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    var number: String? = null

    var firstNumber: Double = 0.0
    var lastNumber: Double = 0.0

    var status: String? = null
    var operator: Boolean = false

    val myFormatter = DecimalFormat("######.######")

    var history: String = ""
    var currentResult: String = ""

    var dotControl: Boolean = true
    var buttonEqualsControl: Boolean = false

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before super.onCreate
        val sharedPrefs = getSharedPreferences("Dark Theme", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("switch", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Setup toolbar
        setSupportActionBar(mainBinding.toolbar)

        mainBinding.textViewResult.text = "0"

        // Number buttons
        mainBinding.btnZero.setOnClickListener { onNumberClicked("0") }
        mainBinding.btnOne.setOnClickListener { onNumberClicked("1") }
        mainBinding.btnTwo.setOnClickListener { onNumberClicked("2") }
        mainBinding.btnThree.setOnClickListener { onNumberClicked("3") }
        mainBinding.btnFour.setOnClickListener { onNumberClicked("4") }
        mainBinding.btnFive.setOnClickListener { onNumberClicked("5") }
        mainBinding.btnSix.setOnClickListener { onNumberClicked("6") }
        mainBinding.btnSeven.setOnClickListener { onNumberClicked("7") }
        mainBinding.btnEight.setOnClickListener { onNumberClicked("8") }
        mainBinding.btnNine.setOnClickListener { onNumberClicked("9") }

        mainBinding.btnAC.setOnClickListener { onButtonACClicked() }

        mainBinding.btnDel.setOnClickListener {
            number?.let {
                if (it.length == 1) {
                    onButtonACClicked()
                } else {
                    number = it.substring(0, it.length - 1)
                    mainBinding.textViewResult.text = number
                    dotControl = !number!!.contains(".")
                }
            }
        }

        mainBinding.btnDivide.setOnClickListener { onOperatorClicked("/") }
        mainBinding.btnMulti.setOnClickListener { onOperatorClicked("*") }
        mainBinding.btnMinus.setOnClickListener { onOperatorClicked("-") }
        mainBinding.btnPlus.setOnClickListener { onOperatorClicked("+") }

        mainBinding.btnEqual.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()

            if (operator) {
                calculate()
                mainBinding.textViewHistory.text = history.plus(currentResult)
                    .plus("=")
                    .plus(mainBinding.textViewResult.text.toString())
            }

            operator = false
            dotControl = true
            buttonEqualsControl = true
        }

        mainBinding.btnDot.setOnClickListener {
            if (dotControl) {
                number = if (number == null) {
                    "0."
                } else if (buttonEqualsControl) {
                    if (mainBinding.textViewResult.text.toString().contains(".")) {
                        mainBinding.textViewResult.text.toString()
                    } else {
                        mainBinding.textViewResult.text.toString().plus(".")
                    }
                } else {
                    "$number."
                }
                mainBinding.textViewResult.text = number
            }
            dotControl = false
        }
    }

    private fun onOperatorClicked(symbol: String) {
        history = mainBinding.textViewHistory.text.toString()
        currentResult = mainBinding.textViewResult.text.toString()
        mainBinding.textViewHistory.text = history.plus(currentResult).plus(symbol)

        if (operator) {
            calculate()
        }

        status = when (symbol) {
            "/" -> "division"
            "*" -> "multiplication"
            "-" -> "subtraction"
            "+" -> "addition"
            else -> null
        }

        operator = false
        number = null
        dotControl = true
    }

    private fun calculate() {
        when (status) {
            "multiplication" -> multiply()
            "division" -> divide()
            "subtraction" -> minus()
            "addition" -> plus()
            else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
        }
    }

    private fun onButtonACClicked() {
        number = null
        status = null
        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""
        firstNumber = 0.0
        lastNumber = 0.0
        dotControl = true
        buttonEqualsControl = false
    }

    private fun onNumberClicked(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else if (buttonEqualsControl) {
            number = if (dotControl) {
                clickedNumber
            } else {
                mainBinding.textViewResult.text.toString().plus(clickedNumber)
            }

            firstNumber = number!!.toDouble()
            lastNumber = 0.0
            status = null
            mainBinding.textViewHistory.text = ""
        } else {
            number += clickedNumber
        }
        mainBinding.textViewResult.text = number

        operator = true
        buttonEqualsControl = false
    }

    private fun plus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber += lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    private fun minus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber -= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    private fun multiply() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber *= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    private fun divide() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()

        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "The divisor cannot be zero", Toast.LENGTH_LONG)
                .show()
        } else {
            firstNumber /= lastNumber
            mainBinding.textViewResult.text = myFormatter.format(firstNumber)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_item -> {
                val intent = Intent(this, ChangeThemeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()

        sharedPreferences = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("result", mainBinding.textViewResult.text.toString())
        editor.putString("history", mainBinding.textViewHistory.text.toString())
        editor.putString("number", number)
        editor.putString("status", status)
        editor.putBoolean("operator", operator)
        editor.putBoolean("dot", dotControl)
        editor.putBoolean("equal", buttonEqualsControl)
        editor.putString("first", firstNumber.toString())
        editor.putString("last", lastNumber.toString())

        editor.apply()
    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)

        mainBinding.textViewResult.text = sharedPreferences.getString("result", "0")
        mainBinding.textViewHistory.text = sharedPreferences.getString("history", "")

        number = sharedPreferences.getString("number", null)
        status = sharedPreferences.getString("status", null)
        operator = sharedPreferences.getBoolean("operator", false)
        dotControl = sharedPreferences.getBoolean("dot", true)
        buttonEqualsControl = sharedPreferences.getBoolean("equal", false)
        firstNumber = sharedPreferences.getString("first", "0.0")!!.toDouble()
        lastNumber = sharedPreferences.getString("last", "0.0")!!.toDouble()
    }
}
