package com.canela.service.loanmgmt.crontroller;


import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.net.*;

@RestController
@RequestMapping("/api/credit")
public class controller {


    @PostMapping("/update/{CC}/{status}")
    public String updateUser(@PathVariable String CC, @PathVariable String status) {

        try {
            String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
            data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");
            String root;
            root = "http://localhost:4000/graphql?query=mutation%7B%0A%20%20updateStatusCredit(%20document%3A%20" + CC + "%2C%20status%3A%20\"" + status + "\")%7B%0A%20%20%20%20status%0A%20%20%7D%0A%20%20%0A%7D";

            URL url = new URL(root);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/all")
    public ResponseEntity<String> getALlUsersReport() {
        try {
            URL url = new URL("http://localhost:4000/graphql?query=%7B%0A%20%20getAllUsers%7B%0A%20%20%20%20document%2C%0A%20%20%20%20type%2C%0A%20%20%20%20firstname%2C%0A%20%20%20%20lastname%2C%0A%20%20%20%20mail%2C%0A%20%20%20%20percentage%2C%0A%20%20%20%20status%0A%20%20%7D%0A%7D");
            HttpURLConnection conection = (HttpURLConnection) url.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JSONObject object = new JSONObject(response.toString());
                object = (JSONObject) object.get("data");
                JSONArray array = (JSONArray) object.get("getAllUsers");


                return ResponseEntity.status(responseCode).body(new JSONObject() {{
                    put("status", HttpURLConnection.HTTP_OK);
                    put("data", array);

                }}.toString());

            } else
                return ResponseEntity.status(responseCode).body(conection.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

}