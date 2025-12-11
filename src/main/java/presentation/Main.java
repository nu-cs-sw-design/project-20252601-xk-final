package presentation;

import domain.classdiagram.PlantUMLGenerator;
import domain.dependencycheck.CyclicDependencyChecker;
import domain.stylecheck.ClassStyleRevisionManager;
import domain.util.ASMToDomainNodeConverter;
import domain.util.DomainClassNode;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        ASMToDomainNodeConverter converter = new ASMToDomainNodeConverter();
        List<DomainClassNode> domainClassNodes = new ArrayList<>();

        for (String arg : args) {
            ClassReader classReader = new ClassReader(arg);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            DomainClassNode domainClassNode = converter.createDomainClassNode(classNode);
            domainClassNodes.add(domainClassNode);
        }

        System.out.println("Type \"style-check\" to show style revisions.");
        System.out.println("Type \"diagram\" to output a PlantUML diagram.");
        System.out.println("Type \"diagram-full\" to output a PlantUML diagram including external dependencies.");
        System.out.println("Type \"cycles-check\" to check for cyclic dependencies.");
        System.out.println("Type \"quit\" to quit.");

        String instruction = "";
        Scanner scanner = new Scanner(System.in);
        while (!instruction.equals("quit")) {

            System.out.println();
            instruction = scanner.next();

            switch (instruction) {
                case "style-check" -> {
                    ClassStyleRevisionManager classStyleRevisionManager = new ClassStyleRevisionManager(System.out);
                    classStyleRevisionManager.generateReport(domainClassNodes);
                }
                case "diagram" -> {
                    PlantUMLGenerator plantUMLGenerator = new PlantUMLGenerator(System.out, false);
                    plantUMLGenerator.generateClassDiagram(domainClassNodes);
                }
                case "diagram-full" -> {
                    PlantUMLGenerator plantUMLGenerator = new PlantUMLGenerator(System.out, true);
                    plantUMLGenerator.generateClassDiagram(domainClassNodes);
                }
                case "cycles-check" -> {
                    CyclicDependencyChecker cyclicDependencyChecker = new CyclicDependencyChecker(System.out);
                    cyclicDependencyChecker.generateReport(domainClassNodes);
                }
            }

        }

    }

}
