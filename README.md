# Country Selector

Country selector allows you to get quick information of a country.


| <a href="https://ibb.co/ZMRnhXj"><img src="https://i.ibb.co/QK726Qg/Country-Selector-Screenshots.png" alt="Country-Selector-Screenshots" border="0"></a>  






# Project Overview

  The purpose of this project is to demonstrate the current quality level of the code I write and the UI/UX that I  design.
  Which will help the companies who are considering hiring me to have a cleaner perspective on my skill level.
<br>
You can find brief technical information about the project here.

## Model-View-ViewModel

This project uses the [recommended](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) design pattern by Google, MVVM. 
Which helps to have a nice, maintainable structure that is easy to unit test since the modules are independent.

Also, it helps a lot to have a lifecycle aware inheritable ViewModel class that is working perfectly with also lifecycle aware LiveData class. 

## Dependency Injection
This project also uses the dependency injection since it drastically helps with unit testing, maintaining loosely coupled code and having reusable modules.

As a framework, Dagger 2 had been preferred since it has no effect on performance, reduces boilerplate code (even though not in this particular case (: ) and also recommended and supported by Google. 

## Testing

A significant amount of the cases in this project whether it is a module or a UI is covered by unit, integration and UI tests.
Following libraries had been preferred due to their quality and popularity:

 - Junit 4
 - Mockito
 - MockWebServer
 - Hamcrest
 - Espresso
 - UIAutomator

## Custom Themes & Styles

In designing of the UI, custom themes had been created and view attributes like color, dimensions, strings are **not** hardcoded which is allowing additional themes to be created easily.

Views has their own styles which reduces the copying and pasting the attributes and provides consistency when creating similar views.

## Designing

 - While designing the layouts, most of the time ConstraintLayout had
   been preferred for the best performance.
  
 - Avoided from stacking the layouts as much as possible    to achieve
   the best optimization.
   
 -  Layouts are compatible with both portrait and landscape mode.
 
 - Avoided displaying too much options or information in one view for the best user experience.
 
 - Used ViewStub for ErrorCard to optimize rendering of the layout since it has stacked layouts and it does not need to be inflated immediatly 



Globe Icon is edited, given gradient and drop shadow by me.
Android error icon is designed by me by getting inspired of an another design.

|<a href="https://imgbb.com/"><img src="https://i.ibb.co/7ncmqcN/globe.png" alt="Icon" height="250" width="250" border="0"></a>  |<a href="https://imgbb.com/"><img src="https://i.ibb.co/QNyqdKN/android-crash-ic.png" alt="android-crash-ic" height="250" width="250" border="0"></a>  |
|--|--|



## Documentation Of The Codes

The project is well-documented by using javadoc and it is ready to be exported as HTML. 


# Design Insight


## Integration Of Countries To The Project
When I was planning to integrate a list of countries and their flags to the application, I have decided I should demonstrate how I would approach a changeable big data (since it wasn't actually a big data, I have preferred to have a fast scroller with an alphabetical letter indicator in favor of the pagination) .

Therefore,  even though it would be easier and more applicable to copy-paste a static list of country names and codes from an open-source Java project,  I have chosen to add a JSON file containing the list of the countries and querying them once on each launch. Since I stored them in a List within a singleton class, JSONCountriesRepository, countries are already being in the memory. Hence, it does not require to be queried each time it is needed.
And I have added the images of the every flag by using a batch drawable importer plugin.

I thought that this list could be used in multiple places. Therefore I have thought that if it would be a DialogFragment it would not only be more easy to access by the users but also would be much more easy to implement on the other screens. So I proceeded with that idea and used observer pattern in the DialogFragment to have flexibility on implementation since every screen might want to take a different action when a country is clicked.


## Retrieving The Country Details

I have used Retrofit to send GET requests since it is reliable, easy and compatible with GSON out of the box.

I have handled the exceptional cases like slow network connection, no internet connection, not found responses, etc. and added a modern informing UI to convey the error.

I have also written singleton ScheduledExecutorService and Retrofit service classes by considering the scalability, reusability, and performance.

## Dagger 2

I have not created any subcomponent like MainActivity component because there is not a dependency in the application that should not be accessed by the another lifecycle (no login or auth system) and there is also not a chain of dependencies that are only being used to satistfy one class.


## Further Development

 - **Custom FastScrolller With SectionIndexer**
The library that I used for FastScrolling with SectionIndexer for RecyclerView does not work very well.
Writing a custom one and using an AbsListView would have been better.


 - **DayNight Theme**
I really like the new DayNight feature/theme, it could be impemented to the application but I have not implemented it yet because time was running out and I want to keep the dark theme as default unless user choses to do otherwise which means not only I would have to create a light theme but also create a SharedPreferences repository, instrumentation test and a settings screen. 
And this is not a production application, so I have not find it efficent to do so.
