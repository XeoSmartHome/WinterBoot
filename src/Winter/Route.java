package Winter;

import java.util.function.Consumer;

public record Route(String path, String method, Consumer<Context> handler) {
}
