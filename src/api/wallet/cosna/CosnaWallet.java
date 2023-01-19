package api.wallet.cosna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CosnaWallet {
	
    private String master_key;
    private String private_key;
    
    /**
     * Constructs and object to access endpoints in the cosna wallet integration API.
     * @param master_key - The master key.
     * @param private_key - The private key.
     */
    public CosnaWallet(String master_key, String private_key) {
    	this.master_key = master_key;
    	this.private_key = private_key;
    }
    
    /**
     * Exposes an API endpoint to perform payments through cosna wallet.
     * @param payload - The payload that makes up the request body.
     * @param endpoint_url - The payment endpoint.
     * @return String
     * @throws Exception
     */
    public String init_payment(String payload, String endpoint_url) throws Exception {
    	HttpURLConnection connection = this.connect(endpoint_url);
    	connection.setConnectTimeout(5000);
    	connection.setReadTimeout(5000);
    	connection.setRequestMethod("POST");
    	connection.setRequestProperty("Content-Type", "application/json");
    	connection.setRequestProperty("Accept", "application/json");
    	connection.setDoOutput(true);
    	connection.setRequestProperty("master-key", master_key);
    	connection.setRequestProperty("private-key", private_key);
    	this.setRequestBody(connection, payload);
    	return this.getResponse(connection);
    }
    
    /**
	 * Set the body of the server request.
     * @param connection - An HttpURLConnection object.
     * @param payload - The request body to send.
	 * @return void.
     */
    private void setRequestBody(HttpURLConnection connection, String payload) {
    	try(OutputStream os = connection.getOutputStream()) {
    	    byte[] input = payload.getBytes("utf-8");
    	    os.write(input, 0, input.length);			
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Get the server response.
     * @param connection - An HttpURLConnection object.
     * @return A String object describing the server response.
     */
    private String getResponse(HttpURLConnection connection) {
    	StringBuilder response = new StringBuilder();
    	try(BufferedReader br = new BufferedReader(
		  new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
//		    System.out.println(response.toString());
    	} catch (IOException e) {
			e.printStackTrace();
    	}
		return response.toString();
    }
    
    /**
	 * Opens a server connection.
     * @param endpoint_url - A string representing the endpoint.
     * @return An HttpURLConnection object.
     * @throws Exception
     */
    private HttpURLConnection connect(String endpoint_url) throws Exception {
    	String base_url = "https://api.staging.payment.cosna-afrique.com/v1";
    	URL url = new URL(base_url.concat(endpoint_url));
    	return (HttpURLConnection) url.openConnection();
    }
    
}
