package metrique;

/*****************************************************/

import java.util.*;
import java.io.*;
import java.util.logging.*;
import java.util.Random;

/**
 * Classe Graphe permettant de manipuler des graphes. Représentation par listes
 * de successeurs
 **/
class Graphe {
    /** les couleurs utilisées lors des parcours de graphe **/
    public static final String BLUE = "blue";
    public static final String BLACK = "black";
    public static final String RED = "red";
    private static final Random RAND = new Random();

    /** ensemble de Node (ou NodeSet) **/
    private TreeSet<Object> treeSet;

    /** constructeur **/
    Graphe() {
        this.treeSet = new TreeSet<Object>();
    }

    /** accès à l'ensemble de Node **/
    public TreeSet<Object> getS() {
        return this.treeSet;
    }

    /** ajout d'un Node dans le graphe **/
    public boolean addNode(Node node) {
        return this.treeSet.add(node);
    }

    /** test de l'existence d'un Node dans le graphe **/
    public boolean containsNode(int i) {
        return this.treeSet.contains(new Node(i));
    }

    /** suppression d'un Node dans le graphe **/
    public boolean removeNode(Node node) {
        if (!this.treeSet.remove(node))
            return false;
        for (Iterator<?> I = node.succ().iterator(); I.hasNext();) {
            Arc arc = (Arc) I.next();
            Node node2 = arc.to();
            node2.removePred(arc);
        }
        for (Iterator<?> I = node.pred().iterator(); I.hasNext();) {
            Arc arc = (Arc) I.next();
            Node node2 = arc.from();
            node2.removeSucc(arc);
        }
        return true;
    }

    /** accès à un Node du graphe **/
    public Node getNode(int i) {
        if (this.containsNode(i))
            for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
                Node node = (Node) I.next();
                if (node.id() == i)
                    return node;
            }
        return null;
    }

    /** test de l'existence d'un Arc dans le graphe **/
    public boolean containsArc(int i, int j) {
        if (this.containsNode(i) && this.containsNode(j)) {
            Node from = this.getNode(i);
            Node to = this.getNode(j);
            return from.containsSucc(new Arc(from, to));
        }
        return false;
    }

    /** accès à un Arc du graphe **/
    public Arc getArc(int i, int j) {
        if (this.containsArc(i, j)) {
            Node from = this.getNode(i);
            for (Iterator<?> I = from.succ().iterator(); I.hasNext();) {
                Arc arc = (Arc) I.next();
                if (arc.from().id() == i && arc.to().id() == j)
                    return arc;
            }
        }
        return null;
    }

    /** ajout d'un Arc dans le graphe **/
    public boolean addArc(Arc arc) {
        int i = arc.from().id();
        int j = arc.to().id();
        if (this.containsNode(i) && this.containsNode(j) && !this.containsArc(i, j)) {
            arc.from().addSucc(arc);
            arc.to().addPred(arc);
            return true;
        }
        return false;
    }

    /** suppression d'un Arc dans le graphe **/
    public boolean removeArc(Arc arc) {
        int i = arc.from().id();
        int j = arc.to().id();
        if (this.containsNode(i) && this.containsNode(j) && this.containsArc(i, j)) {
            arc.from().removeSucc(arc);
            arc.to().removePred(arc);
            return true;
        }
        return false;
    }

    public int nbNodes() {
        return this.treeSet.size();
    }

    public int nbArcs() {
        int nb = 0;
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {

            Node node = (Node) I.next();
            nb += node.succ().size();
        }
        return nb;
    }

    /* colorier tous les noeuds et arcs d'une meme couleur */
    public void setColor(String color) {
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            node.setColor(color);
            for (Iterator<?> J = node.succ().iterator(); J.hasNext();) {
                Arc arc = (Arc) J.next();
                arc.setColor(color);
            }
        }
    }

    /** methode d'affichage **/
    public String toString() {
        String graph = "";
        String nodes = "Nodes = {";
        String arcs = "Arcs = {";
        graph += "G --> " + this.nbNodes() + "Nodes \n";
        graph += "       " + this.nbArcs() + "Arcs \n ";
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            nodes += " " + node;
            TreeSet<?> succ = (TreeSet<?>) node.succ();
            // Parcours de l'ensemble des successeurs
            for (Iterator<?> J = succ.iterator(); J.hasNext();) {
                Arc arc = (Arc) J.next();
                arcs += " " + arc;
            }

        }
        return graph + nodes + "}\n" + arcs + "}\n";

    }

    /** methode d'affichage grammaire dot **/
    public static void toDot(Graphe graph, String filename) throws IOException {

        // on initialise un logger afin d'afficher les exception d'ouverture et de
        // fermeture de stream
        Logger logger = Logger.getLogger("logger");

        // on utilise un string builder pour les nodes et les arcs
        StringBuilder nodesBld = new StringBuilder();
        StringBuilder arcsBld = new StringBuilder();

        // on utilise un try-with-ressources qui est disponible depuis Java 7 et qui
        // permet de s'assurer que les ressources seront correctement fermées
        try (FileOutputStream fich = new FileOutputStream(filename);
                DataOutputStream out = new DataOutputStream(fich);) {

            out.writeBytes("digraph G {\n");

            // parcours de l'ensemble de Node
            for (Iterator<?> I = graph.treeSet.iterator(); I.hasNext();) {
                Node node = (Node) I.next();
                nodesBld.append(" " + node.toDot());
                TreeSet<?> succ = (TreeSet<?>) node.succ();
                // parcours de l'ensemble des successeurs de N
                for (Iterator<?> J = succ.iterator(); J.hasNext();) {
                    Arc arc = (Arc) J.next();

                    String arcStr = arc.toString();
                    String label = "";
                    if (arc.label().length() != 0)
                        label = "label=" + arc.label() + ",";
                    String color = "color=" + arc.color();
                    arcsBld.append(" " + arcStr + " [" + label + color + "]\n");
                }
            }
            out.writeBytes(nodesBld.toString());
            out.writeBytes(arcsBld.toString());
            out.writeBytes("}");
        } catch (EOFException e) {
            logger.log(Level.SEVERE, "Erreur d'ouverture des streams: {0}", e);
        }
    }

    public static Graphe divGraph1(int nb, boolean visu) {
        Graphe graph = new Graphe();
        for (int i = 2; i <= nb; i++) {
            Node node = new Node(i);
            if (i % 2 == 0 && visu) {
                node.setColor(Graphe.RED);
            }
            graph.addNode(node);
        }
        // ajout des arcs
        for (int i = 2; i <= nb; i++)

            for (int j = 2; j <= nb; j++)

                if (i % j == 0) {
                    Node node1 = graph.getNode(i);
                    Node node2 = graph.getNode(j);
                    Arc arc = new Arc(node1, node2);
                    if (visu) {
                        int div = i / j;
                        String label = div + "";
                        arc.setLabel(label);
                    }
                    graph.addArc(arc);
                }

        return graph;
    }

    // ---------
    /* parcours en largeur */
    public void parcoursLargeur(boolean visu) {
        // couleur noire (inexplorée) pour tous les noeuds
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            node.setColor(Graphe.BLACK);
        }
        // parcours à partir d'une source inexplorée
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            if (node.color().equals(Graphe.BLACK))
                parcoursLargeur(node, visu);
        }
        // remet les sommets en noir
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            node.setColor(Graphe.BLACK);
        }
    }

    /* parcours en largeur à partir d'une source */
    private void parcoursLargeur(Node node, boolean visu) {
        node.setColor(Graphe.BLUE);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node);
        while (!list.isEmpty()) {
            Node tempNode1 = list.get(0);
            for (Iterator<?> I = tempNode1.succ().iterator(); I.hasNext();) {
                Arc arc = (Arc) I.next();
                Node tempNode2 = arc.to();
                if (tempNode2.color().equals(Graphe.BLACK) && !list.contains(tempNode2)) {
                    list.add(tempNode2);
                    if (visu)
                        arc.setColor(Graphe.BLUE);
                }
            }
            list.remove(tempNode1);
            tempNode1.setColor(Graphe.BLUE);
        } // fin du while
    }

    /* parcours en profondeur */
    public void parcoursProfondeur(boolean visu) {
        // couleur noire (inexplorée) pour tous les noeuds
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            node.setColor(Graphe.BLACK);
        }
        // parcours à partir d'une source inexplorée
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            if (node.color().equals(Graphe.BLACK))
                parcoursProfondeur(node, visu);
        }
    }

    /* parcours en profondeur récursif à partir d'une source */
    private void parcoursProfondeur(Node node, boolean visu) {
        node.setColor(Graphe.RED);
        for (Iterator<?> I = node.succ().iterator(); I.hasNext();) {
            Arc arc = (Arc) I.next();
            Node tempNode = arc.to();
            if (!tempNode.color().equals("red")) {
                if (visu)
                    arc.setColor("green");
                parcoursProfondeur(tempNode, visu);
            }
        }
    }

    /**********************************************************/
    /** méthode statique de génération d'un graphe aléatoire **/
    public static Graphe randomGraphe(int nb) {
        Graphe G = new Graphe();

        // ensemble S de Node
        generateRandomNodes(G, nb);

        // ensemble A d'Arcs
        generateArcs(G);

        return G;
    }

    static void generateArcs(Graphe G) {
        // ensemble A d'Arcs

        for (Iterator<?> I = G.getS().iterator(); I.hasNext();) {
            Node node1 = (Node) I.next();

            for (Iterator<?> J = G.getS().iterator(); J.hasNext();) {
                Node node2 = (Node) J.next();
                int choice = (10 * Graphe.RAND.nextInt());
                /*
                 * choice compris entre 0 et 10. si choice < 5: on n'ajoute pas l'arc (N1,N2) si
                 * choice > 5: on ajoute l'arc
                 */
                if (choice > 5 && node1 != node2)
                    G.addArc(new Arc(node1, node2));
            }
        }
    }

    static void generateRandomNodes(Graphe G, int nb) {
        // ensemble S de Node
        int i = nb;
        while (i > 0) {
            int id = (int) Math.rint(10 * nb * Math.random());
            if (G.addNode(new Node(id)))
                i--;
        }
    }

    private static Graphe createGraphe(int nb) {
        Graphe graphe = new Graphe();

        // ensemble S de Node
        int i = nb;
        while (i > 0) {
            int id = (10 * nb * Graphe.RAND.nextInt());
            if (graphe.addNode(new Node(id, "n" + id)))
                i--;
        }

        return graphe;
    }

    private static void temp(Graphe graphe) {
        for (Iterator<?> I = graphe.getS().iterator(); I.hasNext();) {
            Node node1 = (Node) I.next();

            for (Iterator<?> J = graphe.getS().iterator(); J.hasNext();) {
                Node node2 = (Node) J.next();
                if (node2.id() > node1.id()) {
                    int choice = (10 * Graphe.RAND.nextInt());
                    if (choice > 5) {
                        Arc arc = new Arc(node1, node2);
                        int il = arc.from().id();
                        int jl = arc.to().id();
                        if (graphe.containsNode(il) && graphe.containsNode(jl) && !graphe.containsArc(il, jl)) {
                            arc.from().addSucc(arc);
                            arc.to().addPred(arc);
                        }
                    }
                }
            }
        }
    }

    /** méthode statique de génération d'un DAG aléatoire **/
    public static Graphe randomDAG(int nb) {
        Graphe graphe = Graphe.createGraphe(nb);

        Graphe.temp(graphe);

        return graphe;
    }

    /************************/
    /** calcul des sources **/
    public TreeSet<Object> sources(boolean visu) {
        TreeSet<Object> sources = new TreeSet<Object>();
        for (Iterator<?> I = this.treeSet.iterator(); I.hasNext();) {
            Node node = (Node) I.next();
            if (node.pred().isEmpty()) {
                sources.add(node);
                if (visu)
                    node.setShape("box");
            }
        }
        return sources;
    }

    /** calcul d'un tri topologique **/
    public ArrayList<Node> triTopologique(boolean visu) {
        TreeSet<Object> sources = this.sources(visu);
        ArrayList<Node> choisis = new ArrayList<Node>();

        while (sources.isEmpty()) {
            // choix d'un Node dans sources
            int nb = (Graphe.RAND.nextInt() * sources.size());
            if (nb == 0)
                nb++;
            Node nodeX = null;
            for (Iterator<?> I = sources.iterator(); I.hasNext();) {
                if (nb == 1)
                    nodeX = (Node) I.next();
                else
                    I.next();
                nb--;
            }
            sources.remove(nodeX);
            choisis.add(nodeX);
            // maj des sources par parcours des succ de x

            if (nodeX != null) {
                for (Iterator<?> I = nodeX.succ().iterator(); I.hasNext();) {
                    Arc arcX = (Arc) I.next();
                    Node nodeY = arcX.to();
                    boolean ajoutY = true;
                    for (Iterator<?> J = nodeY.pred().iterator(); J.hasNext();) {
                        Arc arcY = (Arc) J.next();
                        Node nodeZ = arcY.from();

                        if (!choisis.contains(nodeZ))
                            ajoutY = false;

                    }
                    if (ajoutY) {
                        sources.add(nodeY);
                        if (visu)
                            arcX.setColor(Graphe.RED);
                    }
                } // fin du for
            }
        }
        return choisis;
    }

    // test de l'existence d'un cycle
    public boolean cycles(boolean visu) {
        ArrayList<Node> list = this.triTopologique(false);
        if (list.size() != this.nbNodes()) {
            if (visu) {
                for (Iterator<?> I = this.getS().iterator(); I.hasNext();) {
                    Node node = (Node) I.next();
                    if (!list.contains(node))
                        node.setColor("blue");
                }
            }
            return true;
        }
        return false;
    }

}// fin de Graph
