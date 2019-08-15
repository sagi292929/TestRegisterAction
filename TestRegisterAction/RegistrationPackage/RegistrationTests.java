package RegistrationPackage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

import org.junit.jupiter.api.Test;

import jdk.jfr.Description;

public class RegistrationTests {
	
	@Test
	@Description(value = "Negative test - User exists") 
	public void test_Negative1() {
		
		String requestUrl = "https://rest-eus1.ott.kaltura.com/restful_v5_0/api_v3/service/ottuser/action/register";
		String payload = "{\r\n" + 
				"  \"partnerId\": 185,\r\n" + 
				"  \"user\": {\r\n" + 
				"    \"objectType\": \"KalturaOTTUser\",\r\n" + 
				"    \"username\": \"sameUser\",\r\n" + 
				"    \"firstName\": \"dfdee\",\r\n" + 
				"    \"lastName\": \"1537875168493\",\r\n" + 
				"    \"email\": \"qaott2018+sameUser@gmail.com\",\r\n" + 
				"    \"address\": \"dfdee fake address\",\r\n" + 
				"    \"city\": \"dfdee fake city\",\r\n" + 
				"    \"countryId\": 7,\r\n" + 
				"    \"externalId\": \"sameUser\"\r\n" + 
				"  },\r\n" + 
				"  \"password\": \"password\"\r\n" + 
				"}\r\n";
		
		sendPostRequest(requestUrl, payload);
		
		String res = sendPostRequest(requestUrl, payload);
		
		assertTrue(res.contains("\"code\": \"2014\""));
		assertTrue(res.contains("error"));
		assertTrue(res.contains("User exists"));
	}
	
	
	@Test
	@Description(value = "Negative test - Missing parameter") 
	public void test_Negative2() {
		
		String requestUrl = "https://rest-eus1.ott.kaltura.com/restful_v5_0/api_v3/service/ottuser/action/register";
		String payload = "{\"partnerId\": 185}";
		
		sendPostRequest(requestUrl, payload);
		
		String res = sendPostRequest(requestUrl, payload);
		
		assertTrue(res.contains("Missing parameter [user]"));
		assertTrue(res.contains("error"));
		assertTrue(res.contains("\"code\": \"500013\""));
	}
	
	
	@Test
	@Description(value = "New user Registration") 
	public void test_sanity() {
		
		String requestUrl = "https://rest-eus1.ott.kaltura.com/restful_v5_0/api_v3/service/ottuser/action/register";
		String payload = "{\r\n" + 
				"  \"partnerId\": 185,\r\n" + 
				"  \"user\": {\r\n" + 
				"    \"objectType\": \"KalturaOTTUser\",\r\n" + 
				"    \"username\": \"NEW_USER1537875168493\",\r\n" + 
				"    \"firstName\": \"NEW_USER\",\r\n" + 
				"    \"lastName\": \"1537875168493\",\r\n" + 
				"    \"email\": \"qaott2018+NEW_USER1537875168493@gmail.com\",\r\n" + 
				"    \"address\": \"NEW_USER fake address\",\r\n" + 
				"    \"city\": \"NEW_USER fake city\",\r\n" + 
				"    \"countryId\": 7,\r\n" + 
				"    \"externalId\": \"NEW_USER1537875168493\"\r\n" + 
				"  },\r\n" + 
				"  \"password\": \"password\"\r\n" + 
				"}\r\n";
		
		//	String payload = "{  \"partnerId\": 185,  \"user\": {    \"objectType\": \"KalturaOTTUser\",    \"username\": \"dfdee1537875168491\",    \"firstName\": \"dfdee\",    \"lastName\": \"1537875168491\",    \"email\": \"qaott2018+dfdee1537875168491@gmail.com\",    \"address\": \"dfdee fake address\",    \"city\": \"dfdee fake city\",    \"countryId\": 7,    \"externalId\": \"dfdee1537875168491\"  },  \"password\": \"password\"}";
		
		String RndUserName = GetRandomUserName();
		payload = payload.replace("NEW_USER", RndUserName);
		
		String res = sendPostRequest(requestUrl, payload);
		
		assertTrue(res.contains(RndUserName));
		assertFalse(res.contains("error"));
	}
	
	private static String sendPostRequest(String requestUrl, String jsonInputString) {
	    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json");

	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(jsonInputString);
	        writer.flush();
	        writer.close();
	        
	      //display what returns the POST request
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	        return jsonString.toString();
	    } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	    }
	}
	
	private String GetRandomUserName()
	{
		 byte[] array = new byte[7];
		    new Random().nextBytes(array);
		    String generatedString = new String(array, Charset.forName("UTF-8"));
		    
		    return generatedString;
	}
}
