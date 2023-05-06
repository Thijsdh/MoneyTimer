package nl.thijsdh.moneytimer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import nl.thijsdh.moneytimer.R
import nl.thijsdh.moneytimer.util.Storage

class HourlyRateFragment(private val storage: Storage) : Fragment() {

    private lateinit var hourlyRatePicker: NumberPicker
    private lateinit var hourlyRatePickerDecimal: NumberPicker

    private var value: Float
        get() = hourlyRatePicker.value.toFloat() + hourlyRatePickerDecimal.value.toFloat() / 100
        set(value) {
            hourlyRatePicker.value = value.toInt()
            hourlyRatePickerDecimal.value = ((value - value.toInt()) * 100).toInt()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hourly_rate, container, false)

        hourlyRatePicker = view.findViewById(R.id.hourly_rate_picker)
        hourlyRatePicker.minValue = 0
        hourlyRatePicker.maxValue = 1000
        hourlyRatePicker.wrapSelectorWheel = false
        hourlyRatePicker.setOnValueChangedListener { _, _, _ -> storage.hourlyRate = value }

        hourlyRatePickerDecimal = view.findViewById(R.id.hourly_rate_picker_decimal)
        hourlyRatePickerDecimal.minValue = 0
        hourlyRatePickerDecimal.maxValue = 99
        hourlyRatePickerDecimal.setOnValueChangedListener { _, _, _ -> storage.hourlyRate = value }

        value = storage.hourlyRate

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(storage: Storage) = HourlyRateFragment(storage)
    }
}