package com.canela.service.loanmgmt.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




//id: loanManagementService
//uri: http://10.0.0.0:9002/

@RestController
@RequestMapping(value = "/api/loans")
public class GetLoansControllers {
	
	 @GetMapping(value = "/getUserLoans/{document}/{typeDocument}")
	    public ResponseEntity<String> getUserLoans(@PathVariable String document, @PathVariable String typeDocument) {
		 
		 try {
				// GraphQL info 
				 String url = "http://localhost:3002/graphql";
				 String operation = "getLoansByUser"; //INSERT OPERATION QUERY HERE
				 String query = "query{getLoansByUser(user_document:\""+document+"\",user_document_type:"+typeDocument+"){\n"
				 		+ "  id\n"
				 		+ "  interest_rate\n"
				 		+ "  min_payment\n"
				 		+ "  balance\n"
				 		+ "  payment_date\n"
				 		+ "  debt\n"
				 		+ "  user_id\n"
				 		+ "  user_document_type\n"
				 		+ "  \n"
				 		+ "}}"; //INSERT QUERY HERE
				
				 // GraphQL request 
					 CloseableHttpClient client = HttpClientBuilder.create().build();
				        HttpGet requestGraphQL = new HttpGet(url);
				        URI uri = new URIBuilder(requestGraphQL.getURI())
				                .addParameter("query", query)
				                .build();
				        requestGraphQL.setURI(uri);
				        HttpResponse response =  client.execute(requestGraphQL);
				        InputStream inputResponse = response.getEntity().getContent();
				        String actualResponse = new BufferedReader(
				                new InputStreamReader(inputResponse, StandardCharsets.UTF_8))
				                .lines()
				                .collect(Collectors.joining("\n"));

				        final ObjectNode node = new ObjectMapper().readValue(actualResponse, ObjectNode.class);
				        
				        JsonNode loans = node.get("data").get(operation);	
				        
				    // Verify Empty Response  
				        if(loans.isEmpty()) {
				        	 return ResponseEntity.status(HttpURLConnection.HTTP_NOT_FOUND).body("Lo sentimos, hubo un error.");
				        }
				     // Return response 
				        else{
				        	 JsonNode UserLoans = node.get("data").get(operation);
						        
						     return ResponseEntity.status(HttpURLConnection.HTTP_ACCEPTED).body(UserLoans.toString());
				        }

			} catch (Exception e) {
				throw new RuntimeException(e);			
			}	 
		 
	 }
	 
	 static class loansRequest {

	     private String userDocument;
	     private int typeDocument;
		public String getUserDocument() {
			return userDocument;
		}
		public void setUserDocument(String userDocument) {
			this.userDocument = userDocument;
		}
		public int getTypeDocument() {
			return typeDocument;
		}
		public void setTypeDocument(int typeDocument) {
			this.typeDocument = typeDocument;
		}
 
	 }
	 
}
