package nl.thijsdh.moneytimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import nl.thijsdh.moneytimer.util.Storage
import nl.thijsdh.moneytimer.util.Storage.Companion.TimerState

const val REFRESH_INTERVAL = 100L

@Composable
fun TimerButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun Timer(storage: Storage) {
    val time = remember { mutableStateOf(storage.totalTime) }
    val state = remember { mutableStateOf(storage.timerState) }

    fun start() {
        storage.timerState = TimerState.RUNNING
        storage.startTimestamp = System.currentTimeMillis()

        state.value = TimerState.RUNNING
    }

    fun pause() {
        storage.timerState = TimerState.PAUSED
        storage.totalTime = time.value

        state.value = TimerState.PAUSED
    }

    fun stop() {
        storage.timerState = TimerState.STOPPED
        storage.totalTime = 0

        time.value = 0
        state.value = TimerState.STOPPED
    }

    LaunchedEffect(key1 = true) {
        while (true) {
            if (state.value == TimerState.RUNNING) {
                time.value = storage.totalTime + System.currentTimeMillis() - storage.startTimestamp
            }
            delay(REFRESH_INTERVAL)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "%s%.3f".format(
                stringResource(R.string.currency_symbol),
                time.value / 1000.0 / 60.0 / 60.0 * storage.hourlyRate
            ),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 32.dp)
        )
        Row {
            if (state.value == TimerState.PAUSED) {
                TimerButton(
                    icon = Icons.Filled.Refresh,
                    onClick = { stop() },
                    contentDescription = stringResource(R.string.reset),
                    colors = ButtonDefaults.textButtonColors()
                )
            }
            if (state.value == TimerState.STOPPED || state.value == TimerState.PAUSED) {
                TimerButton(
                    icon = Icons.Filled.PlayArrow,
                    onClick = { start() },
                    contentDescription = stringResource(R.string.start),
                    enabled = storage.hourlyRate > 0
                )
            }
            if (state.value == TimerState.RUNNING) {
                TimerButton(
                    icon = Icons.Filled.Pause,
                    onClick = { pause() },
                    contentDescription = stringResource(R.string.pause)
                )
            }
        }
    }
}
