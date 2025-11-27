package domain.stylecheck;

import java.io.IOException;
import java.io.OutputStream;

public abstract class StyleRevisionPrinter {

    OutputStream output;

    StyleRevisionPrinter(OutputStream output) {
        this.output = output;
    }

    abstract void print(StyleRevision revision) throws IOException;

}
