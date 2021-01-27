package com.dhbw.tinf19ai.CoroniReisen;

/**
 *This class extracts the risk countries, regions, islands, etc. from the RKI website and converting
 * them into a list.
 * The distinction is made between current risk countries --getRedRiskCountries()-- and countries that were a risk country in
 * the last 10 days but are not anymore --getOrangeRiskCountries()--.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RiskCountriesExtraction {
    private static boolean internetConnection = MainActivity.internetConnection;
    private final static String TAG = "RiskCountriesExtracion";


    //get HTML Website in a string from the website
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

    //get html website from RKI website and save it into a csv file
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveCsv() throws IOException {
        Log.d(TAG, "data will be saved to the csv file");
        URL url = new URL("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html");
        URLConnection urlc = url.openConnection();
        urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputFile = urlc.getInputStream();
        Scanner scanner = new Scanner(inputFile);

        // save the website in a file in the case there is no internet next time
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files");
        dir.mkdirs();
        File file = new File(dir, "Risk-Countries.csv");
        file.createNewFile();

        try {
            FileWriter writer = new FileWriter(file);

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                nextLine = nextLine.replaceAll(";", "");
                writer.append(nextLine);
                writer.append("\n");
            }
            writer.flush();
            writer.close();
            Log.d(TAG, "File written and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //saves the date when it was last updated and returns if the data should be updated again or not
    //the data will be updated is more than 24h have passed
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean saveLastUpdated() throws IOException {
        String csvFile = "LastUpdatedRiskCountries.csv";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        String nowString = now.toString();

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files");
        File file = new File(dir, csvFile);

        if (!file.exists()){
            //create a file to save the last update of the data
            file.createNewFile();
            try {
                FileWriter writer = new FileWriter(file);
                writer.append(nowString);
                writer.flush();
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                ZonedDateTime lastUpdated = ZonedDateTime.parse(line);
                Duration duration = Duration.between(lastUpdated, now);
                long durationHours = duration.toHours();
                if (durationHours > 24){
                    file.createNewFile();
                    try {
                        FileWriter writer = new FileWriter(file);
                        writer.append(nowString);
                        writer.flush();
                        writer.close();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return false;
    }

    //this functions check if the file with the bing date should be saved
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void saveData() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/Risk-Countries.csv");
        if(!dir.exists()) {
            AsyncTask.execute(() -> {
                try {
                    saveCsv();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            boolean update = saveLastUpdated();
            Log.d(TAG, "data needs to be updated: "+update);
            if (update){
                AsyncTask.execute(() -> {
                    try {
                        saveCsv();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getOfflineWebsite() throws IOException {
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/"+"Risk-Countries.csv";
        File file = new File(csvFile);

        List<String> lines = Files.readAllLines(file.toPath());
        String result = "";
        for (String line : lines){
            result = result + line;
        }
        return result;
    }

    //return list of countries that are a risk area - should be used for the red coroni
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> getRedRiskCountries() throws IOException {
        String riskAreaHtml;
        if (internetConnection) {
            riskAreaHtml = getHtmlWebsite();
        } else {
            riskAreaHtml = getOfflineWebsite();
        }
        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p><strong>1. Folgende Staaten gelten aktuell als Virusvarianten-Gebiete:</strong></p>") +
                        "<p><strong>1. Folgende Staaten gelten aktuell als Virusvarianten-Gebiete:</strong></p>".length(),
                riskAreaHtml.indexOf("<p><strong>4. Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</strong></p>"));

        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("</li>", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        return convertedCountriesList;
    }

    //return list of countries that were a risk area in the last 10 days but not anymore - should be used for the orange coroni
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> getOrangeRiskCountries() throws IOException {
        String riskAreaHtml;
        if (internetConnection) {
            riskAreaHtml = getHtmlWebsite();
        } else {
            riskAreaHtml = getOfflineWebsite();
        }
        String list = riskAreaHtml.substring(
                riskAreaHtml.indexOf("<p><strong>4. Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</strong></p>") +
                        "<p><strong>4. Gebiete, die zu einem beliebigen Zeitpunkt in den vergangenen 10 Tagen Risikogebiete waren, aber derzeit KEINE mehr sind:</strong></p>".length(),
                riskAreaHtml.indexOf("<div class=\"sectionRelated links\">"));


        List<String> convertedCountriesList = new ArrayList<String>(Arrays.asList(list.split("</li>", -1)));
        convertedCountriesList = getRiskCountries(convertedCountriesList);

        return convertedCountriesList;
    }

    //return a list of countries without the HTML format, extracting the countries that are included in the countriesDict hashtable
    private static List<String> getRiskCountries(List<String> convertedCountriesList){

        //delete the exceptions (regions, cities, islands, etc.) from the list elements
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

        //add countries to "regions" list if they are in the website and in the hashtable countriesDict
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
        return regions;
    }

    //special function for extra regions that are e.g. grouped countries
    private static ArrayList<String> getExtraRegions(ArrayList<String> convertedList){

        if(convertedList.contains("Palästinensische Gebiete")){
            convertedList.add("Westjordanland");
            convertedList.add("Gazastreifen");
        }

        return convertedList;
    }
}
