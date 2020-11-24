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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class BingData extends Activity {

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

        System.out.println(dir.getAbsolutePath());
        System.out.println(dir.exists());
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
            System.out.println("it worked");
            System.out.println(file.getAbsolutePath());
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //Get csv data
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void getCsvData() throws IOException {
        getBingDataOnline();
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/"+"Bing-COVID19-Data.csv";
        String nextLine;
        String cvsSplitBy = ",";
        ArrayList<String[]> gilbertData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            while ((nextLine = br.readLine()) != null) {
                if(nextLine.split(cvsSplitBy).length>=2)
                    gilbertData.add(nextLine.split(cvsSplitBy));
            }
            System.out.println("reading worked");
            for(int gilbertDataRow=0; gilbertDataRow < 3; gilbertDataRow++){
                System.out.println(Arrays.toString(gilbertData.get(gilbertDataRow)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getID(){
        return null;
    }
}

