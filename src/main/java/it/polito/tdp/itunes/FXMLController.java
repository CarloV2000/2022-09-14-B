/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<String> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	String albumName = this.cmbA1.getValue();
    	if(albumName == null) {
    		this.txtResult.setText("Selezionare un album dalla box");
    		return;
    	}
    	model.doComponente(albumName);
    	Integer dim = model.dimensioneComponente(albumName);
    	Integer nBrani = model.numeroTotBraniComponente(albumName);
    	String s = "Componente connessa: \n Dimensione = "+dim+"\n Numero brani: "+nBrani;
    	this.txtResult.appendText(s);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String durataS = this.txtDurata.getText();
    	Double durata;
    	try {
    		durata = Double.parseDouble(durataS);
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Inserire una durata nel campo ");
    		return;
    	}
    	
    	String s = model.creaGrafo(durata);
    	this.txtResult.setText(s);
    	
    	for(Album x : model.getGrafo().vertexSet()) {
    		this.cmbA1.getItems().add(x.getTitle());
    	}
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
    	String durataS = this.txtDurata.getText();
    	Double durata;
    	try {
    		durata = Double.parseDouble(durataS);
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Inserire una durata nel campo ");
    		return;
    	}
    	String albumName = this.cmbA1.getValue();
    	if(albumName == null) {
    		this.txtResult.setText("Selezionare un album dalla box");
    		return;
    	}
    	model.calcolaPercorso(durata, albumName);
    	List<Album> res = new ArrayList<>(model.getMigliore());
    	int n = model.getNumAlbumMax();
    	String s = "Set trovato : dimensione = "+n+" \n";
    	for(Album x :res) {
    		s += "("+x.getAlbumId()+") "+x.getTitle()+"\n";
    	}
    	this.txtResult.setText(s);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
