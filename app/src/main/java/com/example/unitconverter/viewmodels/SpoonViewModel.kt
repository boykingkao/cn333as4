package com.example.unitconverter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unitconverter.R
import java.lang.NumberFormatException

class SpoonViewModel: ViewModel() {
    private val _spoon: MutableLiveData<Int> = MutableLiveData(R.string.table)

    val spoon : LiveData<Int>
        get() = _spoon

    fun setSpoon(value: Int){
        _spoon.value = value
    }

    private val _spoonunit: MutableLiveData<String> = MutableLiveData("")

    val spoonUnit: LiveData<String>
        get() = _spoonunit

    fun getSpoonUnitAsFloat(): Float= (_spoonunit.value?:"").let{
        return try {
            it.toFloat()
        } catch(e: NumberFormatException) {
            Float.NaN
        }
    }

    fun setSpoonUnit(value: String){
        _spoonunit.value = value
    }

    fun convert() = getSpoonUnitAsFloat().let {
        if (!it.isNaN())
            if (_spoon.value == R.string.table)
                it * 15
            else
                it / 15
        else
            return Float.NaN
    }
}