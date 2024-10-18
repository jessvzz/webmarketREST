/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.richiesta;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.RichiestaCompleta;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 *
 * @author jessviozzi
 */
public class RichiestaCompletaDeserializer extends JsonDeserializer<RichiestaCompleta> {
    @Override
    public RichiestaCompleta deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        RichiestaCompleta richiestaCompleta = new RichiestaCompleta();
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("richiestaOrdine")) {
            JsonNode richiestaOrdineNode = node.get("richiestaOrdine");
            RichiestaOrdine richiestaOrdine = jp.getCodec().treeToValue(richiestaOrdineNode, RichiestaOrdine.class);
            richiestaCompleta.setRichiesta(richiestaOrdine);
        }

        if (node.has("caratteristiche")) {
            JsonNode caratteristicheNode = node.get("caratteristiche");
            List<CaratteristicaRichiesta> caratteristiche = new ArrayList<>();
            for (JsonNode caratteristicaNode : caratteristicheNode) {
                CaratteristicaRichiesta caratteristica = jp.getCodec().treeToValue(caratteristicaNode, CaratteristicaRichiesta.class);
                caratteristiche.add(caratteristica);
            }
            richiestaCompleta.setCaratteristiche(caratteristiche);
        }

        return richiestaCompleta;
    }
}
