package domain.stylecheck;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class StyleRevisionPrinter {

    OutputStream output;

    StyleRevisionPrinter(OutputStream output) {
        this.output = output;
    }

    final void print(String data) throws IOException {
        output.write(data.getBytes(StandardCharsets.UTF_8));
    }

    abstract void print(StyleRevision revision) throws IOException;

}
