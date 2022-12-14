package io.github.oakdh.prometheus;

import java.util.ArrayList;
import java.util.List;

public class HTTPRequest
{
    String method;
    String[] target_path;
    String http_version;

    List<String> headers = new ArrayList<>();

    HTTPRequest(String req)
    {
        String[] lines = req.split("\n");
        
        String start_line = lines[0];
        String[] start_line_tokens = start_line.split(" ");

        method = start_line_tokens[0];
        http_version = start_line_tokens[2];

        target_path = start_line_tokens[1].substring(1).split("/");

        for (int i = 1; i < lines.length; i++)
        {
            headers.add(lines[i]);
        }
    }    
}
