package com.kurtmustafa.countryselector.ui_tests;

import android.os.RemoteException;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.ui.MainActivity;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.FragmentCountryDetails;
import com.kurtmustafa.countryselector.ui_tests.idlingResources.CountryDetailsIdlingResource;
import com.kurtmustafa.countryselector.ui_tests.idlingResources.DialogCountryListLoadingIdlingResource;
import com.kurtmustafa.countryselector.utils.testutils.NetworkUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.kurtmustafa.countryselector.utils.testutils.NetworkUtils.disableInternetViaADBMessages;
import static org.hamcrest.core.StringContains.containsString;


/**
 * This class contains tests that validates all of the view components of the {@link MainActivity} including its fragments and dialogs
 * works as they are intended.
 * <p/>
 * NOTE: The tests performed in this class are depended on the countries.json, countries rest server and every other component that these views use.
 */
@LargeTest
public class MainActivityViewsIntegrationTest
    {
        @Rule
        public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);



        @Before
        public void setUp()
            {
                IdlingPolicies.setMasterPolicyTimeout(30, TimeUnit.SECONDS);
            }



        /**
         * Automated UI test that navigates throughout all of the views of the MainActivity and validates their behaviour.
         * <p/>
         * It does not separate it to small tests because it would cause recreation of the Activity each time which is expensive
         * and time consuming since it loads resources each time it gets initialized. Besides, they are already depended on each other to succeed.
         * Therefore excessive logging&commenting seems like a better way to detect which parts succeed or not.
         */
        @Test
        public void fragmentPickCountryTest() throws RemoteException
            {

                //-----FRAGMENT PICK COUNTRY TEST------

                Timber.i("---------------FRAGMENT PICK COUNTRY TEST BEGINS---------------");

                //Verify Fragment Pick Country is launched by default
                Timber.i("Verifying whether Fragment Pick Country is launched by default");
                isFragPickCountryActive();

                checkDialogCountryListIsNotVisible();

                //Check if dialog is visible after fab click
                Timber.i("Verifying the country list dialog is visible after clicking to fab");
                onView(withId(R.id.pick_country_display_countries_fab)).perform(click());
                checkDialogCountryListIsVisible();

                //Check if dialog disappears after clicking on the back button without finishing the fragment
                Timber.i("Verifying the dialog disappears after clicking on the back button without finishing the fragment");
                Espresso.pressBack();
                isFragPickCountryActive();
                checkDialogCountryListIsNotVisible();

                //Check if the dialog disappears after rotating the screen(prevents null country click listener on recycler adapter)
                Timber.i("Verifying the dialog disappears after rotating the screen");
                onView(withId(R.id.pick_country_display_countries_fab)).perform(click());
                UiDevice.getInstance(getInstrumentation()).setOrientationLeft();
                checkDialogCountryListIsNotVisible();

                //Check if navigates to Fragment Country Details after clicking to a country from Dialog Fragment Country List
                Timber.i("Verifying navigation to Fragment Country Details after clicking to a country");
                onView(withId(R.id.pick_country_display_countries_fab)).perform(click());
                DialogCountryListLoadingIdlingResource listLoadingIdlingResource = new DialogCountryListLoadingIdlingResource(activityRule.getActivity());
                IdlingRegistry.getInstance().register(listLoadingIdlingResource);
                onView(withId(R.id.country_list_recycler_view)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Sweden")), click()));
                IdlingRegistry.getInstance().unregister(listLoadingIdlingResource);
                isFragCountryDetailsActive();

                Timber.i("---------------FRAGMENT PICK COUNTRY TEST ENDS---------------");




                //-----FRAGMENT COUNTRY DETAILS TEST------

                Timber.i("---------------FRAGMENT PICK COUNTRY DETAILS TEST BEGINS---------------");
                checkDialogCountryListIsNotVisible();

                //Check if the card header has the right content
                Timber.i("Verifying the card header has the right content");
                onView(withId(R.id.country_details_card_tv_country)).check(matches(withText("Sweden")));

                //Check if the details has the right content
                Timber.i("Verifying the details has the right content");
                CountryDetailsIdlingResource countryDetailsIdlingResource = new CountryDetailsIdlingResource(activityRule.getActivity());
                IdlingRegistry.getInstance().register(countryDetailsIdlingResource);
                onView(withId(R.id.country_details_tv_details)).check(matches((withText(containsString("Stockholm")))));

                //Check if the dialog is visible after fab click
                Timber.i("Verifying the dialog is visible after fab click");
                onView(withId(R.id.country_details_display_countries_fab)).perform(click());
                checkDialogCountryListIsVisible();

                //View another country's details
                Timber.i("Viewing another country's details");
                onView(withId(R.id.country_list_recycler_view)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Norway")), click()));

                //Check if the card header has the right content
                Timber.i("Verifying the card header has the right content");
                onView(withId(R.id.country_details_card_tv_country)).check(matches(withText("Norway")));

                //Check if the details has the right content
                Timber.i("Verifying the details has the right content");
                onView(withId(R.id.country_details_tv_details)).check(matches((withText(containsString("Oslo")))));

                //Rotate the screen and check whether it still has the right details
                Timber.i("Rotating the screen to verify the contents of the header and card");
                UiDevice.getInstance(getInstrumentation()).setOrientationNatural();
                onView(withId(R.id.country_details_card_tv_country)).check(matches(withText("Norway")));
                onView(withId(R.id.country_details_tv_details)).check(matches((withText(containsString("Oslo")))));

                //tear down
                IdlingRegistry.getInstance().unregister(countryDetailsIdlingResource);

                Timber.i("---------------FRAGMENT PICK COUNTRY DETAILS TEST ENDS---------------");
            }

        /**
         * Verifies {@link FragmentCountryDetails}'s error card behaviour
         *
         * NOTE: This test requires either a rooted device or disabled wifi and mobile data.
         * @throws InterruptedException
         * @throws IOException
         * @throws RemoteException
         */
        @Test
        public void testCountryDetailsErrorCard() throws InterruptedException, IOException, RemoteException
            {
                if(NetworkUtils.haveNetworkConnection(getInstrumentation().getTargetContext()))
                    {
                        if (!disableInternetViaADBMessages())
                            Assert.fail("You need to use either a rooted device or disable your device's wifi and mobile data to run this test");
                    }

                //Click on display country list fab
                onView(withId(R.id.pick_country_display_countries_fab)).perform(click());
                DialogCountryListLoadingIdlingResource listLoadingIdlingResource = new DialogCountryListLoadingIdlingResource(activityRule.getActivity());
                IdlingRegistry.getInstance().register(listLoadingIdlingResource);

                //Click on Sweden
                onView(withId(R.id.country_list_recycler_view)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Sweden")), click()));
                IdlingRegistry.getInstance().unregister(listLoadingIdlingResource);

                //Check if process bar is visible
                onView(withId(R.id.country_details_progress_bar)).check(matches(isDisplayed()));

                //Wait for repository to retry the operation.
                //NO BETTER WAY TO DO  (without writing exceptional test case checks in the repository ) since the repository thread sleeps as well.
                Thread.sleep(CountryDetailsRepository.WAIT_TIME_BEFORE_RETRY * CountryDetailsRepository.RETRY_COUNT);

                //Verify the error card is visible
                onView(withId(R.id.error_card)).check(matches(isDisplayed()));

                //Rotate the screen and verify it is still visible
                UiDevice.getInstance(getInstrumentation()).setOrientationLeft();
                onView(withId(R.id.error_card)).check(matches(isDisplayed()));

            }



         //Created the following methods because not only they are used repeatedly but also their content is not as much self-describing

        private void checkDialogCountryListIsNotVisible()
            {
                Timber.i("Verifying dialog country list is not visible");
                onView(withText(R.string.pick_a_country)).check(doesNotExist());
            }

        private void checkDialogCountryListIsVisible()
            {
                Timber.i("Verifying dialog country list is visible");
                onView(withText(R.string.pick_a_country)).check(matches(isDisplayed()));
            }

        private void isFragCountryDetailsActive()
            {
                Timber.i("Verifying COUNTRY_DETAILS_FRAGMENT is active");
                Assert.assertEquals(MainActivity.ActiveFragment.COUNTRY_DETAILS_FRAGMENT, activityRule.getActivity().getActiveFragment());
            }

        private void isFragPickCountryActive()
            {
                Timber.i("Verifying PICK_COUNTRY_FRAGMENT is active");
                Assert.assertEquals(MainActivity.ActiveFragment.PICK_COUNTRY_FRAGMENT, activityRule.getActivity().getActiveFragment());
            }
    }
