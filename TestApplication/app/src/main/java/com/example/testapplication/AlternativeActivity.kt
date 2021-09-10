package com.example.testapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class AlternativeActivity : AppCompatActivity() {
    lateinit var tvResult: TextView
    lateinit var btnResult: Button
    lateinit var btnClear: Button
    lateinit var btnChangeLayout:Button
    lateinit var yearSpinner:Spinner
    lateinit var monthSpinner:Spinner
    lateinit var daySpinner: Spinner
    var d:Int=0;var m:Int=0;var y:Int=0
    val days = mutableListOf<Int>()
    val years = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alternative_layout)
        fillDaysAndYears()
        tvResult=findViewById(R.id.tvResult)
        btnResult=findViewById(R.id.btnResult)
        btnClear=findViewById(R.id.btnClear)
        btnChangeLayout = findViewById(R.id.btnChangeLayout)
        yearSpinner=findViewById(R.id.yearSpinner)
        monthSpinner=findViewById(R.id.monthSpinner)
        daySpinner=findViewById(R.id.daySpinner)

        val dayAdapter:ArrayAdapter<Int> = ArrayAdapter<Int>(this,R.layout.support_simple_spinner_dropdown_item,days);daySpinner.adapter=dayAdapter;daySpinner.prompt="Days"
        val monthAdapter:ArrayAdapter<String> = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,months);monthSpinner.adapter=monthAdapter;monthSpinner.prompt="Months"
        val yearsAdapter:ArrayAdapter<Int> = ArrayAdapter<Int>(this,R.layout.support_simple_spinner_dropdown_item,years);yearSpinner.adapter=yearsAdapter;yearSpinner.prompt="Years"

        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                d=days[position]
                Log.d(lt,"Position:$position ; day: $d")
            }
            override  fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        monthSpinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parrent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                m=position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        yearSpinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                y=years[position]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }
    private fun fillDaysAndYears() {
        for (i in 1..31) {
            days.add(i)
        }
        for (j in 1946..2012){
            years.add(j)
        }
    }
    fun onClickClear(v: View){
        daySpinner.setSelection(0)
        monthSpinner.setSelection(0)
        yearSpinner.setSelection(0)
    }
    fun onClickResult(v: View){
        try {
            tvResult.text=calculateResult()

        }
        catch (e:MyException){
            daySpinner.setSelection(0);Log.d("MyLog","${e.name} exception and id: ${e.id} ")
            Toast.makeText(this,"Impossible day value for ${months[m]}, please, try again",Toast.LENGTH_LONG).show()
        }
    }
    fun saveLastResult(){
        Log.d(lt, "Alt save")
        getSharedPreferences("save", MODE_PRIVATE).edit().putString(saveTag,tvResult.text.toString()).commit()
    }
    fun loadSavedResult(){
        Log.d(lt,"AltLoad")
       // var a = intent.getStringExtra("result")
        val a = getSharedPreferences("save", MODE_PRIVATE)?.getString(saveTag,"")
        if (a!=null){
            tvResult.text=a
        }
    }
    override fun onResume() {
        super.onResume()
        loadSavedResult()
    }
    override fun onPause() {
        super.onPause()
       saveLastResult()
    }
    fun onClickLayout(v: View){
        finish()
    }
    fun calculateResult():String{
        if (m==1){
            if (y%4==0){
                if (d>29){
                    Log.d(lt,"Day format exception in alt")
                    throw(MyException("Day value", dayErrorCode))}
            }
            else{
                if (d>28){
                    Log.d(lt,"Day format exception in alt")
                    throw(MyException("Day value", dayErrorCode))}
            }
        }else {
            if ((m+1)%2==0){ if (d+1>30){
                Log.d(lt,"Day format exception in alt")
                throw(MyException("Day value", dayErrorCode))}}

        }
        val generation=when(y){
            in 1946..1964 ->{
                "Baby-boomer generation" }
            in 1965..1980 ->{
                "X generation"}
            in 1981..1996->{
                "Y generation"}
            in 1997..2012->{
                "Z generation"}
            else -> ""
        }
        return "Person, born $d ${months[m]} $y belongs to the $generation "
    }
}