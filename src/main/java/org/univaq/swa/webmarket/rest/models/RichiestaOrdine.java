package org.univaq.swa.webmarket.rest.models;

import java.util.Date;



public class RichiestaOrdine{
    private int id;
    private String note;
    private StatoRichiesta stato; //alternativa boolean senza enum
    private Date data;
    private String codiceRichiesta;
    private Utente utente;
    private Utente tecnico;
    private Categoria categoria;

    // Costruttori
    public RichiestaOrdine() {
        super();
        note = "";
        stato = null;
        data = null;
        codiceRichiesta = "";
        utente = null;
        tecnico = null;
        categoria = null;
    }

    public RichiestaOrdine(int id, String note, StatoRichiesta stato, Date data, String codiceRichiesta, Utente utente, Utente tecnico, Categoria categoria) {
        this.id = id;
        this.note = note;
        this.stato = stato;
        this.data = data;
        this.codiceRichiesta = codiceRichiesta;
        this.utente = utente;
        this.tecnico = tecnico;
        this.categoria = categoria;
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
}