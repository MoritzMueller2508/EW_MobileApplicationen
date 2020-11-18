package com.dhbw.tinf19ai.CoroniReisen;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class RiskCountriesExtraction extends AppCompatActivity {

    public static void riskCountriesExtraction() throws IOException {
        URL riskAreaUrl = new URL("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html");

        Scanner scanner = new Scanner(riskAreaUrl.openStream());

        StringBuffer html = new StringBuffer();

        while (scanner.hasNextLine()){
            html.append(scanner.next());
        }
        String result = html.toString();

        String list = result.substring(result.indexOf("<ul>")+6, result.indexOf("</ul>") );
        System.out.println(list);
    }

}
