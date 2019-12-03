package com.willdom.codechallenge

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.willdom.codechallenge.IdlingResource.LoadStoreIdlingResource
import com.willdom.codechallenge.activity.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    private var loadStoresIdlingResource: IdlingResource? = null

    @get:Rule
    var addTraderActivity: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)


    @Before
    fun initIdlResource() {
        loadStoresIdlingResource = LoadStoreIdlingResource()
    }

    @Test
    fun validateConnection() {
        IdlingRegistry.getInstance().register(loadStoresIdlingResource)

        if(isConnected(MyApplication.getAppContext())){
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.message_data_updated)))
        } else {
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.message_offline_data)))
        }
        IdlingRegistry.getInstance().unregister(loadStoresIdlingResource)


    }
    @Test
    fun validateDataDisplayed() {
        IdlingRegistry.getInstance().register(loadStoresIdlingResource)

        // Context of the app under test.
        onView(withId(R.id.store_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.go_maps))
            .check(matches(withText(R.string.open_maps)))
        IdlingRegistry.getInstance().unregister(loadStoresIdlingResource)

    }



}
