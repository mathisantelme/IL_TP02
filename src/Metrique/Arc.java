package metrique;
/*****************************************************/
import java.util.*;
public class Arc implements Comparable {
    /** Node origine de l'Arc **/
    private Node from;
    /** Node origine de l'Arc **/
    private Node to;
    /** Arc valué **/
    private double value;
    /** attributs dot **/
    private String color;
    private String label;

    /** constructeurs **/
    Arc (Node from, Node to) {
	this.from = from;
	this.to = to;
	this.color = "black";
	this.label = "";
	this.value = -1;
    }
    /** accesseur du Node origine **/
    public Node from (){
	return this.from;
    }
    /** accesseur du Node extremite **/
    public Node to (){
	return this.to;
    }
    // méthodes d'accès aux attributs
    public double value (){
	return this.value;
    }
    public void setValue (double v){
	this.value = v;
	this.label=""+v;
    }
    /** méthodes d'accès a un attribut **/
    public String color (){
	return this.color;
    }
    /** méthodes d'accès a un attribut **/
    public void setColor (String c){
	this.color = c; 
    }
    /** méthodes d'accès a un attribut **/
    public String label (){
	return this.label;
    }
    /** méthodes d'accès a un attribut **/
    public void setLabel (String l){
	this.label = l;
    }
    /** methode de comparaison necessaire pour implementer Comparable
	Permet de rechercher un Arc dans un TreeSet **/
    public int compareTo (Object obj)  {  
    	Arc arc = (Arc) obj ;  
		if (this.from.id() == arc.from().id() && this.to.id() == arc.to().id())
		    return 0;
		if (this.from.id() < arc.from().id() || 
		    this.from.id() == arc.from().id() && this.to.id() < arc.to().id())
		    return -1;    
		return 1;
    }
    
    @Override
    /** surcharge de la méthode equals */
    public boolean equals (Object obj) {
    	if (obj == null) return false;
    	
    	if (this.getClass() != obj.getClass()) return false;
    	
    	Arc arc = (Arc) obj ;
    	try {
    		return (this.from.id() == arc.from().id() && this.to.id() == arc.to().id());
    	} catch (NullPointerException e) {
    		return false;
    	}
    }
    
    @Override
    /**
     * surcharge de la méthode hasCode
     * */
    public int hashCode() {
	    return super.hashCode();
	}
    
    // methode d'affichage
    public String toString () { 
	return  this.from.id()+"->"+this.to.id();
    }
    /** methode d'affichage grammaire dot **/
    public String toDot () {
		String arc = this.toString();
		// gestion des attributs dot
		return arc+" ["+ ((this.label().length()!=0) ? "label="+this.label()+"," : "") + "color="+this.color() +"]\n";	
    }
}// fin d'Arc
