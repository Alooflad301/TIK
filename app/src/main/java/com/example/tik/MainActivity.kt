package com.example.tik

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var etBillAmount: EditText
    private lateinit var etTipPercent: EditText
    private lateinit var etPeopleCount: EditText
    private lateinit var rgRounding: RadioGroup
    private lateinit var rbDown: RadioButton
    private lateinit var rbUp: RadioButton
    private lateinit var rbNearest: RadioButton
    private lateinit var btnCalculate: Button
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvPerPerson: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBillAmount = findViewById(R.id.etBillAmount)
        etTipPercent = findViewById(R.id.etTipPercent)
        etPeopleCount = findViewById(R.id.etPeopleCount)
        rgRounding = findViewById(R.id.rgRounding)
        rbDown = findViewById(R.id.rbDown)
        rbUp = findViewById(R.id.rbUp)
        rbNearest = findViewById(R.id.rbNearest)
        btnCalculate = findViewById(R.id.btnCalculate)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvPerPerson = findViewById(R.id.tvPerPerson)

        btnCalculate.setOnClickListener {
            calculateTips()
        }
    }

    private fun calculateTips() {
        // Получение и проверка данных
        val billStr = etBillAmount.text.toString()
        val tipStr = etTipPercent.text.toString()
        val peopleStr = etPeopleCount.text.toString()

        var billAmount: Double
        var tipPercent: Int
        var peopleCount: Int

        // Проверка суммы счета
        if (billStr.isBlank()) {
            showError("Пожалуйста, введите сумму счета.")
            return
        }
        try {
            billAmount = billStr.toDouble()
            if (billAmount < 0) {
                showError("Сумма счета не может быть отрицательной.")
                return
            }
        } catch (e: NumberFormatException) {
            showError("Некорректная сумма счета.")
            return
        }

        // Проверка процента чаевых
        if (tipStr.isBlank()) {
            tipPercent = 15 // по умолчанию
        } else {
            try {
                tipPercent = tipStr.toInt()
                if (tipPercent < 5 || tipPercent > 30) {
                    showError("Процент чаевых должен быть от 5 до 30.")
                    return
                }
            } catch (e: NumberFormatException) {
                showError("Некорректный процент чаевых.")
                return
            }
        }

        // Проверка количества человек
        if (peopleStr.isBlank()) {
            peopleCount = 1 // по умолчанию
        } else {
            try {
                peopleCount = peopleStr.toInt()
                if (peopleCount < 1 || peopleCount > 20) {
                    showError("Количество человек должно быть от 1 до 20.")
                    return
                }
            } catch (e: NumberFormatException) {
                showError("Некорректное количество человек.")
                return
            }
        }

        // Расчет чаевых
        val tipRaw = billAmount * tipPercent / 100.0

        // Округление
        val selectedId = rgRounding.checkedRadioButtonId
        val roundedTip = when (selectedId) {
            R.id.rbDown -> floor(tipRaw)
            R.id.rbUp -> ceil(tipRaw)
            R.id.rbNearest -> round(tipRaw)
            else -> round(tipRaw)
        }

        // Форматирование результата
        val tipStrFormatted = String.format("%.2f", roundedTip)
        val totalSum = billAmount + roundedTip
        val totalSumStr = String.format("%.2f", totalSum)
        val perPerson = totalSum / peopleCount
        val perPersonStr = String.format("%.2f", perPerson)

        // Отображение результатов
        val tipDisplay = if (selectedId == R.id.rbNearest) {
            // если округление до целого, показываем с двумя нулями
            String.format("%.2f", roundedTip)
        } else {
            tipStrFormatted
        }

        tvTipAmount.text = "Сумма чаевых: $tipDisplay"
        tvTotalAmount.text = "Общая сумма: $totalSumStr"
        tvPerPerson.text = "На каждого: $perPersonStr"
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}