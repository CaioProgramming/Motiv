package com.creat.motiv.Beans;

public class Artists {
    String nome,uri;

    public Artists(String nome, String uri) {
        this.nome = nome;
        this.uri = uri;
    }

    public Artists() {
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
}
