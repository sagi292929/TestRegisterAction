package RegistrationPackage;

import static org.junit.jupiter.api.Assertions.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

import org.junit.jupiter.api.Test;

import jdk.jfr.Description;

public class RegistrationTests {
	
	private String requestUrl = "https://rest-eus1.ott.kaltura.com/restful_v5_0/api_v3/service/ottuser/action/register";
	
	
	@Test
	@Description(value = "New user Registration") 
	public void test_sanityByObject() throws JsonGenerationException, JsonMappingException, IOException {
		
		String rndUserName = GetRandomUserName();
		
		Payload payload = new Payload();
		payload.partnerId = 185;
		payload.password = "password";
		
		User user = new User();
		user.objectType = "KalturaOTTUser";
		user.username = rndUserName + "1537875168493";
		user.firstName = GetRandomUserName();
		user.lastName = "1537875168493";
		user.email = "qaott2018+NEW_USER1537875168493@gmail.com";
		user.address = "NEW_USER fake address";
		user.city = "NEW_USER fake city";
		user.countryId = 7;
		user.externalId = rndUserName + "1537875168493";
		
		payload.user = user;
		
		String res = SendPayloadAndGetResponse(payload);
		
		//Note - I've tried to use the response as object but it was inconsistent (I'll explain more by mail)
	//	Response response = GetResponse(res);
	//	assertTrue(response.result.username == rndUserName);
		
		assertTrue(res.contains(rndUserName));
		assertFalse(res.contains("error"));
	}
	

	
	@Test
	@Description(value = "Negative test - User exists") 
	public void test_Negative1ByObject() throws JsonGenerationException, JsonMappingException, IOException 
	{
		Payload payload = new Payload();
		payload.partnerId = 185;
		payload.password = "password";
		
		User user = new User();
		user.objectType = "KalturaOTTUser";
		user.username = "sameUser";
		user.firstName = "dfdee";
		user.lastName = "1537875168493";
		user.email = "qaott2018+sameUser@gmail.com";
		user.address = "dfdee fake address";
		user.city = "dfdee fake city";
		user.countryId = 7;
		user.externalId = "sameUser1537875168493";
		
		payload.user = user;
		
		String res = SendPayloadAndGetResponse(payload);
		
		assertTrue(res.contains("\"code\": \"2014\""), res);
		assertTrue(res.contains("error"));
		assertTrue(res.contains("User exists"));
	}
	
	@Test
	@Description(value = "Negative test - Missing parameter") 
	public void test_Negative2ByObject() throws JsonGenerationException, JsonMappingException, IOException {
		
		Payload payload = new Payload();
		payload.partnerId = 185;
		payload.password = "password";
		
		String res = SendPayloadAndGetResponse(payload);
	
		assertTrue(res.contains("Missing parameter [user]"));
		assertTrue(res.contains("error"));
		assertTrue(res.contains("\"code\": \"500013\""));
	}
	
	
	
	private Response GetResponse(String jsonRes) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		Response response = mapper.readValue(jsonRes, Response.class);
		
		return response;
	}
	
	private String SendPayloadAndGetResponse(Payload payload) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		String payloadStr = mapper.writeValueAsString(payload);

		String res = SendPostRequest(requestUrl, payloadStr);
		
		return res;
	}
	
	private String SendPostRequest(String requestUrl, String jsonInputString) {
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
		RandomString rs = new RandomString(11);
		
		return rs.nextString();
	
		
// byte[] array = new byte[7];
//    new Random().nextBytes(array);
//    String generatedString = new String(array, Charset.forName("UTF-8"));
//    
//    return generatedString;
	}
}
