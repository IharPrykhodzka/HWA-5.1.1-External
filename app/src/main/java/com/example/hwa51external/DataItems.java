package com.example.hwa51external;

public class DataItems {

    private String title_view;
    private String subTitle_view;
    private int imageID;
    private boolean checked;

    public DataItems(String title_view, String subTitle_view, int imageID, boolean checked) {
        this.title_view = title_view;
        this.subTitle_view = subTitle_view;
        this.imageID = imageID;
        this.checked = checked;

    }

    public String getTitle_view() {
        return title_view;
    }

    public void setTitle_view(String title_view) {
        this.title_view = title_view;
    }

    public String getSubTitle_view() {
        return subTitle_view;
    }

    public void setSubTitle_view(String subTitle_view) {
        this.subTitle_view = subTitle_view;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return title_view;
    }
}
