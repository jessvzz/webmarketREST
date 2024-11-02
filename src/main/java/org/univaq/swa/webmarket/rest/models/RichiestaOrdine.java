package org.univaq.swa.webmarket.rest.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RichiestaOrdine{
    private int id;
    private String note;
    private StatoRichiesta stato; 

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date data;
    private String codiceRichiesta;
    private Utente utente;
    private Utente tecnico;
    private Categoria categoria;
    private List<PropostaAcquisto> proposte;
    private Map<String, String> caratteristiche = new HashMap<>();

    
    public RichiestaOrdine() {
        super();
        note = "";
        stato = null;
        data = null;
        codiceRichiesta = "";
        utente = null;
        tecnico = null;
        categoria = null;
        proposte = null;
        caratteristiche =null;
    }

    public RichiestaOrdine(int id, String note, StatoRichiesta stato, Date data, String codiceRichiesta, Utente utente, Utente tecnico, Categoria categoria, List<PropostaAcquisto> proposte, Map<String, String> caratteristiche) {
        this.id = id;
        this.note = note;
        this.stato = stato;
        this.data = data;
        this.codiceRichiesta = codiceRichiesta;
        this.utente = utente;
        this.tecnico = tecnico;
        this.categoria = categoria;
        this.proposte = proposte;
        this.caratteristiche = caratteristiche;
    }
    
    public List<PropostaAcquisto> getProposte() {
        return proposte;
    }

    public void setProposte(List<PropostaAcquisto> proposte) {
        this.proposte = proposte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCodiceRichiesta() {
        return codiceRichiesta;
    }

    public void setCodiceRichiesta(String codiceRichiesta) {
        this.codiceRichiesta = codiceRichiesta;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getTecnico() {
        return tecnico;
    }

    public void setTecnico(Utente tecnico) {
        this.tecnico = tecnico;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public Map<String, String> getCaratteristiche() {
        return caratteristiche;
    }

    public void setCaratteristiche(Map<String, String> caratteristiche) {
        this.caratteristiche = caratteristiche;
    }

    @Override
    public String toString() {
        return "RichiestaOrdine [id=" + id + ", note=" + note + ", stato=" + stato + ", data=" + data + ", codiceRichiesta="
                + codiceRichiesta + ", utente=" + utente + ", tecnico=" + tecnico + ", categoria=" + categoria + "]";
    }     
}