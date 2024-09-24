/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;

/**
 *
 * @author jessviozzi
 */
public class PropostaRes {
    private final PropostaAcquisto proposta;

    PropostaRes(PropostaAcquisto proposta) {
        this.proposta = proposta;
    }
    
    @GET
    @Produces("application/json")
    public Response getItem() {
        if (proposta == null) {
            throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata.");
        }
        return Response.ok(proposta).build();
    }


}
