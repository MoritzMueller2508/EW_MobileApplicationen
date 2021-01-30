# description of the classes

## BingData

>This class connects to the Bing Covid API. The data is imported from the api periodically and written to a csv to get the current numbers.
 We get the following data:
 ID (country)
 Updated (date)
 Confirmed (confirmed cases)
 ConfirmedChange (rate of change of cases from previous day)
 Deaths (confirmed deaths)
 DeathsChange (Rate of change of deaths from the previous day)
 Recovered (Recovered cases)
 RecoveredChange (Rate of change recovered from the previous day)


## CoroniAssignment

>This class assigns the appropriate coroni to the countries which are assigned to the different groups in the class **RiskCountriesExtraction**.


## CountryDetails

>This class displays the data for a matching country.
 The class contains 5 cards with
 a map and the matching coroni,
 our travel advices,
 a reference to the entry requirements of the foreign office,
 the current numbers in the form of a pie chart and
 a reference to our data source.


## CountryDictionary

>This class represents a hashtable with all countries of this world (we don't deny the existance of other worlds).


## DestinationsList

>This class creates the lists of countries for the different buttons.
 It displays the selected category with a suitable text and the list of selectable countries.


## MainActivity

>This class represents the first interface that includes a greeting and rules of conduct during a pandemic.
 By clicking on the image you will be redirected to the next interface.


## MapFragment

>This class represents the selection of the country to be considered with a map.
 When clicking on a marker of an entered country, the user is redirected to the last UI for the country details.
 There are also vacation categories which are represented by buttons.
 By clicking on them, the user will be redirected to the appropriate list of countries.


## Navigator

>This class navigates between the different interfaces (phone-view / tablet-view)


## RiskCountriesExtraction

>This class extracts the risk countries, regions, islands, etc. from the RKI website and converts them into a list.
 The distinction is made between current risk countries *getRedRiskCountries()* and countries that were a risk country in
 the last 10 days but are not anymore *getOrangeRiskCountries()*.

