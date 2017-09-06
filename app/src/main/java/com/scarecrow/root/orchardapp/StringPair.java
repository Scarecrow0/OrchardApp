package com.scarecrow.root.orchardapp;

import java.io.Serializable;

public class StringPair implements Serializable{
    private String mfirst,mnext;
    public StringPair(String first, String next){
        this.mfirst = new String(first);
        this.mnext = new String(next);
    }
    public StringPair(StringPair sp){
        this.mfirst = new String(sp.getMfirst());
        this.mnext = new String(sp.getMnext());
    }
    public String getMfirst(){
        return new String(this.mfirst);
    }
    public String getMnext(){
        return new String(this.mnext);
    }
}
