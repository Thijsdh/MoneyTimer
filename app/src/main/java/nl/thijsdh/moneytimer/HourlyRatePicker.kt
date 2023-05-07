package nl.thijsdh.moneytimer

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.rememberPickerState

var selectedColumn = mutableStateOf(0)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Option(column: Int, text: String, style: TextStyle) = Box(modifier = Modifier.fillMaxSize()) {
    Text(
        text = text, style = style,
        color = if (selectedColumn.value == column) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .align(Alignment.Center)
            .wrapContentSize()
            .pointerInteropFilter {
                if (it.action == MotionEvent.ACTION_DOWN) selectedColumn.value = column
                true
            }
    )
}

@Preview(showBackground = true)
@Composable
fun HourlyRatePickerPreview() {
    HourlyRatePicker(0f) {}
}

@Composable
fun HourlyRatePicker(initialValue: Float, onValueChange: (Float) -> Unit) {
    val currencySymbol = stringResource(id = R.string.currency_symbol)
    val decimalSeparator = stringResource(id = R.string.decimal_separator)

    val textStyle = MaterialTheme.typography.displayMedium
    val textStyleSmall = MaterialTheme.typography.displaySmall

    val valueWhole: Int = initialValue.toInt()
    val valueDecimals: Int = ((initialValue - valueWhole) * 100).toInt()

    val pickerRange = 0..999
    val pickerState = rememberPickerState(
        initialNumberOfOptions = pickerRange.count(),
        initiallySelectedOption = valueWhole,
        repeatItems = false
    )

    val decimalPickerRange = 0..99
    val decimalPickerState = rememberPickerState(
        initialNumberOfOptions = decimalPickerRange.count(),
        initiallySelectedOption = valueDecimals,
        repeatItems = true,
    )

    fun updateValue() {
        val newValue = pickerState.selectedOption + decimalPickerState.selectedOption / 100f
        onValueChange(newValue)
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = currencySymbol,
            style = textStyleSmall
        )
        Picker(
            readOnly = selectedColumn.value != 0,
            state = pickerState,
            contentDescription = "",
            gradientColor = MaterialTheme.colorScheme.background,
            separation = -(12.dp),
            modifier = Modifier
                .size(78.dp, 128.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            updateValue()
            Option(column = 0, text = it.toString(), style = textStyle)
        }
        Text(
            text = decimalSeparator,
            style = textStyleSmall
        )
        Picker(
            readOnly = selectedColumn.value != 1,
            state = decimalPickerState,
            contentDescription = "",
            gradientColor = MaterialTheme.colorScheme.background,
            separation = -(8.dp),
            modifier = Modifier
                .size(42.dp, 128.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            updateValue()
            Option(column = 1, text = "%02d".format(it), style = textStyleSmall)
        }
    }
}
