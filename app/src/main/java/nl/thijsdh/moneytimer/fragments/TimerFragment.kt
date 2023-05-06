package nl.thijsdh.moneytimer.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import nl.thijsdh.moneytimer.R
import nl.thijsdh.moneytimer.util.Storage
import nl.thijsdh.moneytimer.util.Storage.Companion.TimerState

class TimerFragment(private val storage: Storage) : Fragment() {
    private val TIMER_INTERVAL = 100L

    private lateinit var startPauseBtn: ImageButton
    private lateinit var stopBtn: ImageButton
    private lateinit var moneyTextView: TextView

    private val time: Long
        get() = storage.totalTime + (if (storage.timerState == TimerState.RUNNING)
            System.currentTimeMillis() - storage.startTimestamp else 0L)

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            handler.postDelayed(this, TIMER_INTERVAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        startPauseBtn = view.findViewById(R.id.start_pause_button)
        stopBtn = view.findViewById(R.id.stop_button)
        moneyTextView = view.findViewById(R.id.money)

        startPauseBtn.setOnClickListener(onStartPauseButtonPressed)
        stopBtn.setOnClickListener(onStopButtonPressed)

        updateButtons()

        handler.postDelayed(updateTimerRunnable, TIMER_INTERVAL)

        return view
    }

    private fun updateTimer() {
        val currencySymbol = getString(R.string.currency_symbol)
        val money = time / 1000.0 / 60.0 / 60.0 * storage.hourlyRate
        moneyTextView.text = "%s%.3f".format(currencySymbol, money)
    }

    private fun updateButtons() {
        when (storage.timerState) {
            TimerState.STOPPED -> {
                startPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                stopBtn.visibility = View.GONE
            }

            TimerState.PAUSED -> {
                startPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                stopBtn.visibility = View.VISIBLE
            }

            else -> {
                startPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                stopBtn.visibility = View.GONE
            }
        }
    }

    private val onStartPauseButtonPressed = View.OnClickListener {
        if (storage.timerState === TimerState.STOPPED || storage.timerState === TimerState.PAUSED) {
            if (storage.timerState === TimerState.STOPPED) storage.totalTime = 0
            storage.timerState = TimerState.RUNNING
            storage.startTimestamp = System.currentTimeMillis()

        } else {
            storage.timerState = TimerState.PAUSED
            storage.totalTime += System.currentTimeMillis() - storage.startTimestamp
        }
        updateButtons()
    }

    private val onStopButtonPressed = View.OnClickListener {
        storage.timerState = TimerState.STOPPED
        storage.totalTime = 0
        updateButtons()
    }

    companion object {
        @JvmStatic
        fun newInstance(storage: Storage): TimerFragment = TimerFragment(storage)
    }
}
