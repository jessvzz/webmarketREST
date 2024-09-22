/*
 * Questa classe gestisce la logica di business delle richieste, come creazione, lettura e cancellazione delle richieste
 */

package org.univaq.swa.webmarket.rest.business;
import java.util.ArrayList; 
import java.util.List;      
import java.util.concurrent.atomic.AtomicInteger;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

//Per ora non uso i dao, ma uso una lista in memoria per salvare le richieste

public class RichiestaService {

    private static List<RichiestaOrdine> richieste = new ArrayList<>();
    private static AtomicInteger idCounter = new AtomicInteger();

    // Metodo per creare una nuova richiesta
    public static RichiestaOrdine creaRichiesta(RichiestaOrdine richiesta) {
        richiesta.setId(idCounter.incrementAndGet());
        richieste.add(richiesta);
        return richiesta;
    }

     // Metodo per ottenere tutte le richieste
    public static List<RichiestaOrdine> getTutteRichieste() {
        return richieste;
    }


    // Metodo per ottenere una singola richiesta per ID
    public static RichiestaOrdine getRichiestaById(int id) {
        return richieste.stream() // Cerca nella lista
                .filter(r -> r.getId() == id) // Filtra per ID
                .findFirst()  // Prende la prima (se esiste)
                .orElse(null); // Se non esiste, ritorna null
    }

    // Metodo per eliminare una richiesta per ID
    public static boolean eliminaRichiesta(int id) {
        return richieste.removeIf(r -> r.getId() == id);
    }
}
