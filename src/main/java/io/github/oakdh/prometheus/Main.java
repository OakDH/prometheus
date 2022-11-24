package io.github.oakdh.prometheus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{
    
    public static String outputMessage(String message)
    {
        int messageLength = message.length();
        
        return String.format("HTTP/1.1 200\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n%s\r\n", messageLength, message);
    }

    public static void main(String[] args)
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
                PrintWriter ouWriter = new PrintWriter(outStream, true);
                
                String message = "";
                String line = null;
                while ((line = isReader.readLine()) != null)
                {
                    message += line + "\n";

                    if (line.trim().isEmpty()) break;
                }

                
                
                ouWriter.print(outputMessage("Hello from oak!\n"));
                ouWriter.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
