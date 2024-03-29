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

    private final static int PORT1 = 5001;
    private final static int PORT2 = 5000;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private PrintWriter out;

    
    
    // public void connectToServer() throws UnknownHostException, IOException {
    //     String host = InetAddress.getLocalHost().getHostAddress();
    //     int[] ports =  { PORT1, PORT2};

    //     for (int port : ports) {
    //         try {

    //             // Connect with the server
    //             SocketAddress socketAddress = new InetSocketAddress(host, port);
    //             socket = new Socket();
    //             socket.connect(socketAddress, 1000);

    //             socket.setSoTimeout(1000); // Set the timeout for socket operations


    //             outputStream = socket.getOutputStream();
    //             out = new PrintWriter(outputStream, true);
    //             inputStream = socket.getInputStream();

    //             return;

    //             // // Send request to check the conection with the server
    //             // JSONObject jsonObject = new JSONObject();
    //             // jsonObject.put("service", "connecting");

    //             // // Send the encrypted request and receive the response
    //             // JSONObject responseJson = sendEncryptedRequest(jsonObject);

    //             // if (responseJson != null) {
    //             //     char response = ((String) responseJson.get("result")).charAt(0);
    //             //     if (response == '1') {
    //             //         System.out.println("Connected to server: " + host + ":" + port);
    //             //         return; // Connection successful, exit the loop
    //             //     } else {
    //             //         System.out.println("Failed to connect to server: " + host + ":" + port);
    //             //     }
    //             // } else {
    //             //     System.out.println("Failed to connect to server: " + host + ":" + port);
    //             // }
    //         } catch (Exception e) {
    //             // TODO: handle exception
    //             System.out.println("Failed to connect to server: " + host + ":" + port);
    //         }
    //     }
    // }

    public void connectToServer() throws UnknownHostException, IOException {
        String host = InetAddress.getLocalHost().getHostAddress();
        int[] ports =  { PORT1, PORT2};

        for (int port : ports) {
            try {

                // Connect with the server
                socket = new Socket(host, port);


                outputStream = socket.getOutputStream();
                out = new PrintWriter(outputStream, true);
                inputStream = socket.getInputStream();

                // System.out.println("Connected to server: " + host + ":" + port);
                // return; // Connection successful, exit the loop
                
                // Send request to check the conection with the server
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service", "connecting");

                // Send the encrypted request and receive the response
                JSONObject responseJson = sendEncryptedRequest(jsonObject);

                if (responseJson != null) {
                    char response = ((String) responseJson.get("result")).charAt(0);
                    if (response == '1') {
                        socket = new Socket(host, port);
                        outputStream = socket.getOutputStream();
                        out = new PrintWriter(outputStream, true);
                        inputStream = socket.getInputStream();
                        System.out.println("Connected to server: " + host + ":" + port);
                        return; // Connection successful, exit the loop
                    } else {
                        System.out.println("Failed to get response");
                    }
                } else {
                    System.out.println("Failed to connect to server: " + host + ":" + port);
                }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Failed to connect to container");
            }
        }
    }

    public JSONObject sendEncryptedRequest(JSONObject request) throws IOException, ParseException {
        // Parse the decrypted JSON string
        JSONParser parser = new JSONParser();
        
        try {
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
            return (JSONObject) parser.parse(decryptedJson);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Request fails");
        }
        
        return (JSONObject) parser.parse("{}");
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

    public JSONObject getChats(String username) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("service", "groups");
        jsonObject.put("username", username);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        return responseJson;
    }

    public JSONObject getRooms(String username) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("service", "groupsNo");
        jsonObject.put("username", username);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        return responseJson;
    }

    public JSONObject getUsers(String username, String groupName, boolean isAdmin) throws IOException, ParseException {
        connectToServer();

        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();

        if (isAdmin) {
            jsonObject.put("service", "usersAdmin");
        } else {
            jsonObject.put("service", "users");
        }
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupName);

        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        return responseJson;
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

    public char addUser(String username, String groupName, String userToAdd) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "addUser");
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupName);
        jsonObject.put("add", userToAdd);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

    public char addReq(String username, String groupName, String userToAdd) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "addReq");
        jsonObject.put("username", username);
        jsonObject.put("groupname", groupName);
        jsonObject.put("add", userToAdd);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

    public char deleteUser(String groupName, String userToDelete) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "deleteUser");
        jsonObject.put("groupname", groupName);
        jsonObject.put("deleteUser", userToDelete);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

    public char deleteReq(String groupName, String userToDelete) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "deleteReq");
        jsonObject.put("groupname", groupName);
        jsonObject.put("deleteUser", userToDelete);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

    public char deleteGroup(String groupName) throws IOException, ParseException {
        connectToServer();
    
        // Create a JSONObject instance
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service", "deleteChat");
        jsonObject.put("groupname", groupName);
    
        // Send the encrypted request and receive the response
        JSONObject responseJson = sendEncryptedRequest(jsonObject);

        char response = ((String) responseJson.get("result")).charAt(0);
    
        return response;
    }

}

