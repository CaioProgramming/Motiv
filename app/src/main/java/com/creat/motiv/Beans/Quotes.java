package com.creat.motiv.Beans;

public class Quotes {


    public Quotes(String id, String quote, String author, String data, String categoria, String userID, int likes, int backgroundcolor, int textcolor) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.data = data;
        this.categoria = categoria;
        this.userID = userID;
        this.likes = likes;
        this.backgroundcolor = backgroundcolor;
        this.textcolor = textcolor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(int backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public Quotes() {
    }


    String id,quote,author,data,categoria,userID;
    int likes,backgroundcolor,textcolor;



}
