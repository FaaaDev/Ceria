package com.faadev.ceria.model;

public class NominalModel {
    int nominal;
    boolean selected;

    public NominalModel(int nominal) {
        this.nominal = nominal;
        this.selected = false;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
