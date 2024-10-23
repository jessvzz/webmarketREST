package org.univaq.swa.webmarket.rest.jackson.richiesta;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;

public class RichiestaOrdineDeserializer extends JsonDeserializer<RichiestaOrdine> {
    @Override
    public RichiestaOrdine deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        RichiestaOrdine richiesta = new RichiestaOrdine(); // Creo un oggetto vuoto di RichiestaOrdine
        
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("id")) {
            richiesta.setId(node.get("id").asInt());
        }
        if (node.has("data")) {
            String dataStr = node.get("data").asText();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(dataStr);
                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                richiesta.setData(sqlDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        if (node.has("stato")) {
            String statoStr = node.get("stato").asText();
            StatoRichiesta stato = StatoRichiesta.valueOf(statoStr);
            richiesta.setStato(stato);
        }

        if (node.has("utente")) {
            
            JsonNode utenteNode = node.get("utente");
            Utente utente = jp.getCodec().treeToValue(utenteNode, Utente.class);
            richiesta.setUtente(utente);
        }

        if (node.has("categoria")) {
            JsonNode categoriaNode = node.get("categoria");
            Categoria categoria = jp.getCodec().treeToValue(categoriaNode, Categoria.class);
            richiesta.setCategoria(categoria);
        }

        if (node.has("note")) {
            richiesta.setNote(node.get("note").asText());
        }
        
        if (node.has("codiceRichiesta")) {
            richiesta.setCodiceRichiesta(node.get("codiceRichiesta").asText());
        }
        
        if (node.has("tecnico")) {
            JsonNode tecnicoNode = node.get("tecnico");
            Utente tecnico = jp.getCodec().treeToValue(tecnicoNode, Utente.class);
            richiesta.setTecnico(tecnico);
        }

         if (node.has("categoria")) {
             JsonNode categoriaNode = node.get("categoria");
             Categoria categoria = jp.getCodec().treeToValue(categoriaNode, Categoria.class);
             richiesta.setCategoria(categoria);
         }
         
         if (node.has("proposte")) {
            JsonNode proposteNode = node.get("proposte");
            ObjectMapper mapper = new ObjectMapper();
            
            List<PropostaAcquisto> proposte = new ArrayList<>();
            for (JsonNode propostaNode : proposteNode) {
                PropostaAcquisto proposta = mapper.treeToValue(propostaNode, PropostaAcquisto.class);
                proposte.add(proposta);
            }
            richiesta.setProposte(proposte);
        }

        
        return richiesta;
    }
}
