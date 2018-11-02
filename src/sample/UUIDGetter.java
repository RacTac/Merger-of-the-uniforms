package sample;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UUIDGetter {

    private static String uuid = null;

    public static final String getUUID(String player) {
        
        try {
            System.out.println(System.currentTimeMillis()/1000);
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player + "?at=" + System.currentTimeMillis()/1000);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();
            System.out.println("Sending request at " + url.toString() + "; RESPONSE CODE: " + responseCode);

            // Read from the document
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer jsonResponse = new StringBuffer();
            while((inputLine = reader.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            reader.close();
            System.out.println(jsonResponse);

            JSONObject obj = new JSONObject(jsonResponse.toString());
            uuid = obj.getString("id");

            return uuid;
        } catch (IOException | JSONException e) {
            return  null;
        }
    }

}
