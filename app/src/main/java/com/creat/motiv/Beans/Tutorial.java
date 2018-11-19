package com.creat.motiv.Beans;

public class Tutorial {
    String imageuri,step;

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Tutorial(String imageuri, String step) {

        this.imageuri = imageuri;
        this.step = step;
    }
}
