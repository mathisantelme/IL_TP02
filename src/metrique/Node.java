package metrique;

/*****************************************************/
import java.util.*;

/** Classe pour manipuler un noeud d'un graphe **/
public class Node implements Comparable<Object> {
    /** identifiant du Node **/
    private int id;
    /** listes de successeurs et de prédecesseurs **/
    private TreeSet<Object> succ;
    private TreeSet<Object> pred;
    /** attributs dot **/
    private String label;
    private String color;
    private String shape;
    /** permet de compter le nb d'appels à compareTo **/
    public static final int NBCOMPARETO = 0;

    /** constructeur **/
    Node(int i) {
        this.id = i;
        this.succ = new TreeSet<Object>();
        this.pred = new TreeSet<Object>();
        this.label = Integer.toString(i);
        this.color = "black";
        this.shape = "ellipse";
    }

    /** constructeur **/
    Node(int i, String label) {
        this.id = i;
        this.succ = new TreeSet<Object>();
        this.pred = new TreeSet<Object>();
        this.label = label;
        this.color = "black";
        this.shape = "ellipse";
    }

    /** accès à l'identifiant **/
    public int id() {
        return this.id;
    }

    /** accès à l'ensemble des successeurs **/
    public SortedSet<Object> succ() {
        return this.succ;
    }

    /** accès à l'ensemble des predecesseurs **/
    public SortedSet<Object> pred() {
        return this.pred;
    }

    /** méthodes d'accès à un attribut **/
    public String color() {
        return this.color;
    }

    /** méthodes d'accès à un attribut **/
    public void setColor(String c) {
        this.color = c; // possibilité de tester la validité de c
    }

    /** méthodes d'accès à un attribut **/
    public String shape() {
        return this.shape;
    }

    /** méthodes d'accès à un attribut **/
    public void setShape(String s) {
        this.shape = s; // possibilité de tester la validité de c
    }

    /** méthodes d'accès à un attribut **/
    public String label() {
        return this.label;
    }

    /** méthodes d'accès à un attribut **/
    public void setLabel(String l) {
        this.label = l;
    }

    /** ajout d'un successeur **/
    public boolean addSucc(Arc arc) {
        return this.succ.add(arc);
    }

    /** test de l'existence d'un successeur **/
    public boolean containsSucc(Arc arc) {
        return this.succ.contains(arc);
    }

    /** suppression d'un successeur **/
    public boolean removeSucc(Arc arc) {
        return this.succ.remove(arc);
    }

    /** ajout d'un predecesseur **/
    public boolean addPred(Arc arc) {
        return this.pred.add(arc);
    }

    /** test de l'existence d'un predecesseur **/
    public boolean containsPred(Arc arc) {
        return this.pred.contains(arc);
    }

    /** suppression d'un predecesseur **/
    public boolean removePred(Arc arc) {
        return this.pred.remove(arc);
    }

    /**
     * methode de comparaison necessaire pour implementer Comparable permet de
     * rechercher un Node dans un TreeSet
     **/
    public int compareTo(Object obj) {

        Node node = (Node) obj; // C'est Node qui est entre
        if (this.id > node.id())
            return 1;
        if (this.id < node.id())
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Node node = (Node) obj;
        return this.id() == node.id();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** methode d'affichage **/
    public String toString() {
        return this.id + " ";
    }

    /** methode d'affichage grammaire dot **/
    public String toDot() {
        return this.id + " [label=" + this.label + ",color=" + this.color + ",shape=" + this.shape + "]\n";
    }

}// fin de Node
