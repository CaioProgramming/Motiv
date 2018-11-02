package com.creat.motiv.Beans;

public class Developers {
    String nome,cargo,backgif,photouri,linkedin;

    public Developers(String nome, String cargo, String backgif, String photouri, String linkedin) {
        this.nome = nome;
        this.cargo = cargo;
        this.backgif = backgif;
        this.photouri = photouri;
        this.linkedin = linkedin;
    }

    public Developers() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getBackgif() {
        return backgif;
    }

    public void setBackgif(String backgif) {
        this.backgif = backgif;
    }

    public String getPhotouri() {
        return photouri;
    }

    public void setPhotouri(String protouri) {
        this.photouri = protouri;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }
}
