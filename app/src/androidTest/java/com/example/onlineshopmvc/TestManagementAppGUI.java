package com.example.onlineshopmvc;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

import com.example.onlineshopmvc.appActivites.managementActivities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestManagementAppGUI {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void loginTest() {
        onView(withId(R.id.passEnterFeld)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
    }

    @Test
    public void TestLoginChangePass() {
        onView(withId(R.id.passEnterFeld)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.changePass)).perform(click());
        onView(withId(R.id.oldPassFeld)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.newPassFeld)).perform(typeText("ahmed"),closeSoftKeyboard());
        onView(withId(R.id.passChangeBtn)).perform(click());
    }

    @Test
    public void TestAddNewProduct() {
        onView(withId(R.id.passEnterFeld)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.productsBtn)).perform(click());
        onView(withId(R.id.addBtn)).perform(click());

        onView(withId(R.id.stockNrInput)).perform(typeText("12"));
        onView(withId(R.id.titleInput)).perform(typeText("Tisch"));
        onView(withId(R.id.descriptionInput)).perform(typeText("no description"));
        onView(withId(R.id.codeInput)).perform(typeText("100358"));
        onView(withId(R.id.priesInput)).perform(typeText("263.5") ,closeSoftKeyboard());
        onView(withId(R.id.productAddBtn)).perform(click());
    }

    @Test
    public void TestUpdateProductsDetails() {
        onView(withId(R.id.passEnterFeld)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.productsBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.productsDisplay)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.updateStockNrInput)).perform(typeText("43"),closeSoftKeyboard());
        onView(withId(R.id.productUpdateBtn)).perform(click());
    }

    @Test
    public void TestUpdateOrderState() {
        onView(withId(R.id.passEnterFeld)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.ordersBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.ordersDisplay)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.orderStateMenu)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("DELIVERED"))).perform(click());
        onView(withId(R.id.orderStateMenu)).check(matches(withSpinnerText(containsString("DELIVERED"))));
    }
}