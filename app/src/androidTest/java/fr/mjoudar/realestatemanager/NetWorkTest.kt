package fr.mjoudar.realestatemanager

import androidx.test.platform.app.InstrumentationRegistry
import fr.mjoudar.realestatemanager.utils.Utils.isInternetAvailable
import junit.framework.Assert.assertTrue
import org.junit.Test

class NetWorkTest {

    @Test
    fun should_check_internet_availability() {
        val context = InstrumentationRegistry.getInstrumentation().context
        assertTrue(isInternetAvailable(context))
    }
}