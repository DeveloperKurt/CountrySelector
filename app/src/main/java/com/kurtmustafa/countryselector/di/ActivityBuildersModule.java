package com.kurtmustafa.countryselector.di;

import com.kurtmustafa.countryselector.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule
    {

        @ContributesAndroidInjector
        abstract MainActivity contributeMainActivity();
    }
