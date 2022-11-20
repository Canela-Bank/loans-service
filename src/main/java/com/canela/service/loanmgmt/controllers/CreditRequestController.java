package com.canela.service.loanmgmt.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/api/loans")
public class CreditRequestController {
    StringBuilder result = null;

    @PostMapping("/create")
    public ResponseEntity<String> requestCR(@RequestBody String request) throws JSONException {
        URL url = null;
        try {
            // Add URL API
            JSONObject clientObject = new JSONObject(request);
            JSONObject user = (JSONObject) clientObject.get("User");
            String type = (String) user.get("type");
            int document = (int) user.get("document");
            url = new URL ("http://10.2.0.0:3000/api/prov/centralderiesgo/getReports/"+ type +"/"+ document +"");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
//                return new ResponseEntity(con.getResponseMessage(),HttpStatus.OK);
                JSONObject loan = (JSONObject) clientObject.get("Loan");
                String id = (String) loan.get("id");
                Double interest_rate = (Double) loan.get("interest_rate");
                Double min_payment = (Double) loan.get("min_payment");
                Double balance = (Double) loan.get("balance");
                String payment_date = (String) loan.get("payment_date");
                Double debt = (Double) loan.get("debt");
                String user_id = (String) loan.get("user_id");
                Integer user_document_type = (Integer) loan.get("user_document_type");
                try{
                    url = new URL("http://10.1.0.0:3001/graphql?query=mutation%7B%0A%20%20createLoan(id%3A%22"+id+"%22%2Cinterest_rate%3A"+interest_rate+"%2C%20min_payment%3A"+min_payment+"%2Cbalance%3A"+balance+"%2Cpayment_date%3A%22"+payment_date+"%22%2Cdebt%3A"+debt+"%2Cuser_id%3A%22"+user_id+"%22%2Cuser_document_type%3A"+user_document_type+")%7B%0A%20%20%20%20id%0A%20%20%7D%0A%7D");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    int response = conn.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK){
                        return ResponseEntity.status(HttpStatus.OK).body("Loan Creado");
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
                return ResponseEntity.status(404).body("No se pudo Crear");
            } else{
                return new ResponseEntity(con.getResponseMessage(),HttpStatus.BAD_REQUEST);
            }
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
            url = new URL("http://10.1.0.0:3001/graphql?query=mutation%20%7B%0A%20%20deleteLoan(id%3A%20\"" + cRequestId + "\")%20%7B%0A%20%20%20%20message%0A%20%20%7D%0A%7D");
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
