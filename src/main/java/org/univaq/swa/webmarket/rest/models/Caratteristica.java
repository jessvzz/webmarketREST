package org.univaq.swa.webmarket.rest.models;

/**
 *
 * @author user
 */
public class Caratteristica{

    private int id;
    private String nome;
    private Categoria categoria;
    
    // Costruttori
    public Caratteristica() {
        super();
        nome = "";
        categoria = null;
    }

    public Caratteristica(int id, String nome, Categoria categoria) {
        this.id= id;
        this.nome= nome;
        this.categoria= categoria;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome= nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria= categoria;
    }
}