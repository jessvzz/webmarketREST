package org.univaq.swa.webmarket.rest.models;

import java.util.List;

public class RichiestaCompleta {
    private RichiestaOrdine richiesta;
    private List<CaratteristicaRichiesta> caratteristiche;

    public RichiestaCompleta() {
        super();
        richiesta = null;
        caratteristiche = null;
    }

    public RichiestaCompleta(RichiestaOrdine richiesta, List<CaratteristicaRichiesta> caratteristiche) {
        this.richiesta = richiesta;
        this.caratteristiche = caratteristiche;
    }
    
    public RichiestaOrdine getRichiesta() {
        return richiesta;
    }   

    public void setRichiesta(RichiestaOrdine richiesta) {
        this.richiesta = richiesta;
    }       

    public List<CaratteristicaRichiesta> getCaratteristiche() {
        return caratteristiche;
    }

    public void setCaratteristiche(List<CaratteristicaRichiesta> caratteristiche) {
        this.caratteristiche = caratteristiche;
    }
}
