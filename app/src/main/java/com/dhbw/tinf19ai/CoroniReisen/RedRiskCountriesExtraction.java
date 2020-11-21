package com.dhbw.tinf19ai.CoroniReisen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RedRiskCountriesExtraction  {

    //return list of countries that are a risk area - should be used for the red coroni
    public static List<String> redRiskCountriesExtraction() throws IOException {
        URL url = new URL("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html");
        URLConnection urlc = url.openConnection();
        urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputFile = urlc.getInputStream();
        Scanner scanner = new Scanner(inputFile);

        StringBuffer html = new StringBuffer();

        while (scanner.hasNextLine()) {
            html.append(scanner.nextLine());
        }
        String result = html.toString();

        String list = result.substring(result.indexOf("<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>") + "<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>".length(), result.indexOf("</ul><p><br />Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p>"));
        list = list.replaceAll("</ul>", "</ul>cut");
        list = list.replaceAll("</li>", "</li>thiscut");

        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("cut", -1)));

        //getting rid of the extra information after the country
        for (int i = 0; i < convertedCountriesList.size(); i++) {
            String element = convertedCountriesList.get(i);

            if (element.contains("</ul>")) {
                convertedCountriesList.remove(i);
            }
            if (element.contains(" - ")) {
                element = element.substring(element.indexOf(" - "), element.indexOf("</li>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
            if (element.contains("– ")) {
                element = element.substring(element.indexOf("– "), element.indexOf("</li>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }
        //getting rid of the dates
        for (int i = 0; i < convertedCountriesList.size(); i++) {
            String element = convertedCountriesList.get(i);
            if (element.contains("(")) {
                element = element.substring(element.indexOf("("), element.indexOf("this") + 4);
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
            if (element.contains("this")) {
                element = element.substring(element.indexOf("</li>"), element.indexOf("this") + 4);
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }
        //getting rid of the specific areas
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            if (element.contains("<p>")){
                while (!convertedCountriesList.get(i+1).equals("")){
                    convertedCountriesList.remove(i+1);
                }
            }
            if (element.equals("")){
                convertedCountriesList.remove(i);
            }
        }
        //getting rid of extra html tags
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);
            if (element.contains("<li><p>")){
                String newElement = convertedCountriesList.get(i).replace(element, element.substring(7));
                convertedCountriesList.set(i, newElement);
            }
            if (element.contains("<li>")){
                String newElement = convertedCountriesList.get(i).replace(element, element.substring(4));
                convertedCountriesList.set(i, newElement);
            }
        }

        return convertedCountriesList;
    }

}
