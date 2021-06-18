package com.example.dev;

public class Model {
    private int id;
    private String ad;
    private String adres;
    private String konum;
    private byte[] image;

    public Model(int id, String ad, String adres, String konum, byte[] image) {
        this.id = id;
        this.ad = ad;
        this.adres = adres;
        this.konum = konum;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
