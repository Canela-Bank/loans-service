package com.canela.service.deletecreditrequest.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@RestController
@RequestMapping("/credit")
public class DeleteCreditRequest {
    @DeleteMapping("/request")
    public ResponseEntity<String> delete (@PathVariable String cRequestId) {
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
