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

                String line = null;
                while ((line = isReader.readLine()) != null)
                {
                    System.out.println("Inut : "+line);

                    if (line.trim().isEmpty()) break;
                }

                ouWriter.print("HTTP/1.1 200\r\nContent-Type: application/json\r\nContent-Length: 29\r\n\r\n{\"Message\":\"Hello from oak!\"}\r\n");
                

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
