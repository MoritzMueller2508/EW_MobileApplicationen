package com.dhbw.tinf19ai.CoroniReisen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class BingData extends Activity {

    //TODO: make it possible to update only once a day. So the file is not overwritten every time the app is opened.
    //read and write csv data from github repo
    public static FileWriter getBingDataOnline() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/microsoft/Bing-COVID-19-Data/master/data/Bing-COVID19-Data.csv");
        URLConnection urlc = url.openConnection();
        urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputFile = urlc.getInputStream();
        Scanner scanner = new Scanner(inputFile);

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files");
        dir.mkdirs();
        File file = new File(dir, "Bing-COVID19-Data.csv");
        file.createNewFile();

        try {
            FileWriter writer = new FileWriter(file);

            while (scanner.hasNextLine()) {
                writer.append(scanner.nextLine());
                writer.append("\n");
            }
            writer.flush();
            writer.close();
            System.out.println("File written and saved");
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //Get csv data
    /**  @param countryRegion is specific country or region i.a. "Worldwide" or "Germany"
     *    !!! Please enter region or country capitalized i.e. "Germany" instead of "germany" or "GERMANY"
     *    TODO: country or region should be case insensitive
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String[]> getCsvData(String countryRegion) throws IOException {
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/"+"Bing-COVID19-Data.csv";
        String csvSplitBy = ",";
        ArrayList<String[]> bingData = new ArrayList<>();
        ArrayList<String[]> bingDataTemp = new ArrayList<>();
        File file = new File(csvFile);
        List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("cp1252"));

        for (String line : lines) {
            if (line.contains(countryRegion)) {
                String[] array = line.split(csvSplitBy);

                //the arrays with array.length > 13 are for specific cities. Search for cities has not yet been implemented.
                if (array.length < 14){
                    bingDataTemp.add(line.split(csvSplitBy));
                }
            }
        }

        bingData.add(bingDataTemp.get(bingDataTemp.size()-1));

        /*
        for(int row=0; row < bingData.size(); row++){
            System.out.println(Arrays.toString(bingData.get(row)));
        }*/
        return bingData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getID(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String id;
        String[] array = bingData.get(0);
        id = array[0];
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getConfirmedCases(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String confirmedCases = array[2];
        System.out.println(confirmedCases);
        return confirmedCases;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDeathsCases(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String deathCases = array[4];
        System.out.println(deathCases);
        return deathCases;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getRecoveredCases(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String recoveredCases = array[6];
        System.out.println(recoveredCases);
        return recoveredCases;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getLatitude(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String latitude = array[8];
        System.out.println(latitude);
        if (latitude.isEmpty()){return null;}
        return latitude;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getLongitude(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String longitude = array[9];
        System.out.println(longitude);
        if (longitude.isEmpty()){return null;}
        return longitude;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getISO2(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String iso2 = array[10];
        System.out.println(iso2);
        if (iso2.isEmpty()){return null;}
        return iso2;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getISO3(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String iso3 = array[11];
        System.out.println(iso3);
        if (iso3.isEmpty()){return null;}
        return iso3;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date getLastUpdated(String countryRegion) throws IOException, ParseException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String date = array[1];
        Date lastUpdated =new SimpleDateFormat("MM/dd/yyyy").parse(date);

        System.out.println(lastUpdated);
        return lastUpdated;
    }
}

