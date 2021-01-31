# Sources

1. [Coronis - Risk areas](sources.md#coronis---risk-areas)
2. [Covid-19 Bing Data](sources.md#covid-19-bing-data)

## Coronis - Risk areas
<img src="../app/src/main/res/drawable/coroni_red.png" width=300px height=300px> <img src="../app/src/main/res/drawable/coroni_orange.png" width=300px height=300px> <img src="../app/src/main/res/drawable/coroni_green.png" width=300px height=300px>
<br>

To assign the coronis to a country, we look up the current risk areas in the [RKI website](https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html). This website will be saved on a csv file in the user's phone for the case, in which the user goes later on the App while offline. This file will be updated every 24h.

If the user is online, then the App will get the information directly from the website and parse it into two lists: redRiskCountriesList and orangedRiskCountriesList (see the [classes documentation](classes.md#RiskCountriesExtraction)). Once the user entered a valid country or clicked on one in one of the lists, then the App will search through the risk aread lists to see if the country is there. If not, then the country is currently not a risk area (according to the RKI's standards). The Coroni will be displayed accordingly.

- Red Coroni = currently a risk area.
- Orange Coroni = is not currently a risk area but was one in the last 10 days.
- Green Coroni = is not currently a risk area.

<br><br>

## Covid-19 Bing Data

Our pie chart uses the data from the [Bing Covid-19 data repository](https://github.com/microsoft/Bing-COVID-19-Data). This repository is updated every 24h but it's not from us, so it might happen that the most current data is not yet available on our App, due to the Bing Covid-19 data repository not beeing updated. You can always find the latest data on the [Bing Covid-19 website](https://www.bing.com/covid).

Our App extracts the data from above mentioned repository and saves it in a csv file in the phone for later use and in case the user goes offline (so that the last downloaded data is always available to the App). This file is updated every 24h. See the [classes documentation](classes.md#BingData).

From this data we get the last confirmed, recovered and death cases from a certain country; as long as the data is also available on the [Bing Covid-19 data repository](https://github.com/microsoft/Bing-COVID-19-Data).


<br><br>

## MPAndroid - Library

The Bing-Covid-19-Data is displayed in a piechart, provided by the MPAndroid Library [Github](https://github.com/PhilJay/MPAndroidChart).
The piechart got the following features:
- Highlight by tap
    - The desired slice can be tapped, which will then display the data as a float-number. Furthermore, the slice is now highlighted.
- Rotation
    - The user can rotate the chart. To do so, just tap the chart so one slice is highlighted and drag in the desired direction.
- Animation
    - When the piechart is drawn with an animation
- The pichart shows recovered, current and death cases.

At the top, the last time the data was updated is displayed.
