package nl.thijsdh.moneytimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.TimeText
import nl.thijsdh.moneytimer.ui.theme.MoneyTimerTheme
import nl.thijsdh.moneytimer.util.Storage

class MainActivity : ComponentActivity() {
    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = Storage(this)
        setContent {
            MoneyTimerTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PageScroll(storage)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageScroll(storage: Storage) {
    val pagerState = rememberPagerState(initialPage = if (storage.hourlyRate == 0f) 0 else 1)

    TimeText()
    HorizontalPager(state = pagerState, pageCount = 2) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (it) {
                0 -> HourlyRatePicker(storage.hourlyRate) { value ->
                    storage.hourlyRate = value
                }

                1 -> Timer(storage)
                else -> error("Unknown page index")
            }
        }
    }
}

