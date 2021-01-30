package com.dhbw.tinf19ai.CoroniReisen;
/**
 * This class assigns the appropriate coroni to the countries which are assigned to the different groups in the class RiskCountriesExtraction.
 */

import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CoroniAssignment {
    private final static String TAG = "CoroniAssignment";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCoroni(String countryRegion) throws IOException {
        String redCoroni = "red";
        String orangeCoroni = "orange";
        String greenCoroni = "green";

        //ArrayList<String> countries = getAllCountries();
        System.out.println("till here");
        List<String> redRiskCountries = RiskCountriesExtraction.getRedRiskCountries();
        System.out.println(redRiskCountries);
        List<String> orangeRiskCountries = RiskCountriesExtraction.getOrangeRiskCountries();
        System.out.println(orangeRiskCountries);

        //assign right coroni
        for (int i = 0; i < orangeRiskCountries.size(); i++) {
            String element = orangeRiskCountries.get(i);
            if (redRiskCountries.contains(element)) {
                orangeRiskCountries.remove(i);
            }
        }

        if (redRiskCountries.contains(countryRegion)) {
            return redCoroni;
        }
        if (orangeRiskCountries.contains(countryRegion)) {
            return orangeCoroni;
        }

        return greenCoroni;
    }
}
