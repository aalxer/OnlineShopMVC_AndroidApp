package com.example.onlineshopmvc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.onlineshopmvc.appActivites.saleActivities.CheckOutActivity;
import com.example.onlineshopmvc.appActivites.saleActivities.SaleMainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSaleAppGUI {

    @Rule
    public ActivityScenarioRule<SaleMainActivity> activityRule = new ActivityScenarioRule<>(SaleMainActivity.class);

    @Test
    public void testOrderSomething() {
        // Artikel dem Einkaufswagen hinzufügen:
        onView(withId(R.id.saleProductsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, new ViewAction() {
            // Button in einem RecyleView Item finden und clicken:
                    @Override
                    public Matcher<View> getConstraints() {
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return "Click on Checkout Button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View button = view.findViewById(R.id.addToBasketBtn);
                        button.performClick();
                    }
                }));
        onView(withId(R.id.shoppingCartItem)).perform(click());
        onView(withId(R.id.checkoutBtnShoppingCart)).perform(click());

        // Bestellungsinfo eingeben:
        onView(withId(R.id.firstnameInput)).perform(typeText("Ahmed"));
        onView(withId(R.id.surnameInput)).perform(typeText("Alwaili"));
        onView(withId(R.id.emailInput)).perform(typeText("mail@mail.com"));
        onView(withId(R.id.firstLineAddressInput)).perform(typeText("street, 1"), closeSoftKeyboard());
        onView(withId(R.id.postCodeInput)).perform(typeText("80058"), closeSoftKeyboard());
        onView(withId(R.id.cityInput)).perform(typeText("Berlin"), closeSoftKeyboard());

        // Zur Zahlung:
        onView(withId(R.id.paymentBtn)).perform(click());
        onView(withId(R.id.gpayBtn)).perform(click());
    }

    @Test
    public void testCancelAnOrder() {

        // Menu öffnen:
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Cancel Order")).perform(click());
        onView(withId(R.id.orderIdFeld)).perform(typeText("663000"), closeSoftKeyboard());
        onView(withId(R.id.customerNameFeld)).perform(typeText("Ahmed Alwaili"), closeSoftKeyboard());
        onView(withId(R.id.emailFeld)).perform(typeText("mail@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.cancelBtn)).perform(click());
    }

    @Test
    public void testCheckOrderState() {
        // Menu öffnen:
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Check Status")).perform(click());
        onView(withId(R.id.orderIdFeld_checkStatus)).perform(typeText("663000"), closeSoftKeyboard());
        onView(withId(R.id.checkStatusBtn)).perform(click());
    }
}
