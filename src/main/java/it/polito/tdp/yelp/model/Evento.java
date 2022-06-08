package it.polito.tdp.yelp.model;

public class Evento implements Comparable<Evento>{
	
	
	public enum EventType{
		
		DA_INTERVISTARE,
		FERIE
	}
	
	private int giorni;
	private EventType tipo;
	private User intervistato;
	private Giornalista giornalista;
	
	
	
	public Evento(int giorni, EventType tipo,User intervistato, Giornalista giornalista) {
		super();
		this.giorni = giorni;
		this.intervistato = intervistato;
		this.giornalista = giornalista;
		this.tipo= tipo;
	}
	
	public int getGiorni() {
		return giorni;
	}
	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}
	public User getIntervistato() {
		return intervistato;
	}
	public void setIntervistato(User intervistato) {
		this.intervistato = intervistato;
	}
	public Giornalista getGiornalista() {
		return giornalista;
	}
	public void setGiornalista(Giornalista giornalista) {
		this.giornalista = giornalista;
	}
	
	

	public EventType getTipo() {
		return tipo;
	}

	@Override
	public int compareTo(Evento o) {
		return this.giorni- o.giorni;
	}
	
	
	

}
