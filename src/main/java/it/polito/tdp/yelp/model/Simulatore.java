package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Evento.EventType;

public class Simulatore {
	
	//dati in ingresso
	private int x1;
	private int x2;
	
	//dati in uscita
	
		//i giornalisti possono essere o semplicemente numeri interi oppure posso creare una classe
		private List<Giornalista> giornalisti;
		private int numeroGiorni;
		
	
	//modello del mondo
	private Set<User> intervistati;
	private Graph<User, DefaultWeightedEdge> grafo;
	
	
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	
	public Simulatore(Graph<User, DefaultWeightedEdge> grafo) {
		this.grafo= grafo;
		
	}
	
	
	public void init(int x1, int x2) {
		this.x1= x1;
		this.x2= x2;
		
		this.intervistati= new HashSet<User>();
		
		this.numeroGiorni= 0;
		
		this.giornalisti= new ArrayList<>();
		for(int id=0; id<this.x1; id++) {
			this.giornalisti.add(new Giornalista(id));
		}
		
		
		for(Giornalista g: this.giornalisti) {
			User intervistato= selezionaIntervistato(this.grafo.vertexSet());
			
			this.intervistati.add(intervistato);
			g.incrementaNumeroIntervistati();
			
			this.queue.add( new Evento(1, EventType.DA_INTERVISTARE, intervistato, g));
		}
	}
	
	public void run() {
		
		while(!this.queue.isEmpty() && this.intervistati.size()<x2) {
			
			Evento e= this.queue.poll();
			this.numeroGiorni = e.getGiorni();
			
			processEvent(e);
			
		}
		
	}


	private void processEvent(Evento e) {
		
		switch(e.getTipo()) {
		
		case DA_INTERVISTARE:
			
			double caso= Math.random();
			
			if(caso<0.6) {
				//caso 1
				
				User vicino= slezionaAdiacente(e.getIntervistato());
				if(vicino == null) {
					vicino= selezionaIntervistato(this.grafo.vertexSet());   //seleziona il vicino tra tutto il grafo
				}
				
				this.queue.add(new Evento(e.getGiorni()+1, EventType.DA_INTERVISTARE, vicino, e.getGiornalista()));
				this.intervistati.add(vicino);
				e.getGiornalista().incrementaNumeroIntervistati();
				
			} else if(caso<0.8) {
				//caso 2:
				this.queue.add(new Evento(e.getGiorni()+1, EventType.FERIE, e.getIntervistato(), e.getGiornalista()));
				
			} else{
				//caso 3: domani continuo con lo stesso
				this.queue.add(new Evento(e.getGiorni()+1, EventType.DA_INTERVISTARE, e.getIntervistato(), e.getGiornalista()));
			}
			
			
			
			break;
		case FERIE:
			break;
		
		}
		
	}


	public int getX1() {
		return x1;
	}


	public void setX1(int x1) {
		this.x1 = x1;
	}


	public int getX2() {
		return x2;
	}


	public void setX2(int x2) {
		this.x2 = x2;
	}


	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}


	public int getNumeroGiorni() {
		return numeroGiorni;
	}
	
	/**
	 * seleziona un intervistato dalla lista specificata, evitando
	 * di selezionare coloro che sono già in this.intervistati
	 * @param lista
	 * @return
	 */
	
	private User selezionaIntervistato(Collection<User> lista) {
		Set<User> candidati= new HashSet<User>(lista);
		candidati.removeAll(this.intervistati);
		
		int scelto= (int) (Math.random()*candidati.size());
		
		return (new ArrayList<User>(candidati)).get(scelto); 
	}
	
	
	private User slezionaAdiacente(User u) {
		List<User> vicini= Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.intervistati);
		
		if(vicini.size()==0) {
			//quado vertice isolato o tutti gli adiacenti sono stati intervistati
			return null;
		}
		
		double max=0;
		for(User v: vicini) {
			double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if(peso > max) {
				max= peso;
			}
		}
		
		List<User> migliori= new ArrayList<>();
		for(User v: vicini) {
			double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if(peso== max) {
				migliori.add(v);
			}
		}
		
		int scelto= (int) (Math.random()*migliori.size());
		return migliori.get(scelto);
	}
	
	
	
	

}
