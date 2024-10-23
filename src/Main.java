import Winter.Application;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();

        app.Get("/", context -> {
            context.response.status(200).send("Hello, World!");
        });

        app.Get("/add", context -> {
            int a = Integer.parseInt(context.request.query("a"));
            int b = Integer.parseInt(context.request.query("b"));
            context.response.status(200).send("Result: " + (a + b));
        });

        app.Post("/text", context -> {
            String body = context.request.body();

            context.response.status(200).send("Body: " + body);
        });

        IOException error = app.Listen(3000);

        if (error != null) {
            System.out.println("Error in starting server: " + error.getMessage());
        }
    }
}