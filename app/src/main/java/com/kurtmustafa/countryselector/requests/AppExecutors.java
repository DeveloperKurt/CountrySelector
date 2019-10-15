package com.kurtmustafa.countryselector.requests;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Fires the background threads when needed.
 * <p/>
 * Using ScheduledExecutorService over classic Executor since it allows extra functionalities.
 * Then it creates a pool of thread, CORE_POOL_SIZE threads to do all the work required in the app.
 * schedule() method of ScheduledExecutorService can be used to set a network timeout and can cancel the request.
 *
 */
public class AppExecutors
    {
        private static AppExecutors instance;
        private final static short CORE_POOL_SIZE = 3;
        public static AppExecutors getInstance(){
            if(instance == null){
                instance = new AppExecutors();
            }
            return instance;
        }
        private final ScheduledExecutorService networkIO =
                Executors.newScheduledThreadPool(CORE_POOL_SIZE);



        public ScheduledExecutorService networkIO() {
            return networkIO;
        }
    }
