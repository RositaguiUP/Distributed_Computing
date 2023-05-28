package Talkie.Functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {
    private final static int KEY = 8;

    private final static int AUTHPORT = 5001;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private PrintWriter out;

    public void connectToServer() throws UnknownHostException, IOException {
        String host = InetAddress.getLocalHost().getHostAddress();

        // Connect with the server
        socket = new Socket(host, AUTHPORT);
        outputStream = socket.getOutputStream();
        out = new PrintWriter(outputStream, true);
        inputStream = socket.getInputStream();
    }

    public JSONObject sendEncryptedRequest(JSONObject request) throws IOException, ParseException {
        // Convert the JSONObject to a JSON string
        String jsonString = request.toJSONString();

        // Encrypt JSON string
        String encryptedJson = Encrypt.caesarEncrypt(KEY, jsonString);

        // Send JSON string to server
        out.println(encryptedJson);
        out.flush();

        // Read the encrypted response from the server
        byte[] data = new byte[1024];
        int bytesRead = inputStream.read(data);
        String encryptedResp = new String(data, 0, bytesRead);

        // Decrypt the JSON string
        String decryptedJson = Encrypt.caesarDecrypt(KEY, encryptedResp);

        // Parse the decrypted JSON string
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(decryptedJson);
    }

    public char login(String username, String password) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "auth");
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);

        return response;
    }

    public char createGroup(String username, String groupName) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "group");
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupName);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

    public JSONObject getChat(String username, String groupname) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("service", "chat");
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupname);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        return responseJson;
    }

    public char sendMessage(String username, String groupname, String message) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "msg");
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupname);
        jsonObject.put("message", message);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);

        return response;
    }
}

