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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String> getAllBingCountriesGerman() throws IOException {
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dhbw.tinf19ai.CoroniReisen/files/"+"Bing-COVID19-Data.csv";
        String cvsSplitBy = ",";
        ArrayList<String> countries = new ArrayList<>();
        File file = new File(csvFile);
        List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("cp1252"));

        for (int i = 1; i < lines.size() - 1; i++){
            String line = lines.get(i);
            String line2 = lines.get(i+1);
            String[] array = line.split(cvsSplitBy);
            String[] array2 = line2.split(cvsSplitBy);

            if (!(array.length < 2)){
                String country = array[12];
                if (!(array2.length < 2)){
                    String country2 = array2[12];
                    if (!country.equals(country2)){
                        countries.add(array[12]);
                    }
                }
            }
        }

        //translate the countries from BING Database from english to german
        ArrayList<String> countriesDe = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++){
            String country = countries.get(i);
            String countryDe = CountryDictionary.getCountryInGerman(country);
            countriesDe.add(countryDe);
        }

        //add the risk countries to the list if not existant
        List<String> riskCountries = RiskCountriesExtraction.getRedRiskCountries();
        for (int i = 0; i < riskCountries.size(); i++){
            String riskCountry = riskCountries.get(i);
            if (!countriesDe.contains(riskCountry)){
                countriesDe.add(riskCountry);
            }
        }
        List<String> orangeRiskCountries = RiskCountriesExtraction.getOrangeRiskCountries();
        for (int i = 0; i < orangeRiskCountries.size(); i++){
            String riskCountry = orangeRiskCountries.get(i);
            if (!countriesDe.contains(riskCountry)){
                countriesDe.add(riskCountry);
            }
        }

        return countriesDe;
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCoroni(String countryRegion) throws IOException {
        String redCoroni = "red";
        String orangeCoroni = "orange";
        String greenCoroni = "green";

        //ArrayList<String> countries = getAllCountries();
        List<String> redRiskCountries = RiskCountriesExtraction.getRedRiskCountries();
        List<String> orangeRiskCountries = RiskCountriesExtraction.getOrangeRiskCountries();

        //assign right coroni
        for (int i = 0; i<orangeRiskCountries.size(); i++){
            String element = orangeRiskCountries.get(i);
            if (redRiskCountries.contains(element)){
                orangeRiskCountries.remove(i);
            }
        }

        if (redRiskCountries.contains(countryRegion)){
            return redCoroni;
        }
        if (orangeRiskCountries.contains(countryRegion)){
            return orangeCoroni;
        }

        return greenCoroni;
    }
}
