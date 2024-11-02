package org.univaq.swa.webmarket.rest.models;

import java.sql.Date;


public class Ordine{
    private int id;
    private StatoOrdine stato;
    private PropostaAcquisto propostaAcquisto;
    private Date data;


    public Ordine() {
        super();
        stato = null;
        propostaAcquisto = null;
        data = null;
    }


    public Ordine(int id, StatoOrdine stato, PropostaAcquisto propostaAcquisto, Date data) {
        this.id = id;
        this.stato = stato;
        this.propostaAcquisto = propostaAcquisto;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public PropostaAcquisto getProposta() {
        return propostaAcquisto;
    }

    public void setProposta(PropostaAcquisto propostaAcquisto) {
        this.propostaAcquisto = propostaAcquisto;
    }

    public Date getData() {
        return data;
       
    }

    public void setData(Date data) {
      this.data=data;
    }
}
