package org.univaq.swa.webmarket.rest.jackson.utente;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.models.TipologiaUtente;

public class UtenteDeserializer extends JsonDeserializer<Utente> {

    @Override
    public Utente deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Utente utente = new Utente(); 
        
        JsonNode node = jp.getCodec().readTree(jp); 
        
        if (node.has("id")) {
            utente.setId(node.get("id").asInt());
        }
        
        if (node.has("username")) {
            utente.setUsername(node.get("username").asText());
        }
        
        if (node.has("email")) {
            utente.setEmail(node.get("email").asText());
        }
        
        if (node.has("password")) {
            utente.setPassword(node.get("password").asText());
        }

        if (node.has("tipologiaUtente")) {
            JsonNode tipologiaNode = node.get("tipologiaUtente");
            TipologiaUtente tipologiaUtente = jp.getCodec().treeToValue(tipologiaNode, TipologiaUtente.class);
            utente.setTipologiaUtente(tipologiaUtente);
        }
        
        return utente;
    }
}
