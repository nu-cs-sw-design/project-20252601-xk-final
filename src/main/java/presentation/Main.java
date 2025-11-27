package presentation;

import domain.stylecheck.NamingConventionStyleReviser;
import domain.stylecheck.StyleRevisionClassVisitor;
import org.objectweb.asm.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ClassReader classReader = new ClassReader("presentation.Main");

        StyleRevisionClassVisitor rv = new StyleRevisionClassVisitor(System.out);
        rv.addStyleReviser(new NamingConventionStyleReviser());

        classReader.accept(rv, 0);

    }

}
