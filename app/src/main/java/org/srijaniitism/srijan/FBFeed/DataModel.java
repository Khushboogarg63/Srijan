package org.srijaniitism.srijan.FBFeed;

public class DataModel {

    String name;
    //String version;
    //int id_;
    String image;

    public DataModel(String name, String image) {
        this.name = name;
        //this.version = version;
        //this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
