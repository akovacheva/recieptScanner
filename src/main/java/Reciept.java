import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Reciept {

    //skraten text

    static String usingSplitMethod(String text, int length) {

        String[] results = text.split("(?<=\\G.{" + length + "})");

        return results[0];
    }



    public static void main(String[] args) throws IOException, ParseException {


        // vlecme od api-to
        URL url = new URL("https://interview-task-api.mca.dev/qr-scanner-codes/alpha-qr-gFpwhsQ8fkY1");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //pravime konekcija
        conn.setRequestMethod("GET");
        conn.connect(); //zemame podatoci

        //Check if connect is made
        int responseCode = conn.getResponseCode();

        // 200 OK
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }

            //Close the scanner
            scanner.close();


            //JSON simple library Setup with Maven is used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));



            ArrayList<String> names = new ArrayList<String>();

            for (int k = 0; k < dataObject.size(); k++) {
                JSONObject country = (JSONObject) dataObject.get(k);
                if (country.get("weight") == (null)) {
                    names.add("... "+(String) country.get("name") + "," +"\tPrice: $"+ (country.get("price")) + "," +"\t"+ usingSplitMethod((String) country.get("description"), 10) +"..."+ "," + "\tWeight: N/A" );
                } else
                    names.add("... " + (String) country.get("name") + "," +"\tPrice: $"+ (country.get("price")) + "," +"\t"+ usingSplitMethod((String) country.get("description"), 10)+"..." + "," + "\tWeight: "+ country.get("weight") +"g");



                if ((Boolean) country.get("domestic")) {

                    String temp;
                    for (int i = 0; i < names.size(); i++) {
                        for (int j = i + 1; j < names.size(); j++) {

                            // to compare one string with other strings
                            if (names.get(i).compareTo(names.get(j)) > 0) {
                                // swapping
                                temp = names.get(i);
                                names.set(i, names.get(j));
                                names.set(j, temp);
                            }
                            String formattedString = names.toString()
                                    .replace(",", "\n")//remove the commas
                                    .replace("[", "")  //remove the right bracket
                                    .replace("]", "")  //remove the left bracket
                                    .trim();

                            System.out.println(". Domestic");
                            System.out.println(formattedString);
                        }

                    }

                } else{

                    String formattedString = names.get(k).toString()
                            .replace(",", "\n")//remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();
                            System.out.println(". Imported");
                            System.out.println(formattedString);
                        }
                    }


            //Domestic cost i Imported cost
            ArrayList<Double> lista3 = new ArrayList<>();
            double price_d = 0;
            double price_i = 0;
            for (int k = 0; k < dataObject.size(); k++) {
                JSONObject country = (JSONObject) dataObject.get(k);

                lista3.add((Double) country.get("price"));
                if ((Boolean) country.get("domestic")) {

                    price_d += lista3.get(k);

                } else {

                    price_i += lista3.get(k);

                }
            }

            System.out.println("Domestic cost: $" + price_d);
            System.out.println("Imported cost: $" + price_i);


            //Domestic count i Imported count
            int d_count = 0;
            int i_count = 0;

            for (int i = 0; i < dataObject.size(); i++) {

                JSONObject country = (JSONObject) dataObject.get(i);

                if ((Boolean) country.get("domestic")) {

                    d_count++;
                } else
                    i_count++;
            }
            System.out.println("Domestic count: " + d_count);
            System.out.println("Imported count: " + i_count);


        }

            }

                 }






