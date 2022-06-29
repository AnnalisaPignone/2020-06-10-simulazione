package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulator {

	//dati in ingresso
	private Graph <Actor, DefaultWeightedEdge> grafo; 
	private int nGiorni; 
	
	//dati in uscita 
	private int giorniPausa; 
	
	//Modello del mondo 
	private List <AttoriIntervistati> attoriIntervistati; 
	private int giornoCorrente; 
	private int numeroIntervistati; 
	List<String> genere= new ArrayList <String>(); 
	
	//coda degli eventi
	PriorityQueue <Event> queue; 
	
	//costruttore
	public Simulator (Graph <Actor, DefaultWeightedEdge> grafo, List <Actor> vertici) {
		this.grafo=grafo; 
		this.attoriIntervistati= new ArrayList <AttoriIntervistati>(); 
		for (Actor a: vertici) {
			this.attoriIntervistati.add(new AttoriIntervistati(a)); 
		}
	}
	
	public void init(int nGiorni) {
		this.nGiorni=nGiorni; 
		this.queue= new PriorityQueue <Event>(); 
		
		//inizializzo gli output
		this.giorniPausa=0; 
		
		//inizializzo Modello mondo 
		this.giornoCorrente=1; 
		this.numeroIntervistati=0; 
		
		//caricamento iniziale della coda 
		int n= (int)((Math.random())*(attoriIntervistati.size())); 
		attoriIntervistati.get(n).setIntervistato(true);
		AttoriIntervistati aTemp= attoriIntervistati.get(n); 
		genere.add(aTemp.getAttore().getGender()); 
		queue.add(new Event(EventType.INTERVISTA, giornoCorrente, aTemp, aTemp.getAttore().getGender())); 	
		
	}
	
	public void run () {
		while (!this.queue.isEmpty()) {
			Event e=this.queue.poll(); 
			processEvent(e); 
		}
	}

	private void processEvent(Event e) {
		
		EventType type=e.getType(); 
		int giorno=e.getGiorno(); 
		AttoriIntervistati attore= e.getAttoreIntervistato(); 
		
		if(giorno<=nGiorni) {
		switch (type) {
		case INTERVISTA: 
			this.numeroIntervistati++; 
			if(numeroIntervistati!= 1) {
				//se gli utlimi due generi sono uguali 
				if (genere.get(genere.size()-1).compareTo(genere.get(genere.size()-2))==0) {
					double probabilita= Math.random();
					if (probabilita<0.9) {
						giorniPausa++; 
						queue.add(new Event(EventType.PAUSA, giornoCorrente+1, null, null));
						genere.add("pausa"); 
					}
					else aggiungiIntervistaInCoda(type, giorno, attore); 
				//se gli ultimi due generi sono diversi
				}else { 
					double probabilita= Math.random(); 
					if ((probabilita<0.6) ) { //|| (getVicini(attore.getAttore(), attoriIntervistati)==null)
						aggiungiIntervistaInCoda(type, giorno, attore); 
						}
					else { 
						if (getVicini(attore.getAttore(), attoriIntervistati).size()>1) {
						//se ho più vicini 
						int casuale= (int)(Math.random()*	getVicini(attore.getAttore(), attoriIntervistati).size()); 
						AttoriIntervistati aTemp=getVicini(attore.getAttore(), attoriIntervistati).get(casuale);
						genere.add(aTemp.getAttore().getGender()); 
						queue.add(new Event(EventType.INTERVISTA, giornoCorrente+1, aTemp, aTemp.getAttore().getGender()));

						}
						else if (getVicini(attore.getAttore(), attoriIntervistati).size()==1) {
							AttoriIntervistati aTemp=getVicini(attore.getAttore(), attoriIntervistati).get(0);
							genere.add(aTemp.getAttore().getGender()); 
							queue.add(new Event(EventType.INTERVISTA, giornoCorrente+1, aTemp, aTemp.getAttore().getGender()));
						}

						
					}
			}

			}else {
				 aggiungiIntervistaInCoda(type, giorno, attore); 
				}
			break; 
			
		case PAUSA: 
			giorniPausa++; 
			 aggiungiIntervistaInCoda(type, giorno, attore); 
			break; 
		}
		
	}
	}
	
	public void aggiungiIntervistaInCoda(EventType type, int giorno, AttoriIntervistati attore ) {
		int n= (int)((Math.random())*(attoriIntervistati.size())); 
		while(attoriIntervistati.get(n).isIntervistato()==true) {
			 n= (int)((Math.random())*(attoriIntervistati.size())); 
		}
		attoriIntervistati.get(n).setIntervistato(true);
		AttoriIntervistati aTemp= attoriIntervistati.get(n); 
		genere.add(aTemp.getAttore().getGender()); 
		queue.add(new Event(EventType.INTERVISTA, giornoCorrente+1, aTemp, aTemp.getAttore().getGender()));
		}
	
	
	public List <AttoriIntervistati> getVicini(Actor at, List <AttoriIntervistati> intervistati){
		List <Actor> attoriVicini= new ArrayList <Actor>(); 
		List <AttoriIntervistati> attoriVicini2= new ArrayList <AttoriIntervistati>();
		attoriVicini= Graphs.neighborListOf(this.grafo, at); 
		double massimo=0; 
		for (Actor a: attoriVicini) {
			DefaultWeightedEdge edge= this.grafo.getEdge(at, a); 
			if (this.grafo.getEdgeWeight(edge)>massimo) {
				massimo=this.grafo.getEdgeWeight(edge); 
				
			}
		
		}
		
		for (Actor a: attoriVicini) {
			DefaultWeightedEdge edge= this.grafo.getEdge(at, a); 
			if (this.grafo.getEdgeWeight(edge)==massimo) {
				attoriVicini.add(a); 
				
			}
		
		}
		
		for (AttoriIntervistati ai: intervistati) {
			for (Actor a: attoriVicini) {
				if(ai.getAttore().equals(a) && ai.isIntervistato()==false) {
					attoriVicini2.add(ai); 
				}
			}
		}
		return attoriVicini2; 
		
		
		
	}

	public int getGiorniPausa() {
		return giorniPausa;
	}

	public List<AttoriIntervistati> getAttoriIntervistati() {
		return attoriIntervistati;
	}
	
	
	
}


