package com.dhbw.tinf19ai.CoroniReisen;
/**
 * This class assigns the appropriate coroni to the countries which are assigned to the different groups in the class RiskCountriesExtraction.
 */

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class CoroniAssignment {
    //initialize values and objects
    private final static String TAG = "CoroniAssignment";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCoroni(String countryRegion) throws IOException {
        String redCoroni = "red";
        String orangeCoroni = "orange";
        String greenCoroni = "green";

        //ArrayList<String> countries = getAllCountries();
        List<String> redRiskCountries = RiskCountriesExtraction.getRedRiskCountries();
        Log.i("RedRiskCountries", "Got Red_Risk countries with red Coroni");
        List<String> orangeRiskCountries = RiskCountriesExtraction.getOrangeRiskCountries();
        Log.i("OrangeRiskCountries", "Got Orange_Risk countries with orange Coroni" );

        //assign right coroni
        for (int i = 0; i < orangeRiskCountries.size(); i++) {
            String element = orangeRiskCountries.get(i);
            if (redRiskCountries.contains(element)) {
                orangeRiskCountries.remove(i);
            }
        }

        if (redRiskCountries.contains(countryRegion)) {
            Log.i("Assigned Coroni", "getCoroni: Red ");
            return redCoroni;
        }
        if (orangeRiskCountries.contains(countryRegion)) {
            Log.i("Assigned Coroni", "getCoroni: Orange ");
            return orangeCoroni;
        }

        Log.i("Assigned Coroni", "getCoroni: Green");
        return greenCoroni;
    }
}
