package com.kurtmustafa.countryselector;


import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.models.ErrorCodes;
import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.requests.RestCountriesServiceGenerator;
import com.kurtmustafa.countryselector.testutils.LiveDataTestUtil;
import com.kurtmustafa.countryselector.testutils.ModelInstances;
import com.kurtmustafa.countryselector.utils.LiveDataEvent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.LargeTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


@LargeTest //Test time exceeds 1 second due to retry attempts (sleeps the thread)
@RunWith(MockitoJUnitRunner.class)
public class CountryDetailsRepositoryTest
    {
        @Rule //This rule is needed to be able to use the LiveData
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

        private CountryDetailsRepository countryDetailsRepository;
        private MockWebServer server;


        @Before
        public void setUp()
            {
                server = new MockWebServer();
            }

        @After
        public void clean() throws IOException
            {
                server.shutdown();
            }

        @Test
        public void getsDetailsCorrectlyOnResponseSuccess() throws IOException, InterruptedException
            {



                CountryDetails response200CountryDetails = ModelInstances.SwedenCountryDetails;

                //Given
                server.enqueue(new MockResponse().setResponseCode(200).setBody(RestCountryResponses.RESPONSE_200)
                        .addHeader(RestCountryResponses.HEADER_CONTENT_TYPE, RestCountryResponses.HEADER_CONTENT_TYPE_VALUE));

                initRepositoryWithMockServer();

                //When
                countryDetailsRepository.loadNewCountryDetails("se");
                CountryDetails countryDetails = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getCountryDetails());

                //Then
                Assert.assertEquals(response200CountryDetails.toString(), countryDetails.toString());
            }


        @Test
        public void PostsRightErrorWhenNoInternet() throws InterruptedException
            {
                //Given no active server ( equivalent of no internet)
                CountryDetails countryDetails = null;
                RestCountriesServiceGenerator restCountriesServiceGenerator = new RestCountriesServiceGenerator("http://noserver/");
                CountryDetailsRepository countryDetailsRepository = new CountryDetailsRepository(restCountriesServiceGenerator);


                //When
                countryDetailsRepository.loadNewCountryDetails("se");

                //Wait time is higher since it retries
                LiveDataEvent<ErrorCodes> error = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getErrors(), CountryDetailsRepository.REQUEST_TIMEOUT_TIME);

                countryDetails = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getCountryDetails());


                //Then
                Assert.assertEquals(ErrorCodes.NO_INTERNET, error.peekContent());
                Assert.assertNull("Country details liveData was not nullified, might cause inconsistencies!", countryDetails);

            }

        @Test
        public void PostsRightErrorWhen400() throws InterruptedException, IOException
            {

                //Given
                CountryDetails countryDetails = null;
                server.enqueue(new MockResponse().setResponseCode(400)
                        .setBody(RestCountryResponses.RESPONSE_400).addHeader(RestCountryResponses.HEADER_CONTENT_TYPE, RestCountryResponses.HEADER_CONTENT_TYPE_VALUE));
                initRepositoryWithMockServer();


                //When
                countryDetailsRepository.loadNewCountryDetails("se");

                LiveDataEvent<ErrorCodes> error = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getErrors());

                countryDetails = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getCountryDetails());

                //Then
                Assert.assertNull("Country details liveData was not nullified, might cause inconsistencies!", countryDetails);
                Assert.assertEquals(ErrorCodes.BAD_REQUEST, error.peekContent());

            }


        @Test
        public void PostsRightErrorWhen404() throws InterruptedException, IOException
            {
                CountryDetails countryDetails = null;
                //Given
                server.enqueue(new MockResponse().setResponseCode(404).setBody(RestCountryResponses.RESPONSE_404).addHeader(RestCountryResponses.HEADER_CONTENT_TYPE, RestCountryResponses.HEADER_CONTENT_TYPE_VALUE));
                initRepositoryWithMockServer();


                //When
                countryDetailsRepository.loadNewCountryDetails("se");

                LiveDataEvent<ErrorCodes> error = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getErrors());
                countryDetails = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getCountryDetails());

                //Then
                Assert.assertNull("Country details liveData was not nullified, might cause inconsistencies!", countryDetails);
                Assert.assertEquals(ErrorCodes.NOT_FOUND, error.peekContent());

            }


        /**
         * Since responses must be enqueued before the server starts, repository and its dependencies cannot be initialized at @Before method
         * Therefore this class can be called after required responses have been passed to eliminate the boiler plate code
         */
        private void initRepositoryWithMockServer() throws IOException
            {
                server.start();

                HttpUrl URL = server.url("/rest/v2/");

                RestCountriesServiceGenerator restCountriesServiceGenerator = new RestCountriesServiceGenerator(URL.toString());
                countryDetailsRepository = new CountryDetailsRepository(restCountriesServiceGenerator);
            }


        private static class RestCountryResponses
            {

                private static final String RESPONSE_200 = "{\"name\":\"Sweden\",\"topLevelDomain\":[\".se\"],\"alpha2Code\":\"SE\",\"alpha3Code\":\"SWE\",\"callingCodes\":[\"46\"],\"capital\":\"Stockholm\",\"altSpellings\":[\"SE\",\"Kingdom of Sweden\",\"Konungariket Sverige\"],\"region\":\"Europe\",\"subregion\":\"Northern Europe\",\"population\":9894888,\"latlng\":[62.0,15.0],\"demonym\":\"Swedish\",\"area\":450295.0,\"gini\":25.0,\"timezones\":[\"UTC+01:00\"],\"borders\":[\"FIN\",\"NOR\"],\"nativeName\":\"Sverige\",\"numericCode\":\"752\",\"currencies\":[{\"code\":\"SEK\",\"name\":\"Swedish krona\",\"symbol\":\"kr\"}],\"languages\":[{\"iso639_1\":\"sv\",\"iso639_2\":\"swe\",\"name\":\"Swedish\",\"nativeName\":\"svenska\"}],\"translations\":{\"de\":\"Schweden\",\"es\":\"Suecia\",\"fr\":\"Suède\",\"ja\":\"スウェーデン\",\"it\":\"Svezia\",\"br\":\"Suécia\",\"pt\":\"Suécia\",\"nl\":\"Zweden\",\"hr\":\"Švedska\",\"fa\":\"سوئد\"},\"flag\":\"https://restcountries.eu/data/swe.svg\",\"regionalBlocs\":[{\"acronym\":\"EU\",\"name\":\"European Union\",\"otherAcronyms\":[],\"otherNames\":[]}],\"cioc\":\"SWE\"}";
                private static final String RESPONSE_400 = "{\"status\":400,\"message\":\"Bad Request\"}";
                private static final String RESPONSE_404 = "{\"status\":404,\"message\":\"Not Found\"}";

                private static final String HEADER_CONTENT_TYPE_VALUE = "application/json; charset=utf-8";
                private static final String HEADER_CONTENT_TYPE = "Content-Type";
            }


    }
