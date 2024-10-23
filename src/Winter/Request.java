package Winter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String path;
    private final String method;
    private final Map<String, String> query = new HashMap<>();

    public Request(InputStream inputStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            String requestLine = bufferedReader.readLine();

            if (requestLine == null) {
                throw new RuntimeException("Request line is null");
            }

            String[] parts = requestLine.split(" ");
            method = parts[0];
            String fullPath = parts[1];

            if (fullPath.contains("?")) {
                String[] pathParts = fullPath.split("\\?");
                path = pathParts[0];
                String queryString = pathParts[1];

                String[] queryParts = queryString.split("&");

                for (String q : queryParts) {
                    String[] queryParts2 = q.split("=");
                    query.put(queryParts2[0], queryParts2[1]);
                }
            } else {
                path = fullPath;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String path() {
        return path;
    }

    public String method() {
        return method;
    }

    public String query(String a) {
        return query.get(a);
    }
}
