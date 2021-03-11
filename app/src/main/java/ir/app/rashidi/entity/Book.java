package ir.app.rashidi.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String name;
    private String image;
    private String fileUrl;
    private String nasherTell;
    private float score;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getNasherTell() {
        return nasherTell;
    }

    public void setNasherTell(String nasherTell) {
        this.nasherTell = nasherTell;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
