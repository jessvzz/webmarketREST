/*
 * Questa classe gestisce la logica REST e serve come interfaccia per accedere alla logica di business del servizio tramite HTTP
 * (ad esempio, per creare, leggere o eliminare richieste).
 */

package org.univaq.swa.webmarket.rest.resources;
import java.util.List;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.univaq.swa.webmarket.rest.business.RichiestaService;

    @Path("/richieste")
public class RichiestaRes {

    // Endpoint per creare una nuova richiesta (metodo HTTP POST)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)  // Aspetta un input in formato JSON
    @Produces(MediaType.APPLICATION_JSON)  // Restituisce un output in formato JSON
    public Response createRichiesta(RichiestaOrdine richiesta) {
        RichiestaOrdine nuovaRichiesta = RichiestaService.creaRichiesta(richiesta); // Crea una nuova richiesta usando il servizio
        return Response.status(Response.Status.CREATED).entity(nuovaRichiesta).build(); // Restituisce lo stato "201 Created" con la nuova richiesta
    }

    // Endpoint per ottenere tutte le richieste (metodo HTTP GET)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RichiestaOrdine> getRichieste() {
        return RichiestaService.getTutteRichieste();
    }

    // Endpoint per ottenere una singola richiesta tramite ID (metodo HTTP GET)
    @GET
    @Path("/{id}") // Il parametro {id} sarà estratto dall'URL
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesta(@PathParam("id") int id) {
        RichiestaOrdine richiesta = RichiestaService.getRichiestaById(id); // Cerca la richiesta tramite ID
        if (richiesta == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // Se non trovata, ritorna 404 Not Found
        }
        return Response.ok(richiesta).build(); // Se trovata, ritorna 200 OK con la richiesta
    }

    // Endpoint per eliminare una richiesta tramite ID (metodo HTTP DELETE)
    @DELETE
    @Path("/{id}")
    public Response deleteRichiesta(@PathParam("id") int id) {
        boolean eliminata = RichiestaService.eliminaRichiesta(id);
        if (eliminata) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}


