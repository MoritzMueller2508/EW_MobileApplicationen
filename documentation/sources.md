# Sources

## Coronis
<img src="../app/src/main/res/drawable/coroni_green.png" width=200px height=200px> <img src="../app\src\main\res\drawable\coroni_orange.png" width=200px height=200px> <img src="../app\src\main\res\drawable\coroni_red.png" width=200px height=200px>
<br>

To assign the coronis to a country, we look up the current risk areas in the [RKI website](https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html). This website will be saved on a csv file in the user's phone for the case, in which the user goes later on the app while offline. This file will be updated every 24h.

If the user is online, then the app will get the information directly from the website and parse it into two lists; redRiskCountriesList and orangedRiskCountriesList (see the [classes documentation](classes.md)). Once the user entered a valid country or clicked on one in one of the lists, then the App will search through the lists to see if the country is there. If not, then the country is currently not a risk area (according to the RKI's standards). The Coroni will be displayed accordingly.

- Red Coroni = currently a risk area.
- Orange Coroni = is not currently a risk area but was one in the last 10 days.
- Green Coroni = is not currently a risk area.

<br><br>

## Covid-19 Bing Data
