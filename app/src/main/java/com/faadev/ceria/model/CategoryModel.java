package com.faadev.ceria.model;

public class CategoryModel {
    private String category;
    private boolean selected = false;

    public CategoryModel(String category, boolean selected) {
        this.category = category;
        this.selected = selected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
