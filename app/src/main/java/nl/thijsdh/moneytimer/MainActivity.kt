package nl.thijsdh.moneytimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import nl.thijsdh.moneytimer.databinding.ActivityMainBinding
import nl.thijsdh.moneytimer.fragments.HourlyRateFragment
import nl.thijsdh.moneytimer.fragments.TimerFragment
import nl.thijsdh.moneytimer.util.Storage

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = Storage(this)

        val viewPager2 = binding.viewPager
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            TimerFragment.newInstance(storage),
            HourlyRateFragment.newInstance(storage)
        )
        viewPager2.adapter = ViewPagerAdapter(this, fragmentList)
    }
}