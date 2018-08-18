package net.numa08.mviweather.presentation.activity


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import net.numa08.mviweather.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HelloActivityTest {

    @get:Rule
    var mActivityTestRule: ActivityTestRule<HelloActivity> = ActivityTestRule(HelloActivity::class.java)

    @Test
    fun helloActivityTest() {
        val appCompatButton = onView(
                allOf(withId(R.id.button), withText("Click Me"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatButton.perform(click())

        val appCompatButton2 = onView(
                allOf(withId(R.id.button), withText("Click Me"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
                allOf(withId(R.id.button), withText("Click Me"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.constraint.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatButton3.perform(click())

        val textView = onView(
                allOf(withId(R.id.text), withText("Count 3"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        0),
                                0),
                        isDisplayed()))
        textView.check(matches(withText("Count 3")))

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
            }
        }
    }
}
