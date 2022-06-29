package it.polito.tdp.imdb.model;

public class AttoriIntervistati {

	Actor attore; 
	boolean intervistato;
	
	public AttoriIntervistati(Actor attore) {
		super();
		this.attore = attore;
		this.intervistato=false; 
	}

	public Actor getAttore() {
		return attore;
	}

	public void setAttore(Actor attore) {
		this.attore = attore;
	}

	public boolean isIntervistato() {
		return intervistato;
	}

	public void setIntervistato(boolean intervistato) {
		this.intervistato = intervistato;
	}

	@Override
	public String toString() {
		return "AttoriIntervistati [attore=" + attore + ", intervistato=" + intervistato + "]";
	} 
	
	
	
	
	
	
}
