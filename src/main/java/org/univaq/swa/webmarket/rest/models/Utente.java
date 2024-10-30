package org.univaq.swa.webmarket.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Utente{
    private int id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private TipologiaUtente tipologiaUtente;

    // Costruttori
    public Utente() {
        super();
        username = "";
        email = "";
        password = "";
        tipologiaUtente = null;
    }

    public Utente(int id, String username, String email, String password, TipologiaUtente tipologiaUtente) {
        this.id= id;
        this.username = username;
        this.email= email;
        this.password= password;
        this.tipologiaUtente= tipologiaUtente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username= username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password= password;
    }

    public TipologiaUtente  getTipologiaUtente() {
        return tipologiaUtente;
    }

    public void setTipologiaUtente(TipologiaUtente tipologiaUtente) {
        this.tipologiaUtente = tipologiaUtente;
    }

}
