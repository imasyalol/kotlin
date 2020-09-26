package com.example.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_0.setOnClickListener { setTextFields("0") }
        btn_1.setOnClickListener { setTextFields("1") }
        btn_2.setOnClickListener { setTextFields("2") }
        btn_3.setOnClickListener { setTextFields("3") }
        btn_4.setOnClickListener { setTextFields("4") }
        btn_5.setOnClickListener { setTextFields("5") }
        btn_6.setOnClickListener { setTextFields("6") }
        btn_7.setOnClickListener { setTextFields("7") }
        btn_8.setOnClickListener { setTextFields("8") }
        btn_9.setOnClickListener { setTextFields("9") }
        minus_btn.setOnClickListener { setTextFields("-") }
        plus_btn.setOnClickListener { setTextFields("+") }
        multiplication_btn.setOnClickListener { setTextFields("*") }
        slash_btn.setOnClickListener { setTextFields("/") }
        open_bracket_btn.setOnClickListener { setTextFields("(") }
        close_bracket_btn.setOnClickListener { setTextFields(")") }
        dot_btn.setOnClickListener{setTextFields(".")}

        ac_btn.setOnClickListener {
            math_operation.text = ""
            result_text.text = ""
        }

        back_btn.setOnClickListener {
            val str = math_operation.text.toString()
            if(str.isNotEmpty())
                math_operation.text = str.substring(0, str.length - 1)

            result_text.text = ""
        }
        equal_btn.setOnClickListener { try {
            val ex = ExpressionBuilder(math_operation.text.toString()).build()
            val result = ex.evaluate()
            val longRes = result.toLong()
            if (result ==longRes.toDouble())
                result_text.text = longRes.toString()
            else
                result_text.text = result.toString()
        } catch (e:Exception) {
            Log.d("Ошибка", "сообщение: ${e.message}") }
        }
    }
    fun setTextFields(str: String) {
        if (result_text.text.isNotEmpty()) {
            math_operation.text = result_text.text
            result_text.text = ""
        }
        math_operation.append(str)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("math_operation", math_operation.text.toString())
            putString("result_text", result_text.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        math_operation.text = savedInstanceState.getString("math_operation")
        result_text.text = savedInstanceState.getString("result_text")
    }
}