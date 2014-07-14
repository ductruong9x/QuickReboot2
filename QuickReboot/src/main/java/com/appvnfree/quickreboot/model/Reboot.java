package com.appvnfree.quickreboot.model;

/**
 * Created by truongtvd on 7/14/14.
 */
public class Reboot {
    private String title;
    private String subtile;
    private int image;

    public Reboot(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtile() {
        return subtile;
    }

    public void setSubtile(String subtile) {
        this.subtile = subtile;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
