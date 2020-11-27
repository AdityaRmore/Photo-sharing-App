package com.example.photoshop3;

public class Car  {
    private String CarName , imageurl , photo, photourl , imgwall , imageurlwall , favimage , data2name , data2url;

    public Car(String carName, String imageurl, String photo, String photourl, String imgwall, String imageurlwall, String favimage, String data2name, String data2url) {
        CarName = carName;
        this.imageurl = imageurl;
        this.photo = photo;
        this.photourl = photourl;
        this.imgwall = imgwall;
        this.imageurlwall = imageurlwall;
        this.favimage = favimage;
        this.data2name = data2name;
        this.data2url = data2url;
    }


    public Car (){
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotourl() {
        return photourl;
    }

    public String getImgwall() {
        return imgwall;
    }

    public void setImgwall(String imgwall) {
        this.imgwall = imgwall;
    }

    public String getImageurlwall() {
        return imageurlwall;
    }

    public void setImageurlwall(String imageurlwall) {
        this.imageurlwall = imageurlwall;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getFavimage() {
        return favimage;
    }

    public void setFavimage(String favimage) {
        this.favimage = favimage;
    }

    public String getData2name() {
        return data2name;
    }

    public void setData2name(String data2name) {
        this.data2name = data2name;
    }

    public String getData2url() {
        return data2url;
    }

    public void setData2url(String data2url) {
        this.data2url = data2url;
    }
}
