package com.java.note.Jdk.model.builders;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  8:46
 * @Description
 */
public class Product {

    private Strings buildA;
    private Strings buildB;
    private Strings buildC;
    private Strings buildD;

    public Strings getBuildA() {
        return buildA;
    }

    public void setBuildA(Strings buildA) {
        this.buildA = buildA;
    }

    public Strings getBuildB() {
        return buildB;
    }

    public void setBuildB(Strings buildB) {
        this.buildB = buildB;
    }

    public Strings getBuildC() {
        return buildC;
    }

    public void setBuildC(Strings buildC) {
        this.buildC = buildC;
    }

    public Strings getBuildD() {
        return buildD;
    }

    public void setBuildD(Strings buildD) {
        this.buildD = buildD;
    }

    @Override
    public Strings toString() {
        return "Product{" +
                "buildA='" + buildA + '\'' +
                ", buildB='" + buildB + '\'' +
                ", buildC='" + buildC + '\'' +
                ", buildD='" + buildD + '\'' +
                '}';
    }
}
