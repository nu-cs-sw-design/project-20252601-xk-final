package domain.dependencycheck;

import domain.util.DomainClassNode;
import domain.util.DomainTypeUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CyclicDependencyChecker {

    OutputStream output;

    public CyclicDependencyChecker(OutputStream output) {
        this.output = output;
    }

    public void generateReport(List<DomainClassNode> classNodes) throws IOException {

        StringBuilder result = new StringBuilder();

        Graph<DomainClassNode> graph = buildGraph(Set.copyOf(classNodes));
        List<String> cycles = checkGraph(graph);

        if (cycles.isEmpty()) {
            result.append("There are no cycles!\n");
        }
        else for (String cycle : cycles) {
            result.append(cycle);
            result.append("\n");
        }

        output.write(result.toString().getBytes(StandardCharsets.UTF_8));
    }

    // Graph search algorithm yay!
    private List<String> checkGraph(Graph<DomainClassNode> graph) {

        // Newly visited nodes
        List<DomainClassNode> visitedNodes = new ArrayList<>();
        // Already checked nodes (important for backtracking from dead ends)
        List<DomainClassNode> reVisitedNodes = new ArrayList<>();

        List<String> cycles = new ArrayList<>();

        DomainClassNode start = graph.nodes.iterator().next();
        DomainClassNode current = start;
        Set<DomainClassNode> startNeighbors = graph.getNeighbors(start);

        while (visitedNodes.size() < graph.nodes.size()) {

            startNeighbors.removeAll(reVisitedNodes);
            if (startNeighbors.isEmpty())
                break;

            if (!visitedNodes.contains(current)) {
                visitedNodes.add(current);
            }

            Set<DomainClassNode> neighbors = graph.getNeighbors(current);
            reVisitedNodes.forEach(neighbors::remove);
            neighbors.remove(current);

            if (neighbors.isEmpty()) {
                reVisitedNodes.add(current);
                current = visitedNodes.get(visitedNodes.indexOf(current) - 1);
                continue;
            }

            for (var neighbor : neighbors) {
                if (visitedNodes.contains(neighbor)) {
                    cycles.add("There is a cycle involving " + neighbor.getName() + " and " + current.getName());
                    reVisitedNodes.add(current);
                }
                else {
                    current = neighbor;
                    break;
                }
            }

        }

        return cycles;

    }

    private Graph<DomainClassNode> buildGraph(Set<DomainClassNode> classNodes) {

        Dictionary<String, DomainClassNode> classNames = new Hashtable<>();
        classNodes.forEach(classNode -> classNames.put(classNode.getName(), classNode));

        Graph<DomainClassNode> graph = new Graph<>();
        graph.nodes = classNodes;

        for (DomainClassNode classNode : classNodes) {

            // Extension
            DomainClassNode superClassNode = classNames.get(classNode.getSuperName());
            if (superClassNode != null) {
                graph.addEdge(classNode, superClassNode);
            }

            // Implementation
            for (var interfaceName : classNode.getInterfaces()) {
                DomainClassNode interfaceNode = classNames.get(interfaceName);
                if (interfaceNode != null) {
                    graph.addEdge(classNode, interfaceNode);
                }
            }

            // Containment
            for (var field : classNode.getFields()) {
                List<String> involvedTypes = DomainTypeUtil.splitIntoSignificantTypes(field.getType());
                for (var typeName : involvedTypes) {
                    DomainClassNode typeClassNode = classNames.get(typeName);
                    if (typeClassNode != null) {
                        if (typeClassNode != classNode)
                            graph.addEdge(classNode, typeClassNode);
                    }
                }
            }

            // Association
            for (var method : classNode.getMethods()) {

                Set<String> involvedTypes = new HashSet<>();
                involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(method.getReturnType()));
                for (var parameterType : method.getParameterTypes()) {
                    involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(parameterType));
                }
                for (var localVariableType : method.getLocalVariableTypes()) {
                    involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(localVariableType));
                }

                for (var typeName : involvedTypes) {
                    DomainClassNode typeClassNode = classNames.get(typeName);
                    if (typeClassNode != null) {
                        if (typeClassNode != classNode)
                            graph.addEdge(classNode, typeClassNode);
                    }
                }

            }

        }

        return graph;

    }

    private static class Graph<T> {

        Set<T> nodes = new HashSet<>();
        Set<List<T>> edges = new HashSet<>();

        void addEdge(T node1, T node2) {
            edges.add(List.of(node1, node2));
        }

        Set<T> getNeighbors(T node1) {
            Set<T> results = new HashSet<>();
            for (var node2 : nodes) {
                if (edges.contains(List.of(node1, node2))) {
                    results.add(node2);
                }
            }
            return results;
        }

    }

}
