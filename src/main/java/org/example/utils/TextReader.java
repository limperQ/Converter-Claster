package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextReader {
    public String readTestInput(String fileName)
    {
        final StringBuilder sb = new StringBuilder();
        try (final InputStream fileStream = getClass().getResourceAsStream(fileName))
        {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(fileStream, "UTF8")))
            {
                String strLine;
                while ((strLine = br.readLine()) != null)
                {
                    sb.append(strLine)
                            .append("\r\n");
                }
            }
            sb.delete(sb.length() - 2, sb.length()); // xD )) we are in junit,
            // no matter
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load test resources", e);
        }

        return sb.toString();
    }
}
