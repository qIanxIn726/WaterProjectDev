package com.example.water.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailModel implements Serializable {

    /**
     * at : 2017-06-23 11:11:12.839
     * value : ad3
     */
    //@SerializedName("at")
    private String at;

    //@SerializedName("value")
    private String value;

    @SerializedName("unit_symbol")
    private String unitSymbol;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public int getSize(){
        return value.length();
    }
}
