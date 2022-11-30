package io.github.oakdh.prometheus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPHandler {
    
    private HTTPHandler(){} // Make it so that the class isnt instantiatable

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

                if (handleMessage(message, outWriter)) break;
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
    public static boolean handleMessage(String message, PrintWriter outWriter)
    {
        outWriter.print(createPacket("Hello from oak!\n"));
        outWriter.flush();

        return false;
    }

    public static String createPacket(String payload)
    {
        int messageLength = payload.length();
        
        return String.format("HTTP/1.1 200\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n%s\r\n", messageLength, payload);
    }
}
