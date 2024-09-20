package com.localsearch.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.localsearch.R

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // 엔터 키를 '완료'로 설정
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("비밀번호") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    )
}