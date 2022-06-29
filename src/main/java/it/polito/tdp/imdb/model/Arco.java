package it.polito.tdp.imdb.model;

public class Arco {

	Actor a1; 
	Actor a2; 
	double peso; 
	
	public Arco(Actor a1, Actor a2) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = 0;
	}

	public Actor getA1() {
		return a1;
	}

	public void setA1(Actor a1) {
		this.a1 = a1;
	}

	public Actor getA2() {
		return a2;
	}

	public void setA2(Actor a2) {
		this.a2 = a2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	
	
	
	
}
