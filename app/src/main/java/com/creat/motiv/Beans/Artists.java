package com.creat.motiv.Beans;

public class Artists {
    private String nome,uri;
   private int color;

    public Artists() {
    }

    public Artists(String nome, String uri, int color) {
        this.nome = nome;
        this.uri = uri;
        this.color = color;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
