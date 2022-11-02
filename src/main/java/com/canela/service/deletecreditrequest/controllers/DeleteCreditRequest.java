package com.canela.service.deletecreditrequest.controllers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class DeleteCreditRequest {
    @DeleteMapping("/request")
    public ResponseEntity<String> delete (@RequestBody String requestBody) throws JsonProcessingException {
        Map<String, Object> values = new ObjectMapper().readValue(requestBody, HashMap.class);
        String cRequestId = (String) values.get("cRequestId");

        URL url = null;
        try {
            url = new URL("http://localhost:8080/graphql?query=mutation{deleteCreditRequest(crId:\"" + cRequestId + "\")}");
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
