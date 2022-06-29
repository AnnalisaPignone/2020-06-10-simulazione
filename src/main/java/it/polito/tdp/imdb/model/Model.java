package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao; 
	private Graph<Actor,DefaultWeightedEdge> grafo;  
	private Map<Integer,Actor> idMap; 
	private List<Actor> vertici; 
	private List<Arco> archi; 
	private List<Arco> archiTemp; 
	int giorniPausa; 
	List <AttoriIntervistati> attoriInt= new ArrayList <>(); 
	

	
	public Model() {
		this.dao= new ImdbDAO(); 
		this.idMap= new HashMap<Integer,Actor>(); 
		dao.loadAllActors(idMap);
		vertici=new ArrayList<>(); 
		archi=new ArrayList<>(); 
		archiTemp=new ArrayList<>(); 

	}
	
	public void creaGrafo(String genere) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getActorsByGenre(this.idMap, genere));
		this.vertici= this.dao.getActorsByGenre(this.idMap, genere); 
		Collections.sort(vertici);
		//aggiungo gli archi 
		this.archiTemp=new ArrayList(this.dao.getArchi(this.idMap, genere)); 
		this.archi= pulisciArchi(this.archiTemp); 
		for (Arco a: archi) {
			if (this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2()) ) {
				DefaultWeightedEdge edge= this.grafo.getEdge(a.getA1(), a.getA2()); 
				if (edge==null) {
					Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), 1); 
					a.setPeso(1);
				}
					else {
						double pesoVecchio = this.grafo.getEdgeWeight(edge);
						double pesoNuovo = pesoVecchio + 1;
						this.grafo.setEdgeWeight(edge, pesoNuovo);
						a.setPeso(pesoNuovo);
					}
		
	}
	}
	}
	
	private List <Arco> pulisciArchi(List<Arco> archi) {
		for (int i=0; i<archi.size(); i++) {
			for (int j=0; j<archi.size();j++) {
				if((archi.get(i).getA1().equals(archi.get(j).getA2()))  && (archi.get(i).getA2().equals(archi.get(j).getA1()))) {
					archi.remove(j); 
				}
			}	
	}
		return archi; 
	}
	
	public Set<Actor> getComponenteConnessa2(Actor attore){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector <Actor, DefaultWeightedEdge>(this.grafo);
		Set<Actor> visitati= new HashSet<>(); 
		visitati= ci.connectedSetOf(attore);
		return visitati; 
	}
		
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public List<Actor> getVertici(){
		return this.vertici; 
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List <String> getAllGenres(){
		return this.dao.getAllGenres(); 
	}
	
	public void Simulazione(int ngiorni) {
		Simulator sim= new Simulator(this.grafo, this.vertici); 
		sim.init(ngiorni);
		sim.run(); 
		giorniPausa= sim.getGiorniPausa(); 
		attoriInt=sim.getAttoriIntervistati(); 
	}

	public int getGiorniPausa() {
		return giorniPausa;
	}

	public List<AttoriIntervistati> getAttoriInt() {
		return attoriInt;
	}
	
	
	
	
}
