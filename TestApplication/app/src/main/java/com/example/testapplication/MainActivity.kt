package com.example.testapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
const val lt = "MyLog"
const val formatErrorCode=1
const val yearErrorCode=2
const val monthErrorCode=3
const val dayErrorCode=4
const val saveTag="Save"
val months = listOf("January","February","March","April","May","June","July","August","September","October","November","December")

class MainActivity : AppCompatActivity() {
    lateinit var tvResult:TextView
    lateinit var dateET:EditText
    lateinit var btnResult:Button
    lateinit var btnClear:Button
    lateinit var savePref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult=findViewById(R.id.tvResult)
        dateET=findViewById(R.id.dayET)
        btnClear=findViewById(R.id.btnClear)
        btnResult=findViewById(R.id.btnResult)
    }
    fun onClickClear(v:View){
        clearAll()
    }
    fun onClickResult(v:View){
        Log.d(lt,"Result")
        try{ tvResult.text=checkAge(dateET.text.toString())}
        catch(e:MyException){
            when (e.id){
                1->{dateET.text.clear();Log.d("MyLog","${e.name} exception and id: ${e.id} ")
                Toast.makeText(this,"Wrong date format, please, try again",Toast.LENGTH_LONG).show()}
                2->{dateET.text.clear();Log.d("MyLog","${e.name} exception and id: ${e.id} ")
                    Toast.makeText(this,"I don't know this year's generation",Toast.LENGTH_LONG).show()}
                3->{dateET.text.clear();Log.d("MyLog","${e.name} exception and id: ${e.id} ")
                    Toast.makeText(this,"Impossible month value, please, try again",Toast.LENGTH_LONG).show()}
                4->{dateET.text.clear();Log.d("MyLog","${e.name} exception and id: ${e.id} ")
                    Toast.makeText(this,"Impossible day value for this month, please, try again",Toast.LENGTH_LONG).show()}
            }
        }
    }
    fun saveLastResult(){
        savePref=getSharedPreferences("save",MODE_PRIVATE)
        val editor = savePref.edit()
        editor.putString(saveTag,tvResult.text.toString())
        editor.commit()
        Log.d(lt,"Saved prev result")
    }
    fun loadSavedResult(){
        savePref=getSharedPreferences("save",MODE_PRIVATE)
        val s =savePref.getString(saveTag,"")
        if (s!=null){
            tvResult.text =s
        }
        Log.d(lt,"Loaded prev result")
    }
    override fun onResume() {
        super.onResume()
        loadSavedResult()
    }
    override fun onPause() {
        super.onPause()
        saveLastResult()
    }
    fun onClickLayout(v:View){
        Log.d(lt,"Changing")
        val intent = Intent(this, AlternativeActivity::class.java)
        intent.putExtra("result",tvResult.text.toString())
        startActivity(intent)
    }
    fun checkAge(s:String):String{
        val str_bd = s.split(".").toTypedArray()
        if (str_bd.size!=3){
            val formatException = MyException("Wrong format", formatErrorCode)
            throw formatException
        }
        if ((str_bd[2].toInt()>2012)||(str_bd[2].toInt()<1946)){
            val yearException = MyException("Wrong year value", yearErrorCode)
            throw yearException
        }
        if ((str_bd[1].toInt() <1)||(str_bd[1].toInt()>12)){
            val monthException = MyException("Wrong month value", monthErrorCode)
            throw monthException
        }
        val dayException = MyException("Wrong day value", dayErrorCode)
        if (str_bd[0].toInt()>0){

            if (str_bd[1].toInt()==3){
                if (str_bd[2].toInt()%4==0){
                    if (str_bd[0].toInt()>29){
                        throw dayException
                    }
                }
                else{if (str_bd[0].toInt()>28){
                    throw dayException
                }
                }
            }
            else{
                if (str_bd[2].toInt()%2!=0){
                    if (str_bd[0].toInt()>31){
                        throw dayException
                    }
                }
                else{
                    if (str_bd[0].toInt()>30){
                        throw dayException
                    }
                }
            }
        }
        else{
            throw dayException
        }
        val generation = when(str_bd[2].toInt()){
            in 1946..1964 ->{
                "Baby-boomer generation"
            }
            in 1965..1980 ->{
                "X generation"
            }
            in 1981..1996->{
                "Y generation"
            }
            in 1997..2012->{
                "Z generation"
            }
            else->{ throw(MyException("Something wrong",1)) }
        }
        return "Person, born ${str_bd[0]} ${months[str_bd[1].toInt()-1]} ${str_bd[2]} belongs to the $generation "
    }
    fun clearAll(){
        Log.d(lt,"Cleaning")
        dateET.text.clear()
    }
}
class MyException(n:String, num:Int):Exception()
{
    var name:String = n
    var id:Int = num
}