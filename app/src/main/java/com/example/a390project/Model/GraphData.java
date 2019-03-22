package com.example.a390project.Model;

import java.util.List;

public class GraphData {
    private List<Float> x;
    private List<Float> y;
    private String graphTitle;

    public GraphData(List<Float> x, List<Float> y, String graphTitle) {
        this.x = x;
        this.y = y;
        this.graphTitle = graphTitle;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    public List<Float> getX() {
        return x;
    }

    public void setX(List<Float> x) {
        this.x = x;
    }

    public List<Float> getY() {
        return y;
    }

    public void setY(List<Float> y) {
        this.y = y;
    }
}
