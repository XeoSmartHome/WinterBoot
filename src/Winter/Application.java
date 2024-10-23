package Winter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Application {
    private final ArrayList<Route> routes = new ArrayList<Route>();

    public void Get(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "GET", handler));
    }

    public void Post(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "POST", handler));
    }

    public void Put(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "PUT", handler));
    }

    public void Patch(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "PATCH", handler));
    }

    public void Delete(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "DELETE", handler));
    }

    public void Options(String path, Consumer<Context> handler) {
        routes.add(new Route(path, "OPTIONS", handler));
    }

    public IOException Listen(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started on http://localhost:" + port);

            while (true) {
                Socket client = server.accept();
                new Thread(() -> {
                    try {
                        Request request;

                        try {
                            request = new Request(client.getInputStream());
                        } catch (Exception e) {
                            return;
                        }

                        Response response = new Response(client.getOutputStream());
                        Context context = new Context(request, response);

                        Route route = routes.stream().filter(r -> r.path().equals(request.path()) && r.method().equals(request.method())).findFirst().orElse(null);

                        if (route != null) {
                            try {
                                route.handler().accept(context);
                            } catch (Exception e) {
                                response.status(500).send("Internal Server Error");
                            }
                        } else {
                            response.status(404).send("Not Found");
                        }
                        client.close();
                    } catch (IOException e) {
                        System.out.printf("Error in handling request: %s\n", e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            return e;
        }
    }
}
