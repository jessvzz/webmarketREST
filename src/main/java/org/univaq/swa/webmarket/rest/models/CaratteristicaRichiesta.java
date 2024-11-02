package org.univaq.swa.webmarket.rest.models;


public class CaratteristicaRichiesta{
    private int id;
    private RichiestaOrdine richiestaOrdine;
    private Caratteristica caratteristica;
    private String valore;

 
    public CaratteristicaRichiesta() {
        super();
        richiestaOrdine=null;
        caratteristica=null;
        valore=null;
    }

    public CaratteristicaRichiesta(int id, RichiestaOrdine richiestaOrdine, Caratteristica caratteristica, String valore) {
        this.id = id;
        this.richiestaOrdine = richiestaOrdine;
        this.caratteristica = caratteristica;
        this.valore = valore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RichiestaOrdine getRichiestaOrdine() {
        return richiestaOrdine;
    }

    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        this.richiestaOrdine = richiestaOrdine;
    }

    public Caratteristica getCaratteristica() {
        return caratteristica;
    }

    public void setCaratteristica(Caratteristica caratteristica) {
        this.caratteristica = caratteristica;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }


    @Override
    public String toString() {
        return "CaratteristicaRichiesta{" +
                "id=" + id +
                ", richiestaOrdine=" + richiestaOrdine.getId() +
                ", caratteristica=" + caratteristica.getId() +
                ", valore='" + valore + '\'' +
                '}';
    }
}