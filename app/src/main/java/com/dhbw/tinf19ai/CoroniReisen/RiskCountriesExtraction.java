package com.dhbw.tinf19ai.CoroniReisen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RiskCountriesExtraction {

    //get HTML Website in a string
    private static String getHtmlWebsite() throws IOException{
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
        return result;
    }

    //return list of countries that are a risk area - should be used for the red coroni
    public static List<String> getRedRiskCountries() throws IOException {
        String riskAreaHtml = getHtmlWebsite();
        System.out.println(riskAreaHtml);
        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>") + "<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>".length(),
                riskAreaHtml.indexOf("</ul><p>Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p>"));
        System.out.println(list);

        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("</li>", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        return convertedCountriesList;
    }

    //return list of countries that were a risk area in the last 10 days but not anymore - should be used for the orange coroni
    public static List<String> getOrangeRiskCountries() throws IOException {
        String riskAreaHtml = getHtmlWebsite();
        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p>Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p><ul>") +
                        "<p>Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p><ul>".length(),
                riskAreaHtml.indexOf("<div class=\"sectionRelated links\">"));


        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("</li>", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        return convertedCountriesList;
    }

    //return a list of countries without the HTML format
    private static List<String> getRiskCountries(List<String> convertedCountriesList){

        for(int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);
            convertedCountriesList.set(i, element + " <br>");
            element = convertedCountriesList.get(i);
            if(element.contains("Ausnahme")){
                element = element.substring(element.indexOf("Ausnahme"), element.indexOf(" <br>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
            if(element.contains("Ausgenommen")){
                element = element.substring(element.indexOf("Ausgenommen"), element.indexOf(" <br>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }

        ArrayList<String> regions = new ArrayList<String>();
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String region = convertedCountriesList.get(i);
            for(Map.Entry countryDictEntry : CountryDictionary.countriesDict.entrySet()){
                if(region.contains(countryDictEntry.getKey().toString())){
                    regions.add(countryDictEntry.getValue().toString());
                    continue;
                }
                if(region.contains(countryDictEntry.getValue().toString())){
                    regions.add(countryDictEntry.getValue().toString());
                    continue;
                }
            }
        }

        regions = getExtraRegions(regions);
        System.out.println(regions);
        return regions;
    }

    private static ArrayList<String> getExtraRegions(ArrayList<String> convertedList){

        if(convertedList.contains("Palästinensische Gebiete")){
            convertedList.add("Westjordanland");
            convertedList.add("Gazastreifen");
        }

        return convertedList;
    }
}
