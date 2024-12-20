/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.univaq.swa.webmarket.rest.exceptions.AppExceptionMapper;
import org.univaq.swa.webmarket.rest.exceptions.JacksonExceptionMapper;
import org.univaq.swa.webmarket.rest.jackson.ObjectMapperContextResolver;
import org.univaq.swa.webmarket.rest.resources.ProposteRes;
import org.univaq.swa.webmarket.rest.resources.RichiesteRes;
import org.univaq.swa.webmarket.rest.security.AuthLoggedFilter;
import org.univaq.swa.webmarket.rest.security.AuthenticationRes;
import org.univaq.swa.webmarket.rest.security.CORSFilter;

/**
 *
 * @author didattica
 */
@ApplicationPath("rest")
public class RESTApp extends Application {
    private final Set<Class<?>> classes;
    public RESTApp() {
        HashSet<Class<?>> c = new HashSet<Class<?>>();
        //aggiungiamo tutte le *root resurces* (cioè quelle
        //con l'annotazione Path) che vogliamo pubblicare
        c.add(AuthenticationRes.class);
        c.add(RichiesteRes.class);
        c.add(ProposteRes.class);
        
        
        //aggiungiamo il provider Jackson per poter
        //usare i suoi servizi di serializzazione e 
        //deserializzazione JSON
        c.add(JacksonJsonProvider.class);
        //necessario se vogliamo una (de)serializzazione custom di qualche classe    
        c.add(ObjectMapperContextResolver.class);
        //esempio di autenticazione
        c.add(AuthLoggedFilter.class);
        //aggiungiamo il filtro che gestisce gli header CORS
        c.add(CORSFilter.class);
        //esempi di exception mapper, che mappano in Response eccezioni non già derivanti da WebApplicationException
        c.add(AppExceptionMapper.class);
        c.add(JacksonExceptionMapper.class);
        classes = Collections.unmodifiableSet(c);
    }
    //l'override di questo metodo deve restituire il set
    //di classi che Jersey utilizzerà per pubblicare il
    //servizio. Tutte le altre, anche se annotate, verranno
    //IGNORATE
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}