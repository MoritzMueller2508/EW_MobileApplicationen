package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class represents a hashtable with all countries of this world.
 */

import android.util.Log;

import java.util.Hashtable;
import java.util.Map;

public class CountryDictionary {
    //initialize values and objects
    public static Hashtable<String, String> countriesDict = new Hashtable<String, String>();

    //add countries to the dictionary
    public static void setCountriesDict() {
        countriesDict.put("Afghanistan", "Afghanistan");
        countriesDict.put("Albania", "Albanien");
        countriesDict.put("Algeria", "Algerien");
        countriesDict.put("American Samoa", "Amerikanisch-Samoa");
        countriesDict.put("Andorra", "Andorra");
        countriesDict.put("Angola", "Angola");
        countriesDict.put("Anguilla", "Anguilla");
        countriesDict.put("Antigua and Barbuda", "Antigua und Barbuda");
        countriesDict.put("Argentina", "Argentinien");
        countriesDict.put("Armenia", "Armenien");
        countriesDict.put("Aruba", "Aruba");
        countriesDict.put("Australia", "Australien");
        countriesDict.put("Austria", "Österreich");
        countriesDict.put("Azerbaijan", "Aserbaidschan");
        countriesDict.put("Bahamas", "Bahamas");
        countriesDict.put("Bahrain", "Bahrain");
        countriesDict.put("Bangladesh", "Bangladesch");
        countriesDict.put("Barbados", "Barbados");
        countriesDict.put("Belarus", "Belarus");
        countriesDict.put("Belgium", "Belgien");
        countriesDict.put("Belize", "Belize");
        countriesDict.put("Benin", "Benin");
        countriesDict.put("Bermuda", "Bermuda");
        countriesDict.put("Bhutan", "Bhutan");
        countriesDict.put("Bolivia", "Bolivien");
        countriesDict.put("Bonaire", "Bonaire");
        countriesDict.put("Bosnia and Herzegovina", "Bosnien und Herzegowina");
        countriesDict.put("Botswana", "Botsuana");
        countriesDict.put("Brazil", "Brasilien");
        countriesDict.put("British Virgin Islands", "Britische Jungferninseln");
        countriesDict.put("Brunei", "Brunei");
        countriesDict.put("Bulgaria", "Bulgarien");
        countriesDict.put("Burkina Faso", "Burkina Faso");
        countriesDict.put("Burundi", "Burundi");
        countriesDict.put("Cabo Verde", "Cabo Verde");
        countriesDict.put("Cambodia", "Kambodscha");
        countriesDict.put("Cameroon", "Kamerun");
        countriesDict.put("Canada", "Kanada");
        countriesDict.put("Cayman Islands", "Kaimaninseln");
        countriesDict.put("Central African Republic", "Zentralafrikanische Republik");
        countriesDict.put("Chad", "Tschad");
        countriesDict.put("Chile", "Chile");
        countriesDict.put("China (mainland)", "China");
        countriesDict.put("China", "China");
        countriesDict.put("Colombia", "Kolumbien");
        countriesDict.put("Comoros", "Komoren");
        countriesDict.put("Congo", "Republik Kongo");
        countriesDict.put("Kongo Rep", "Republik Kongo");
        countriesDict.put("Congo (DRC)", "Kongo DR");
        countriesDict.put("Demokratische Republik Kongo", "Kongo DR");
        countriesDict.put("Democratic Republic of the Congo", "Kongo DR");
        countriesDict.put("Costa Rica", "Costa Rica");
        countriesDict.put("Côte d'Ivoire", "Côte d'Ivoire");
        countriesDict.put("Croatia", "Kroatien");
        countriesDict.put("Cuba", "Kuba");
        countriesDict.put("Curaçao", "Curacao");
        countriesDict.put("Cyprus", "Zypern");
        countriesDict.put("Czechia", "Tschechien");
        countriesDict.put("Denmark", "Dänemark");
        countriesDict.put("Djibouti", "Dschibuti");
        countriesDict.put("Dominica", "Dominica");
        countriesDict.put("Dominican Republic", "Dominikanische Republik");
        countriesDict.put("Ecuador", "Ecuador");
        countriesDict.put("Egypt", "Ägypten");
        countriesDict.put("El Salvador", "El Salvador");
        countriesDict.put("Equatorial Guinea", "Äquatorialguinea");
        countriesDict.put("Eritrea", "Eritrea");
        countriesDict.put("Estonia", "Estland");
        countriesDict.put("Eswatini", "Eswatini");
        countriesDict.put("Ethiopia", "Äthiopien");
        countriesDict.put("Falkland Islands", "Falkland Inseln");
        countriesDict.put("Faroe Islands", "Färöer Inseln");
        countriesDict.put("Fiji", "Fidschi");
        countriesDict.put("Finland", "Finnland");
        countriesDict.put("France", "Frankreich");
        countriesDict.put("French Guiana", "Französisch-Guayana"); //Französisch-Guayana
        countriesDict.put("Französisch-Guyana", "Französisch-Guayana");
        countriesDict.put("French Polynesia", "Französisch-Polynesien"); //Französisch Polynesien
        countriesDict.put("French-Guadeloupe", "Guadeloupe"); //Französisch-Guadeloupe
        countriesDict.put("French-Martinique", "Martinique"); //Französisch-Martinique
        countriesDict.put("Saint Martin", "St. Martin");
        countriesDict.put("Réunion", "La Réunion");
        countriesDict.put("Reunion", "La Réunion");
        countriesDict.put("The Reunion", "La Réunion");
        countriesDict.put("La Reunion", "La Réunion");
        countriesDict.put("Gabon", "Gabun");
        countriesDict.put("Gaza Strip", "Gazastreifen");
        countriesDict.put("Georgia", "Georgien");
        countriesDict.put("Germany", "Deutschland");
        countriesDict.put("Ghana", "Ghana");
        countriesDict.put("Gibraltar", "Gibraltar");
        countriesDict.put("Greece", "Griechenland");
        countriesDict.put("Greenland", "Grönland");
        countriesDict.put("Grenada", "Grenada");
        countriesDict.put("Guam", "Guam");
        countriesDict.put("Guatemala", "Guatemala");
        countriesDict.put("Guernsey", "Guernsey");
        countriesDict.put("Guinea", "Guinea");
        countriesDict.put("Guinea-Bissau", "Guinea-Bissau");
        countriesDict.put("Guyana", "Guyana");
        countriesDict.put("Haiti", "Haiti");
        countriesDict.put("Honduras", "Honduras");
        countriesDict.put("Hong Kong SAR", "Hongkong"); //Sonderverwaltungszone
        countriesDict.put("Hong Kong", "Hongkong");
        countriesDict.put("Hungary", "Ungarn");
        countriesDict.put("Iceland", "Island");
        countriesDict.put("India", "Indien");
        countriesDict.put("Indonesia", "Indonesien");
        countriesDict.put("Iran", "Iran");
        countriesDict.put("Iraq", "Irak");
        countriesDict.put("Ireland", "Irland");
        countriesDict.put("Isle of Man", "Isle of Man");
        countriesDict.put("Israel", "Israel");
        countriesDict.put("Italy", "Italien");
        countriesDict.put("Jamaica", "Jamaika");
        countriesDict.put("Yemen", "Jemen");
        countriesDict.put("Japan", "Japan");
        countriesDict.put("Jersey", "Jersey");
        countriesDict.put("Jordan", "Jordanien");
        countriesDict.put("Kazakhstan", "Kasachstan");
        countriesDict.put("Kenya", "Kenia");
        countriesDict.put("Kosovo", "Kosovo");
        countriesDict.put("Kuwait", "Kuwait");
        countriesDict.put("Kyrgyzstan", "Kirgisistan");
        countriesDict.put("Laos", "Laos");
        countriesDict.put("Latvia", "Lettland");
        countriesDict.put("Lebanon", "Libanon");
        countriesDict.put("Lesotho", "Lesotho");
        countriesDict.put("Liberia", "Liberia");
        countriesDict.put("Libya", "Libyen");
        countriesDict.put("Liechtenstein", "Liechtenstein");
        countriesDict.put("Lithuania", "Litauen");
        countriesDict.put("Luxembourg", "Luxemburg");
        countriesDict.put("Macao SAR", "Macau");
        countriesDict.put("Madagascar", "Madagaskar");
        countriesDict.put("Malawi", "Malawi");
        countriesDict.put("Malaysia", "Malaysia");
        countriesDict.put("Maldives", "Malediven");
        countriesDict.put("Mali", "Mali");
        countriesDict.put("Malta", "Malta");
        countriesDict.put("Marshall Islands", "Marshallinseln");
        countriesDict.put("Mauritania", "Mauretanien");
        countriesDict.put("Mauritius", "Mauritius");
        countriesDict.put("Mayotte", "Mayotte");
        countriesDict.put("Mexico", "Mexiko");
        countriesDict.put("Moldova", "Republik Moldau");
        countriesDict.put("Monaco", "Monaco");
        countriesDict.put("Mongolia", "Mongolei");
        countriesDict.put("Montenegro", "Montenegro");
        countriesDict.put("Montserrat", "Montserrat");
        countriesDict.put("Morocco", "Marokko");
        countriesDict.put("Mozambique", "Mosambik");
        countriesDict.put("Myanmar", "Myanmar");
        countriesDict.put("Namibia", "Namibia");
        countriesDict.put("Nepal", "Nepal");
        countriesDict.put("Netherlands", "Niederlande");
        countriesDict.put("New Caledonia", "Neukaledonien");
        countriesDict.put("New Zealand", "Neuseeland");
        countriesDict.put("Nicaragua", "Nicaragua");
        countriesDict.put("Niger", "Niger");
        countriesDict.put("Nigeria", "Nigeria");
        countriesDict.put("North Macedonia", "Nordmazedonien");
        countriesDict.put("Northern Mariana Islands", "Nördliche Marianen");
        countriesDict.put("Norway", "Norwegen");
        countriesDict.put("Oman", "Oman");
        countriesDict.put("Pakistan", "Pakistan");
        countriesDict.put("Palestinian territories", "Palästinensische Gebiete");
        countriesDict.put("Panama", "Panama");
        countriesDict.put("Papua New Guinea", "Papua-Neuguinea");
        countriesDict.put("Paraguay", "Paraguay");
        countriesDict.put("Peru", "Peru");
        countriesDict.put("Philippines", "Philippinen");
        countriesDict.put("Poland", "Polen");
        countriesDict.put("Portugal", "Portugal");
        countriesDict.put("Puerto Rico", "Puerto Rico");
        countriesDict.put("Qatar", "Katar");
        countriesDict.put("Romania", "Rumänien");
        countriesDict.put("Russia", "Russische Föderation");
        countriesDict.put("Russland", "Russische Föderation");
        countriesDict.put("Rwanda", "Ruanda");
        countriesDict.put("Saba", "Saba");
        countriesDict.put("Saint Barthélemy", "Saint-Barthélemy");
        countriesDict.put("Saint Lucia", "St. Lucia");
        countriesDict.put("Saint Pierre and Miquelon", "Saint Pierre und Miquelon");
        countriesDict.put("Saint Vincent and the Grenadines", "St. Vincent und die Grenadinen");
        countriesDict.put("Samoa", "Samoa");
        countriesDict.put("San Marino", "San Marino");
        countriesDict.put("São Tomé and Príncipe", "São Tomé und Príncipe");
        countriesDict.put("Saudi Arabia", "Saudi-Arabien");
        countriesDict.put("Senegal", "Senegal");
        countriesDict.put("Serbia", "Serbien");
        countriesDict.put("Seychelles", "Seychellen");
        countriesDict.put("Sierra Leone", "Sierra Leone");
        countriesDict.put("Singapore", "Singapur");
        countriesDict.put("Slovakia", "Slowakei");
        countriesDict.put("Slovenia", "Slowenien");
        countriesDict.put("Solomon Islands", "Salomon-Inseln");
        countriesDict.put("Somalia", "Somalia");
        countriesDict.put("South Africa", "Südafrika");
        countriesDict.put("North Korea", "Nordkorea");
        countriesDict.put("Korea (Volksrepublik)", "Nordkorea");
        countriesDict.put("South Korea", "Südkorea");
        countriesDict.put("South Sudan", "Südsudan");
        countriesDict.put("Süd-Sudan", "Südsudan");
        countriesDict.put("Spain", "Spanien");
        countriesDict.put("Sri Lanka", "Sri Lanka");
        countriesDict.put("St Kitts and Nevis", "St. Kitts und Nevis");
        countriesDict.put("St Marten", "St. Maarten");
        countriesDict.put("St Vincent and the Grenadines", "St. Vincent und die Grenadinen");
        countriesDict.put("Sudan", "Sudan");
        countriesDict.put("Suriname", "Surinam");
        countriesDict.put("Sweden", "Schweden");
        countriesDict.put("Switzerland", "Schweiz");
        countriesDict.put("Syria", "Syrien");
        countriesDict.put("Syrische Arabische Republik", "Syrien");
        countriesDict.put("Taiwan", "Taiwan");
        countriesDict.put("Tajikistan", "Tadschikistan");
        countriesDict.put("Tanzania", "Tansania");
        countriesDict.put("Thailand", "Thailand");
        countriesDict.put("The Gambia", "Gambia");
        countriesDict.put("Timor-Leste", "Timor Leste");
        countriesDict.put("Togo", "Togo");
        countriesDict.put("Trinidad and Tobago", "Trinidad und Tobago");
        countriesDict.put("Trinidad Tobago", "Trinidad und Tobago");
        countriesDict.put("Tunisia", "Tunesien");
        countriesDict.put("Turkey", "Türkei");
        countriesDict.put("Turkmenistan", "Turkmenistan");
        countriesDict.put("Turks and Caicos Islands", "Turks- und Caicosinseln");
        countriesDict.put("U.S. Virgin Islands", "Amerikanische Jungferninseln");
        countriesDict.put("US Virgin Islands", "Amerikanische Jungferninseln");
        countriesDict.put("Uganda", "Uganda");
        countriesDict.put("Ukraine", "Ukraine");
        countriesDict.put("United Arab Emirates", "Vereinigte Arabische Emirate");
        countriesDict.put("United Kingdom", "Vereinigtes Königreich");
        countriesDict.put("United States", "USA");
        countriesDict.put("Uruguay", "Uruguay");
        countriesDict.put("Uzbekistan", "Usbekistan");
        countriesDict.put("Vanuatu", "Vanuatu");
        countriesDict.put("Vietnam", "Vietnam");
        countriesDict.put("Vatican City", "Vatikanstadt");
        countriesDict.put("Venezuela", "Venezuela");
        countriesDict.put("Wallis and Futuna", "Wallis und Futuna");
        countriesDict.put("Zambia", "Sambia");
        countriesDict.put("Zimbabwe", "Simbabwe");
        countriesDict.put("West Bank", "Westjordanland");

    }

    public static String getCountryInGerman(String countryEn) {
        //initialize values and objects
        String countryDe;
        //get translation from hashtable
        countryDe = countriesDict.get(countryEn);
        Log.i("Translation", "getCountryInGerman: translated country to german");
        return countryDe;
    }

    public static  String getCountryInEnglish(String countryDe){
        //initialize values and objects
        String countryEn;
        //special cases

        if(countryDe.equals("Russische Föderation"))
            return   countryEn = "Russia";

        else if(countryDe.equals("Republik Kongo"))
            return  countryEn = "Congo";

        else if(countryDe.equals("Kongo DR"))
            return countryEn = "Congo (DRC)";

        else if(countryDe.equals("China"))
            return countryEn = "China";

        else if(countryDe.equals("Französisch-Guayana"))
            return countryEn = "French Guiana";

        else if (countryDe.equals("La Réunion"))
            return countryEn = "Reunion"; //empty

        else if (countryDe.equals("Hongkong"))
            return countryEn = "Hong Kong SAR";

        else if (countryDe.equals("St. Vincent und die Grenadinen"))
            return countryEn = "Saint Vincent and the Grenadines";

        else if (countryDe.equals("Nordkorea"))
            return countryEn = "North Korea"; //empty

        else if (countryDe.equals("Südsudan"))
            return countryEn = "South Sudan";

        else if (countryDe.equals("Syrien"))
            return countryEn = "Syria";

        else if(countryDe.equals("Trinidad und Tobago"))
            return  countryEn = "Trinidad and Tobago";

        else if(countryDe.equals("Amerikanische Jungferninseln"))
            return countryEn = "U.S. Virgin Islands";


        else {

            //get translation from hashtable
            for (Map.Entry<String, String> e : countriesDict.entrySet() //iterate through valueSet - german String location
            ) {
                if (countryDe.equals(e.getValue())) {
                    countryEn = e.getKey();
                    Log.i("Translation", "getCountryInEnglish: translated country to english");
                    return countryEn;
                }
            }
        }

        return countryEn ="not found"; //return 'not found' if translation failed
    }

}
