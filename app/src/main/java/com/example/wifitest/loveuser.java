package com.example.wifitest;

public class loveuser {
    private String lovename,lovedrink1,lovedrink2,lovedrink3/*,lovedrink4,lovedrink5*/;
    private String lovedrink1ml,lovedrink2ml,lovedrink3ml/*,lovedrink4ml,lovedrink5ml*/;

    public loveuser(){}
    public loveuser(String lovename, String lovedrink1, String lovedrink2, String lovedrink3,/*String lovedrink4,String lovedrink5,*/ String lovedrink1ml, String lovedrink2ml, String lovedrink3ml/*,String lovedrink4ml,String lovedrink5ml*/){
        this.lovename = lovename;
        this.lovedrink1 = lovedrink1;
        this.lovedrink2 = lovedrink2;
        this.lovedrink3 = lovedrink3;
        //this.lovedrink4 = lovedrink4;
        //this.lovedrink5 = lovedrink5;
        this.lovedrink1ml = lovedrink1ml;
        this.lovedrink2ml = lovedrink2ml;
        this.lovedrink3ml = lovedrink3ml;
        //this.lovedrink4ml = lovedrink4ml;
        //this.lovedrink5ml = lovedrink5ml;
    }

    public String getLovename() {
        return lovename;
    }

    public String getLovedrink1() {
        return lovedrink1;
    }

    public String getLovedrink2() {
        return lovedrink2;
    }

    public String getLovedrink3() {
        return lovedrink3;
    }

    /*public String getLovedrink4() {
        return lovedrink4;
    }

    public String getLovedrink5() {
        return lovedrink5;
    }*/

    public String getLovedrink1ml() {
        return lovedrink1ml;
    }

    public String getLovedrink2ml() {
        return lovedrink2ml;
    }

    public String getLovedrink3ml() {
        return lovedrink3ml;
    }

    /*public String getLovedrink4ml() {
        return lovedrink4ml;
    }

    public String getLovedrink5ml() {
        return lovedrink5ml;
    }*/

    public void setLovename(String lovename) {
        this.lovename = lovename;
    }

    public void setLovedrink1(String lovedrink1) {
        this.lovedrink1 = lovedrink1;
    }

    public void setLovedrink2(String lovedrink2) {
        this.lovedrink2 = lovedrink2;
    }

    public void setLovedrink3(String lovedrink3) {
        this.lovedrink3 = lovedrink3;
    }

    /*public void setLovedrink4(String lovedrink4) {
        this.lovedrink4 = lovedrink4;
    }

    public void setLovedrink5(String lovedrink5) {
        this.lovedrink5 = lovedrink5;
    }*/

    public void setLovedrink1ml(String lovedrink1ml) {
        this.lovedrink1ml = lovedrink1ml;
    }

    public void setLovedrink2ml(String lovedrink2ml) {
        this.lovedrink2ml = lovedrink2ml;
    }

    public void setLovedrink3ml(String lovedrink3ml) {
        this.lovedrink3ml = lovedrink3ml;
    }

    /*public void setLovedrink4ml(String lovedrink4ml) {
        this.lovedrink4ml = lovedrink4ml;
    }

    public void setLovedrink5ml(String lovedrink5ml) {
        this.lovedrink5ml = lovedrink5ml;
    }*/
}
