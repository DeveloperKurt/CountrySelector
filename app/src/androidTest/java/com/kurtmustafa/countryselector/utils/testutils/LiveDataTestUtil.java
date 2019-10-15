package com.kurtmustafa.countryselector.utils.testutils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;



/**
 * Can be used to make testing with LiveData more easier since it provides following functionalities:
 * <p>
 * This function observes a LiveData until it receives a new value (via onChanged) and then it removes the observer.
 * If the LiveData already has a value, it returns it immediately.
 * Additionally, if the value is never set, it will throw an exception after 2 seconds (or whatever you set).
 * This prevents tests that never finish when something goes wrong.
 *
 * @see <a href="https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04"> Google Article On This</a>
 */
public class LiveDataTestUtil
    {


        /**
         * @param liveData livedata to observe
         * @param waitTime wait time in seconds for data to be set.
         * @throws InterruptedException
         */
        public static <T> T getOrAwaitValue(final LiveData<T> liveData, int waitTime) throws InterruptedException
            {
                final Object[] data = new Object[1];
                final CountDownLatch latch = new CountDownLatch(1);
                Observer<T> observer = new Observer<T>()
                    {
                        @Override
                        public void onChanged(@Nullable T o)
                            {

                                data[0] = o;
                                latch.countDown();
                                liveData.removeObserver(this);
                            }
                    };
                liveData.observeForever(observer);
                // Don't wait indefinitely if the LiveData is not set.
                if (!latch.await(waitTime, TimeUnit.SECONDS))
                    {
                        throw new RuntimeException("LiveData value was never set.");
                    }
                //noinspection unchecked
                return (T) data[0];
            }


        //Copyright 2019 Google LLC.SPDX-License-Identifier: Apache-2.0
        /**
         * @throws InterruptedException
         */
        public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException
            {
                final Object[] data = new Object[1];
                final CountDownLatch latch = new CountDownLatch(1);
                Observer<T> observer = new Observer<T>()
                    {
                        @Override
                        public void onChanged(@Nullable T o)
                            {
                                data[0] = o;
                                latch.countDown();
                                liveData.removeObserver(this);
                            }
                    };
                liveData.observeForever(observer);
                // Don't wait indefinitely if the LiveData is not set.
                if (!latch.await(2, TimeUnit.SECONDS))
                    {
                        throw new RuntimeException("LiveData value was never set.");
                    }
                //noinspection unchecked
                return (T) data[0];
            }


    }