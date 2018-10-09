package org.sxchinacourt.bean;

/**
 * Created by 殇冰无恨 on 2017/12/4.
 */

public class Fruit {
    private String name;
    private int imageId;

    public Fruit() {
    }

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
