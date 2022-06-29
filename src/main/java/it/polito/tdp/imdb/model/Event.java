package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event> {

	public enum EventType{
		INTERVISTA, 
		PAUSA
	}
	
	private EventType type; 
	private int giorno; 
	private AttoriIntervistati attoreIntervistato;
	private String genere; 
	
	public Event(EventType type, int giorno, AttoriIntervistati attoreIntervistato, String genere) {
		super();
		this.type = type;
		this.giorno = giorno;
		this.attoreIntervistato = attoreIntervistato;
		this.genere=genere; 
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public AttoriIntervistati getAttoreIntervistato() {
		return attoreIntervistato;
	}

	public void setAttoreIntervistato(AttoriIntervistati attoreIntervistato) {
		this.attoreIntervistato = attoreIntervistato;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno-o.getGiorno();
	}
	
	
	
	
	
	
	
	
}
