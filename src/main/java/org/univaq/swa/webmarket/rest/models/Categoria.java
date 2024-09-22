package org.univaq.swa.webmarket.rest.models;


import java.util.List;




public class Categoria{

    private int id;
    private String nome;
    private Integer padre; //  permette valori nulli
    private List<Caratteristica> caratteristiche;

    // Costruttori
    public Categoria() {
        super();
        nome = "";
        padre = null;
    }

    public Categoria(int id, String nome, int padre) {
        this.id= id;
        this.nome= nome;
        this.padre= padre;
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

    public int getPadre() {
        return padre;
    }

    public void setPadre(int padre) {
        this.padre = padre;
    }

    public List<Caratteristica> getCaratteristiche() {
        return caratteristiche;
    }

    public void setCaratteristiche(List<Caratteristica> caratteristiche) {
        this.caratteristiche = caratteristiche;
    }
}