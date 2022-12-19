package io.github.oakdh.prometheus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

public class HTTPHandler {
    
    private HTTPHandler(){} // Make it so that the class isnt instantiatable

    public static String CLIENT_ID_NOT_PROVIDED_FAILURE = createFailurePacket(1, "Client ID wasn't provided.");
    public static String CLIENT_ACCOUNT_NOT_FOUND_FAILURE = createFailurePacket(2, "Client account with given ID not found.");
    public static String COMMAND_NOT_FOUND_FAILURE = createFailurePacket(3, "Could not find command.");

    public static int PORT = 1025;

    public static void initialize()
    {
        try
        {
            ServerSocket ss = new ServerSocket(1025);

            while (true)
            {
                Socket con = ss.accept();

                InputStream inputStream = con.getInputStream();
                BufferedReader isReader = new BufferedReader(new InputStreamReader(inputStream));

                OutputStream outStream = con.getOutputStream();
                PrintWriter outWriter = new PrintWriter(outStream, true);
                
                String message = "";
                String line = null;
                while ((line = isReader.readLine()) != null)
                {
                    message += line + "\n";

                    if (line.trim().isEmpty()) break;
                }

                if (handleMessage(new HTTPRequest(message), outWriter)) break;
            }

            ss.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This function gets called every time the server receives a http packet.
     * 
     * @param message The payload of the packet
     * @param outWriter The output writer thats connected to the socket
     * @return Returns whether the server should stop checking for connections
     */
    public static boolean handleMessage(HTTPRequest request, PrintWriter outWriter)
    {
        boolean should_exit = false;

        switch (request.target_path[0])
        {
        case "hello_world":
            outWriter.print(createPacket(new JSONObject().put("status", 0).put("message", "Hello from Oak!").toString()));

            break;
        case "exit":
            outWriter.print(createPacket(new JSONObject().put("status", 0).put("message", "Stopping server...").toString()));
            should_exit = true;

            break;
        case "client_login":
            if (request.target_path.length < 2)
            {
                outWriter.print(createPacket(CLIENT_ID_NOT_PROVIDED_FAILURE));
                break;
            }

            JSONObject json = DatabaseHandler.getUserLogin(Long.parseLong(request.target_path[1]));

            if (json.getLong("id") == -1)
            {
                outWriter.print(createPacket(CLIENT_ACCOUNT_NOT_FOUND_FAILURE));
                break;
            }

            json.put("status", 0);

            outWriter.print(createPacket(json.toString()));

            break;
        default:
            outWriter.print(createPacket(COMMAND_NOT_FOUND_FAILURE));

            break;
        }

        outWriter.flush();

        return should_exit;
    }

    public static String createPacket( String payload)
    {
        int messageLength = payload.length();
        
        return String.format("HTTP/1.1 200\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n%s\r\n",
            messageLength, payload);
    }

    private static String createFailurePacket(int errorCode, String message)
    {
        return new JSONObject().put("status", errorCode).put("message", message).toString();
    }
}
