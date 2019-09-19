package metrique;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class TPGraphe {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger("logger");
        // graphe des diviseurs
        Graphe graph1 = Graphe.randomDAG(10);
        String graphString = graph1.toString();
        logger.log(Level.INFO, "{0}", graphString);
        Graphe.toDot(graph1, "prof.dot");
        graph1.parcoursProfondeur(true);
        Graphe.toDot(graph1, "profDiv.dot");
        graph1.setColor("black");
        ArrayList<Node> list = graph1.triTopologique(true);
        String listStr = list.toString();
        logger.log(Level.INFO, "{0}", listStr);
        Graphe.toDot(graph1, "profDiv.dot");
    }
}