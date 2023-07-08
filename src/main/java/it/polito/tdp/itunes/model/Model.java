package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultEdge>grafo;
	private List<Album>allAlbum;
	private ItunesDAO dao;
	private Map<Integer, Album>idMapAlbums;
	private Map<String, Album>nameMapAlbums;
	
	private int numAlbumMax;
	private List<Album> migliore;
	
	public Model() {
		this.dao = new ItunesDAO();
		this.allAlbum = new ArrayList<>();
		this.idMapAlbums = new HashMap<>();
		this.nameMapAlbums = new HashMap<>();
	}

	public String creaGrafo(Double durataMinS) {
		Double durataMinMS = durataMinS*1000;
		this.grafo = new SimpleGraph<Album, DefaultEdge>(DefaultEdge.class);
		
		this.allAlbum = new ArrayList<>(dao.getAllAlbums(durataMinMS));
		Graphs.addAllVertices(grafo, this.allAlbum);
		
		for(Album x: this.allAlbum) {
			this.idMapAlbums.put(x.getAlbumId(), x);
			this.nameMapAlbums.put(x.getTitle(), x);
		}
		
		for(Album x : this.allAlbum) {
			for(Album y : this.allAlbum) {
				if(!x.equals(y)) {
					Double peso = dao.getWeight(x, y);
					if(peso >=1) {
						grafo.addEdge(x, y);
					}
				}
			}	
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}

	public Set<Album> doComponente(String albumName) {
		Album a1 = this.idMapAlbums.get(albumName);
		ConnectivityInspector<Album, DefaultEdge> inspector = new ConnectivityInspector<Album, DefaultEdge>(this.grafo);
		Set<Album> connessi = inspector.connectedSetOf(a1);
		return connessi;
	}
	
	public Integer dimensioneComponente(String albumName) {
		Integer dim = 0;
		for(Album x : doComponente(albumName)) {
			dim ++;
		}
		return dim;
	}
	
	public Integer numeroTotBraniComponente(String albumName) {
		Integer nTot = 0;
		Set<Album>connessi = this.doComponente(albumName);
		for(Album x : connessi) {
			nTot += dao.getNBrani(x);
		}
		return nTot;
	}
	
	public void calcolaPercorso(Double durataMAX, String albumName) {
		Album a1 = this.nameMapAlbums.get(albumName);
		this.numAlbumMax = 0;
		this.migliore = new ArrayList<Album>();
		List<Album> rimanenti = new ArrayList<>(this.doComponente(albumName));
		List<Album> parziale = new ArrayList<>();
		
		if(a1.getDurata()>durataMAX) {
			return;
		}
		parziale.add(a1);
		rimanenti.remove(a1);
	
		ricorsione(parziale, rimanenti, durataMAX, 1, a1.getDurata());
		
	}

	
	private void ricorsione(List<Album> parziale, List<Album> rimanenti, Double durataMAX, int livello, Double durataParziale){
		
		// Condizione Terminale
		if (rimanenti.isEmpty()) {
			//calcolo dimensione
			Integer dimens = parziale.size();
			if (dimens > this.numAlbumMax && durataParziale < durataMAX) {
				this.numAlbumMax = dimens;
				this.migliore = new ArrayList<>(parziale);
			}
			return;
		}
		
       	for (Album p : rimanenti) {
       		if(durataParziale+p.getDurata() <= durataMAX) {//se non mi fa uscire dal vincolo sulla durata: provo a fare ricorsione e ad aggiungere
       			List<Album> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(p);
 				currentRimanenti.remove(p);
 				ricorsione(parziale, currentRimanenti, durataMAX, livello+1, durataParziale+p.getDurata());
 				parziale.remove(parziale.size()-1);
 			}
 		}

	}
	

	public Graph<Album, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Album> getAllAlbum() {
		return allAlbum;
	}

	public Map<Integer, Album> getIdMapAlbums() {
		return idMapAlbums;
	}

	public int getNumAlbumMax() {
		return numAlbumMax;
	}

	public List<Album> getMigliore() {
		return migliore;
	}

	
	
		
}
