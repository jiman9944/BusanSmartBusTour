package kr.co.ubitec.smartbusanculturetourguide.data;

/**
 * Created by Ubitec on 2018-01-10.
 */

public class ContentsPicture {
    int Contents_No;
    String Title;
    String Url_Addr;
    int Picture_No;

    public ContentsPicture(int Contents_No, String Title, String Url_Addr, int Picture_No){
        this.Contents_No = Contents_No;
        this.Title = Title;
        this.Url_Addr = Url_Addr;
        this.Picture_No = Picture_No;
    }

    public ContentsPicture() {

    }

    public int getContents_No() {
        return this.Contents_No;
    }

    public void setContents_No(int contents_No) {
        this.Contents_No = contents_No;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getUrl_Addr() {
        return this.Url_Addr;
    }

    public void setUrl_Addr(String url_Addr) {
        this.Url_Addr = url_Addr;
    }

    public int getPicture_No() {
        return this.Picture_No;
    }

    public void setPicture_No(int picture_No) {
        this.Picture_No = picture_No;
    }



}
