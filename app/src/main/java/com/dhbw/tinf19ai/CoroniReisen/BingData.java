package com.dhbw.tinf19ai.CoroniReisen;
/**
 * This class connects to the Bing Covid API. The data is imported from the api periodically and written to a csv to get the current numbers.
 * <p>
 * We get the following data:
 * ID (country)
 * Updated (date)
 * Confirmed (confirmed cases)
 * ConfirmedChange (rate of change of cases from previous day)
 * Deaths (confirmed deaths)
 * DeathsChange (Rate of change of deaths from the previous day)
 * Recovered (Recovered cases)
 * RecoveredChange (Rate of change recovered from the previous day
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

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
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class BingData extends Activity {

    //initialize values and objects
    private final static String TAG = "BingData";

    //read and write csv data from github repo
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static FileWriter getBingDataOnline() throws IOException {
        Log.d(TAG, "data will be saved to the csv file");
        URL url = new URL("https://media.githubusercontent.com/media/microsoft/Bing-COVID-19-Data/master/data/Bing-COVID19-Data.csv");
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
            Log.d(TAG, "File written and saved");
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //saves the date when it was last updated and returns if the data should be updated again or not
    //the data will be updated is more than 24h have passed
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean saveLastUpdated() throws IOException {
        String csvFile = "LastUpdated.csv";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        String nowString = now.toString();

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files");
        File file = new File(dir, csvFile);

        if (!file.exists()) {
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
                if (durationHours > 24) {
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
    public static void saveBingData() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/Bing-COVID19-Data.csv");
        if (!dir.exists()) {
            AsyncTask.execute(() -> {
                try {
                    getBingDataOnline();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            boolean update = saveLastUpdated();
            Log.d(TAG, "data needs to be updated: " + update);
            if (update) {
                AsyncTask.execute(() -> {
                    try {
                        getBingDataOnline();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }


    //Get csv data

    /**  @param countryRegion is specific country or region i.a. "Worldwide" or "Germany"
     *    !!! Please enter region or country capitalized i.e. "Germany" instead of "germany" or "GERMANY"
     *    TODO: country or region should be case insensitive
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static ArrayList<String[]> getCsvData(String countryRegion) throws IOException {
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files" + "/Bing-COVID19-Data.csv";
        String csvSplitBy = ",";
        ArrayList<String[]> bingData = new ArrayList<>();
        ArrayList<String[]> bingDataTemp = new ArrayList<>();
        File file = new File(csvFile);
        List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("cp1252"));
        System.out.println("works");

        for (String line : lines) {
            if (line.contains(countryRegion)) {
                String[] array = line.split(csvSplitBy);

                //the arrays with array.length > 13 are for specific cities. Search for cities has not yet been implemented.
                if (array.length < 14) {
                    bingDataTemp.add(line.split(csvSplitBy));
                }
            }
        }
        System.out.println("worksII");
        bingData.add(bingDataTemp.get(bingDataTemp.size() - 1));

        /* auslesen der kompletten Datei
        for(int row=0; row < bingData.size(); row++){
            System.out.println(Arrays.toString(bingData.get(row)));
        }*/
        return bingData;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getArrayCountry(String countryRegion) throws IOException {
        ArrayList<String[]> bingData = getCsvData(countryRegion);
        String[] array = bingData.get(0);
        String[] arrayCountry = {array[2], array[4], array[6]};
        return arrayCountry;
    }
}

