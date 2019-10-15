package com.kurtmustafa.countryselector.di.qualifiers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/*
This @Qualifier is used to distinguish between ActivityContext and ApplicationContext
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext
    {
    }
