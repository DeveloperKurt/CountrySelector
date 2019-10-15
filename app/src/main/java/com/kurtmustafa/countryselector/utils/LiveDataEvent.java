package com.kurtmustafa.countryselector.utils;

import androidx.annotation.Nullable;

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * <p/>
 * When observing a LiveData if the screen get rotated, observers will get notified for a 2nd time even if it was a one-time event, for example:<br>
 * An error gets sent to the CountryDetails observer, it gets handled, user views another country successfully, and rotates his/her screen.<br>
 * Whoops! and now there is an error card getting displayed on user's screen!
 * <p/>
 * By using this class one time {@link androidx.lifecycle.LiveData} events can be handled and also exceptional cases can be covered by
 * {@link #peekContent()}  method.
 *
 * @see <a href="https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150">For more details see the Google's article</a>
 */
public class LiveDataEvent<T>
    {

        private T content;

        private boolean hasBeenHandled = false;


        public LiveDataEvent(T content)
            {
                if (content == null)
                    {
                        throw new IllegalArgumentException("Content cannot be null");
                    }
                this.content = content;
            }

        /**
         * Returns the content and prevents its usage again.
         */
        @Nullable
        public T getContentIfNotHandled()
            {
                if (hasBeenHandled)
                    {
                        return null;
                    }
                else
                    {
                        hasBeenHandled = true;
                        return content;
                    }
            }

        /**
         * Returns the content, even if it's already been handled.
         */
        public T peekContent()
            {
                return content;
            }

        public boolean hasBeenHandled()
            {
                return hasBeenHandled;
            }
    }