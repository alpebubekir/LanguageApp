package com.alpebubekir.languageapp;

import android.provider.MediaStore;

public class Word {

    private String tr;
    private String en;
    private String link;
    private String ses;
    private String id;

    public Word(String id,String tr, String en, String link,String ses) {
        this.tr = tr;
        this.en = en;
        this.link = link;
        this.ses = ses;
        this.id = id;
    }

    public String getTr() {
        return tr;
    }

    public String getEn() {
        return en;
    }

    public String getLink() {
        return link;
    }

    public String getSes() { return ses; }

    public String getId() {
        return id;
    }
}
