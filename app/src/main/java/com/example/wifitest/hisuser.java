package com.example.wifitest;

public class hisuser {

    private  String time;
    private long hisdrink1,hisdrink2,hisdrink3,hisdrink4,hisdrink5,hisdrink6;

    private hisuser(){}
    private hisuser(String time,long hisdrink1,long hisdrink2,long hisdrink3,long hisdrink4,long hisdrink5,long hisdrink6){
        this.time = time;
        this.hisdrink1 = hisdrink1;
        this.hisdrink2 = hisdrink2;
        this.hisdrink3 = hisdrink3;
        this.hisdrink4 = hisdrink4;
        this.hisdrink5 = hisdrink5;
        this.hisdrink6 = hisdrink6;
    }

    public String getTime() {
        return time;
    }

    public long getHisdrink1() {
        return hisdrink1;
    }

    public long getHisdrink2() {
        return hisdrink2;
    }

    public long getHisdrink3() {
        return hisdrink3;
    }

    public long getHisdrink4() { return hisdrink4;  }

    public long getHisdrink5() {
        return hisdrink5;
    }
    public long getHisdrink6() {
        return hisdrink6;
    }

    public void setHisdrink1(long hisdrink1) {
        this.hisdrink1 = hisdrink1;
    }

    public void setHisdrink2(long hisdrink2) {
        this.hisdrink2 = hisdrink2;
    }

    public void setHisdrink3(long hisdrink3) {
        this.hisdrink3 = hisdrink3;
    }

    public void setHisdrink4(long hisdrink4) {
        this.hisdrink4 = hisdrink4;
    }

    public void setHisdrink5(long hisdrink5) {
        this.hisdrink5 = hisdrink5;
    }
    public void setHisdrink6(long hisdrink6) {
        this.hisdrink6 = hisdrink6;
    }

}
