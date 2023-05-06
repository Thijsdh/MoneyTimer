package nl.thijsdh.moneytimer.util

import android.content.Context
import android.content.SharedPreferences

class Storage(context: Context) {
    companion object {
        private const val PREF_NAME = "timer_prefs"
        private const val KEY_START_TIMESTAMP = "start_timestamp"
        private const val KEY_TOTAL_TIME = "total_time"
        private const val KEY_TIMER_STATE = "timer_state"
        private const val KEY_HOURLY_RATE = "hourly_rate"

        enum class TimerState {
            STOPPED, RUNNING, PAUSED
        }
    }

    private val mPrefs: SharedPreferences

    init {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var startTimestamp: Long
        get() = mPrefs.getLong(KEY_START_TIMESTAMP, 0)
        set(timestamp) {
            mPrefs.edit().putLong(KEY_START_TIMESTAMP, timestamp).apply()
        }

    var totalTime: Long
        get() = mPrefs.getLong(KEY_TOTAL_TIME, 0)
        set(totalTime) {
            mPrefs.edit().putLong(KEY_TOTAL_TIME, totalTime).apply()
        }

    var timerState: TimerState
        get() = TimerState.valueOf(mPrefs.getString(KEY_TIMER_STATE, TimerState.STOPPED.name)!!)
        set(timerState) {
            mPrefs.edit().putString(KEY_TIMER_STATE, timerState.name).apply()
        }

    var hourlyRate: Float
        get() = mPrefs.getFloat(KEY_HOURLY_RATE, 0f)
        set(hourlyRate) {
            mPrefs.edit().putFloat(KEY_HOURLY_RATE, hourlyRate).apply()
        }

}