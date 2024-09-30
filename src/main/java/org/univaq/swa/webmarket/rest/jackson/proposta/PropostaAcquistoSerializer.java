/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.proposta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.univaq.swa.webmarket.rest.models.Ordine;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;

/**
 *
 * @author jessviozzi
 */
public class PropostaAcquistoSerializer extends JsonSerializer<PropostaAcquisto> {
    @Override
    public void serialize(PropostaAcquisto item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); // {
        jgen.writeNumberField("id", item.getId());
        jgen.writeStringField("codice", item.getCodice());
        jgen.writeStringField("codiceProdotto", item.getCodiceProdotto());
        jgen.writeStringField("motivazione", item.getMotivazione());
        jgen.writeStringField("note", item.getNote());
        jgen.writeStringField("prodotto", item.getProdotto());
        jgen.writeStringField("produttore", item.getProduttore());
        jgen.writeStringField("url", item.getUrl());
        jgen.writeNumberField("prezzo", item.getPrezzo());
        jgen.writeObjectField("richiestaOrdine", item.getRichiestaOrdine());
        jgen.writeObjectField("stato", item.getStatoProposta());

        java.sql.Date sqlDate = item.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //in caso questo poi lo cambiamo
        String formattedDate = sdf.format(sqlDate);
        jgen.writeStringField("data", formattedDate);
        jgen.writeEndObject(); // }
    }}
