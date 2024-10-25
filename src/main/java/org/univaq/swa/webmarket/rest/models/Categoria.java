package org.univaq.swa.webmarket.rest.models;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;




public class Categoria{

    private int id;
    private String nome;
    
    private List<Caratteristica> caratteristiche;

    // Costruttori
    public Categoria() {
        super();
        nome = "";
    }

    public Categoria(int id, String nome, int padre) {
        this.id= id;
        this.nome= nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome= nome;
    }


    public List<Caratteristica> getCaratteristiche() {
        return caratteristiche;
    }

    public void setCaratteristiche(List<Caratteristica> caratteristiche) {
        this.caratteristiche = caratteristiche;
    }
}