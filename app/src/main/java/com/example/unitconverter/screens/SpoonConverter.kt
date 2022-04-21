package com.example.unitconverter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unitconverter.R
import com.example.unitconverter.viewmodels.SpoonViewModel


@Composable
fun SpoonConverter(){
    val viewModel: SpoonViewModel = viewModel()
    val strTable = stringResource(id = R.string.table)
    val strGram = stringResource(id = R.string.gram)
    val currentValue = viewModel.spoonUnit.observeAsState(viewModel.spoonUnit.value ?: "")
    val scale = viewModel.spoon.observeAsState(viewModel.spoon.value ?: R.string.table)
    var result by rememberSaveable{ mutableStateOf("")}

    val enabled by remember(currentValue.value){
        mutableStateOf(!viewModel.getSpoonUnitAsFloat().isNaN())
    }

    val calc = {
        val temp = viewModel.convert()
        result = if (temp.isNaN()) ""
        else "$temp${
            if (scale.value == R.string.table)
                strGram
            else strTable
        }"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment
            .CenterHorizontally,
    ){
        SpoonTextField(spoonUnit = currentValue,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc,
            viewModel = viewModel
        )
        SpoonScaleButtonGroup(selected = scale,
            modifier = Modifier.padding(bottom = 16.dp)
        ){
                resId: Int ->
            viewModel.setSpoon(resId)
        }
        Button(
            onClick = calc,
            enabled = enabled
        ){
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()){
            Text(
                text = result,
                style = MaterialTheme.typography.h3
            )
        }
    }
}

@Composable
fun SpoonTextField(
    spoonUnit: State<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit,
    viewModel: SpoonViewModel
){
    TextField(
        value = spoonUnit.value,
        onValueChange = {
            viewModel.setSpoonUnit(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder_spoon))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun SpoonScaleButtonGroup(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
){
    val sel = selected.value
    Row(modifier = modifier){
        SpoonRadioButton(
            selected = sel == R.string.table,
            resId = R.string.table,
            onClick = onClick
        )
        SpoonRadioButton(
            selected = sel == R.string.gram,
            resId = R.string.gram,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun SpoonRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        RadioButton(selected = selected,
            onClick = { onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier
                .padding(start = 8.dp)
        )

    }

}