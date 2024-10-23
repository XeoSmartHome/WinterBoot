package Winter;

import java.io.OutputStream;

public class Response {
    private final OutputStream outputStream;
    private int status;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response status(int status) {
        this.status = status;
        return this;
    }

    public int status() {
        return status;
    }

    public void send(String body) {
        try {
            outputStream.write(("HTTP/1.1 " + status + " OK\r\n").getBytes());
            outputStream.write("Content-Type: text/plain\r\n".getBytes());
            outputStream.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            outputStream.write("\r\n".getBytes());
            outputStream.write(body.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
