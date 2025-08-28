package com.example.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.ui.theme.CalculatorTheme
import android.widget.Button
import android.util.Log

class MainActivity : ComponentActivity() {
    private lateinit var For: TextView
    private lateinit var Res: TextView
    private val TAG="MainActivity" //ログ用
    private var formula =""
    private var currentInput :String = ""
    private var operators:String?=null
    private var firstValue:Double?=null
    private var old_val:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)

        Res = findViewById(R.id.result)
        Res.text = "0"
        For = findViewById(R.id.formula)
        For.text = ""

        val numberButtons = listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {v->
                if(currentInput==formula&&old_val==""){
                    clearAll()
                }
                val digit = (v as Button).text
                currentInput += digit
                Res.text = currentInput
                formula += digit
                For.text = formula
                old_val=currentInput
                Log.d(TAG, "currentInput: $currentInput")
            }
        }

        val ops=mapOf(R.id.btnAdd to "+", R.id.btnSub to "-", R.id.btnMul to "*", R.id.btnDiv to "/")

        for((id,op) in ops){
            findViewById<Button>(id).setOnClickListener {v->
                if(currentInput.isNotEmpty()&&firstValue!=null&&operators!=null){
                    val secondValue = currentInput.toDouble()
                    if(secondValue!=null){
                        firstValue = calculate(firstValue!!,secondValue,operators!!)
                        currentInput = firstValue.toString()
                        Res.text = currentInput
                    }
                }else{
                    firstValue=currentInput.toDouble()
                }
                operators=op
                currentInput = ""
                formula += (v as Button).text.toString()
                For.text = formula
                old_val=currentInput
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            val secondValue = currentInput.toDouble()
            if(firstValue!=null && operators!=null&&secondValue!=null){
                currentInput = calculate(firstValue!!,secondValue,operators!!).toString()
                Res.text = currentInput
                formula = currentInput
                For.text = formula
                firstValue=null
                operators=null
                old_val=""
            }
        }
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearAll()
        }

    }
    private fun clearAll(){
        Res.text = "0"
        For.text = ""
        formula = ""
        currentInput = ""
        operators = null
        firstValue = null
        old_val=""
    }
    private fun calculate(firstValue:Double, secondValue:Double, operator:String):Double{
       return when(operator){
            "+" -> firstValue + secondValue
            "-" -> firstValue - secondValue
            "*" -> firstValue * secondValue
            "/" -> firstValue / secondValue
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }
}
