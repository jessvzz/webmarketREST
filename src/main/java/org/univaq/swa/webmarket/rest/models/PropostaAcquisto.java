package org.univaq.swa.webmarket.rest.models;


import java.sql.Date;



public class PropostaAcquisto{
    private int id;
    private String produttore;
    private String prodotto;
    private String codiceProdotto;
    private String codice;
    private float prezzo;
    private String url;
    private String note;
    private StatoProposta stato;
    private Date data;
    private String motivazione;
    private RichiestaOrdine richiestaOrdine;

    // Costruttori
    public PropostaAcquisto() {
        super();
        produttore = "";
        prodotto = "";
        codiceProdotto = "";
        codice = "";
        prezzo = 0;
        url = "";
        note = "";
        stato = null;
        data = null;
        motivazione = "";
        richiestaOrdine = null;
    }

    public PropostaAcquisto(int id, String produttore, String prodotto, String codiceProdotto, String codice, float prezzo, String url, String note, StatoProposta stato,Date data, String motivazione, RichiestaOrdine richiestaOrdine) {
        this.id = id;
        this.produttore = produttore;
        this.prodotto = prodotto;
        this.codiceProdotto = codiceProdotto;
        this.codice = codice;
        this.prezzo = prezzo;
        this.url = url;
        this.note = note;
        this.stato = stato;
        this.data = data;
        this.motivazione = motivazione;
        this.richiestaOrdine = richiestaOrdine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduttore() {
        return produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    public String getProdotto() {
        return prodotto;
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    
    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StatoProposta getStatoProposta() {
        return stato;
    }

    public void setStatoProposta(StatoProposta stato) {
        this.stato = stato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMotivazione() {
        return motivazione;
    }

    public void setMotivazione(String motivazione) {
        this.motivazione = motivazione;
    }

    public RichiestaOrdine getRichiestaOrdine() {
        return richiestaOrdine;
    }

    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        this.richiestaOrdine = richiestaOrdine;
    }
    
    


}
