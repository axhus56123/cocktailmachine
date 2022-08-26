package com.example.wifitest;

public class hisuser {

    private long hisdrink1,hisdrink2,hisdrink3;

    private hisuser(){}
    private hisuser(long hisdrink1,long hisdrink2,long hisdrink3){
        this.hisdrink1 = hisdrink1;
        this.hisdrink2 = hisdrink2;
        this.hisdrink3 = hisdrink3;
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

    public void setHisdrink1(long hisdrink1) {
        this.hisdrink1 = hisdrink1;
    }

    public void setHisdrink2(long hisdrink2) {
        this.hisdrink2 = hisdrink2;
    }

    public void setHisdrink3(long hisdrink3) {
        this.hisdrink3 = hisdrink3;
    }

}
