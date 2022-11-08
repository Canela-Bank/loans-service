package com.canela.service.deletecreditrequest.controllers;
import com.canela.service.deletecreditrequest.model.RequestCR;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class DeleteCreditRequest {
    StringBuilder result = null;

    @PostMapping("/requestCR")
    public ResponseEntity<String> requestCR(@RequestBody String User) throws JSONException {
        try {
            // Add URL API
            JSONObject clientObject = new JSONObject(User);
            String type = (String) clientObject.get("type");
            int document = (int) clientObject.get("document");
            URL url = new URL ("10.2.0.0/api/prov/centralderiesgo/getReports/"+ type +"/"+ document +"");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    try {
                        JSONObject body = new JSONObject(result.toString());
                        JSONObject data = (JSONObject) body.get("data");
                        JSONObject user = (JSONObject) data.get("user");
                        JSONObject finalData1 = data;
                        JSONObject dataReturn = new JSONObject(){{
                            put("document", user.get("document"));
                            put("type", user.get("type"));
                            put("firstname", user.get("firstname"));
                            put("lastname", user.get("lastname"));
                            put("mail", user.get("mail"));
                            put("phone", user.get("phone"));
                            put("request", "card");
                            put("status", "wait");
                        }};
                        url = new URL("http://localhost:4000/graphql?query=mutation{createCreditRequest(document:\"" + document + "\")}");
                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        int responseCode2 = con.getResponseCode();
                        if(responseCode2 == HttpURLConnection.HTTP_OK){
                            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            result = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject response = new JSONObject(result.toString());
                            data = (JSONObject) ((JSONObject) response.get("data")).get("createCreditRequest");
                            HttpURLConnection finalCon1 = con;
                            JSONObject finalData = data;
                            return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(new JSONObject(){{
                                put("status", finalCon1.getResponseCode());
                                put("data", finalData);
                            }}.toString());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpURLConnection.HTTP_INTERNAL_ERROR).body(new JSONObject(){{
                        put("status", HttpURLConnection.HTTP_INTERNAL_ERROR);
                        put("message", e.getMessage());
                    }}.toString());
                }
            }
            HttpURLConnection finalCon = con;
            return ResponseEntity.status(responseCode).body(new JSONObject(){{
                put("status", responseCode);
                put("message", finalCon.getResponseMessage());
            }}.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpURLConnection.HTTP_INTERNAL_ERROR).body(new JSONObject(){{
                put("status", HttpURLConnection.HTTP_INTERNAL_ERROR);
                put("message", e.getMessage());
            }}.toString());
        }
    }

    @DeleteMapping("/request")
    public ResponseEntity<String> delete (@RequestBody String requestBody) throws JsonProcessingException {
        Map<String, Object> values = new ObjectMapper().readValue(requestBody, HashMap.class);
        String cRequestId = (String) values.get("cRequestId");

        URL url = null;
        try {
            url = new URL("http://localhost:4000/graphql?query=mutation{deleteCreditRequest(crId:\"" + cRequestId + "\")}");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                return ResponseEntity.status(HttpStatus.OK).body("Eliminado");
            }
        }
        catch(MalformedURLException e){
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(404).body("No se pudo eliminar");
    }
}
