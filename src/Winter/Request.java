package Winter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Request {


    private final String path;
    private final String method;
    private final Map<String, String> query = new HashMap<>();

    private final BufferedReader bufferedReader;

    public Request(InputStream inputStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

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

    public String body() {
        int contentLength = 0;
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }

                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(" ")[1]);
                }
            }

            char[] body = new char[contentLength];
            bufferedReader.read(body, 0, contentLength);

            return new String(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
