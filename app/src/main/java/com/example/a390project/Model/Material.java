package com.example.a390project.Model;

public class Material {
    String materialName;
    String materialDescription;
    Float materialQuantity;

    Material(){}

    public Material(String materialDescription, Float materialQuantity){
        this.materialDescription = materialDescription;
        this.materialQuantity = materialQuantity;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public Float getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(Float materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
