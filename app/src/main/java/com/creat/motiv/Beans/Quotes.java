package com.creat.motiv.Beans;

public class Quotes {


    public Quotes(String id, String quote, String author, String data, String categoria, String userID, String username, String userphoto, int likes, int backgroundcolor, int textcolor) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.data = data;
        this.categoria = categoria;
        this.userID = userID;
        this.username = username;
        this.userphoto = userphoto;
        this.likes = likes;
        this.backgroundcolor = backgroundcolor;
        this.textcolor = textcolor;
    }

    public Quotes() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
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

    String id,quote,author,data,categoria,userID,username,userphoto;
    int likes,backgroundcolor,textcolor;



}
