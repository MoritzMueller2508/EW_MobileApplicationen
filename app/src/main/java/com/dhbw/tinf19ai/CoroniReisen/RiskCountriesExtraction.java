package com.dhbw.tinf19ai.CoroniReisen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>") + "<p>Folgende Staaten/Regionen gelten aktuell als Risikogebiete:</p><ul>".length(),
                riskAreaHtml.indexOf("</ul><p><br />Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p>"));
        list = list.replaceAll("<ul>", "<ul></li>");
        list = list.replaceAll("<li>", "cut");

        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("cut", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        List<String> countriesList = getExtraRegions(list, convertedCountriesList);
        System.out.println(convertedCountriesList);

        return countriesList;
    }

    //return list of countries that were a risk area in the last 10 days but not anymore - should be used for the orange coroni
    public static List<String> getOrangeRiskCountries() throws IOException {
        String riskAreaHtml = getHtmlWebsite();
        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p><br />Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p><ul>") +
                        "<p><br />Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</p><ul>".length(),
                riskAreaHtml.indexOf("<div class=\"sectionRelated links\"><h2>Archiv der ausgewiesenen Risikogebiete seit 15.6.2020</h2>"));

        list = list.replaceAll("<ul>", "<ul></li>");
        list = list.replaceAll("<li>", "cut");

        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("cut", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        List<String> countriesList = getExtraRegions(list, convertedCountriesList);
        System.out.println(convertedCountriesList);

        return countriesList;
    }

    //return a list of countries without the HTML format
    private static List<String> getRiskCountries(List<String> convertedCountriesList){

        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            //removing empty list elements
            if (element.equals("")){
                convertedCountriesList.remove(i);
            }
            //getting rid of the specific areas/cities
            if (element.contains("<ul>")){
                while (!convertedCountriesList.get(i+1).contains("</ul>")){
                    convertedCountriesList.remove(i+1);
                }
                convertedCountriesList.remove(i+1);
            }
        }

        //getting rid of the extra information after the country
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            if (element.contains("-")){
                element = element.substring(element.indexOf("-"), element.indexOf("</li>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            if (element.contains("–")){
                element = element.substring(element.indexOf("–"), element.indexOf("</li>"));
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }

        //getting rid of the dates
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            if (element.contains("(")){
                element = element.substring(element.indexOf("("), element.indexOf("</li>")+5);
                String newElement = convertedCountriesList.get(i).replace(element, "");
                convertedCountriesList.set(i, newElement);
            }
        }

        //getting rid of extra html tags
        for (int i = 0; i < convertedCountriesList.size(); i++){
            String element = convertedCountriesList.get(i);

            if (element.contains("</li>")){
                String newElement = convertedCountriesList.get(i).replace(element, element.substring(0, element.indexOf("</li>")));
                convertedCountriesList.set(i, newElement);
            }
        }
        for (int i = 0; i < convertedCountriesList.size(); i++) {
            String element = convertedCountriesList.get(i);
            if (element.contains("<p>")) {
                String newElement = convertedCountriesList.get(i).replace(element, element.substring(3));
                convertedCountriesList.set(i, newElement);
            }
            if (element.contains(":")){
                String subElement  = element.substring(element.indexOf(":"));
                String newElement = convertedCountriesList.get(i).replace(subElement, "");
                convertedCountriesList.set(i, newElement);
            }
        }

        for (int i = 0; i < convertedCountriesList.size(); i++) {
            int length = convertedCountriesList.get(i).length();
            String element = convertedCountriesList.get(i);
            char lastChar = element.charAt(element.length()-1);
            if (lastChar == ' '){
                String newElement = element.substring(0, length-1);
                convertedCountriesList.set(i, newElement);
            }
        }

        return convertedCountriesList;
    }

    private static List<String> getExtraRegions(String originalList, List<String> convertedList){
        if(originalList.contains("Süd-Sudan")){
            convertedList.remove("Süd");
            convertedList.add("Südsudan");
        }
        if(convertedList.contains("Vereinigtes Königreich von Großbritannien und Nordirland")){
            convertedList.remove("Vereinigtes Königreich von Großbritannien und Nordirland");
            convertedList.add("Vereinigtes Königreich");
        }
        if(convertedList.contains("Trinidad Tobago")){
            convertedList.remove("Trinidad Tobago");
            convertedList.add("Trinidad und Tobago");
        }
        if(originalList.contains("Korea (Volksrepublik)")){
            convertedList.remove("Korea");
            convertedList.add("Nordkorea");
        }
        if(convertedList.contains("Kongo Rep")){
            convertedList.remove("Kongo Rep");
            convertedList.add("Republik Kongo");
        }
        if(convertedList.contains("Syrische Arabische Republik")){
            convertedList.remove("Syrische Arabische Republik");
            convertedList.add("Syrien");
        }
        if(convertedList.contains("Palästinensische Gebiete")){
            convertedList.add("Westjordanland");
            convertedList.add("Gazastreifen");
        }
        if(originalList.contains("Französisch-Guyana")){
            convertedList.add("Französisch-Guyana");
        }
        if(originalList.contains("Französisch-Polynesien")){
            convertedList.add("Französisch-Polynesien");
        }
        if(originalList.contains("St. Martin")){
            convertedList.add("St. Martin");
        }
        if(originalList.contains("Guadeloupe")){
            convertedList.add("Guadeloupe");
        }
        if(originalList.contains("Martinique")){
            convertedList.add("Martinique");
        }
        if(originalList.contains("Réunion")){
            convertedList.add("La Réunion");
        }
        return convertedList;
    }
}
