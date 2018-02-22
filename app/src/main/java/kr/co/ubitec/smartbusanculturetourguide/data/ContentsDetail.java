package kr.co.ubitec.smartbusanculturetourguide.data;

/**
 * Created by Ubitec on 2018-01-09.
 */

public class ContentsDetail {
    int No;
    int Lang;
    String Contents;
    String Address_ko;
    String Address_en;

    public ContentsDetail(){
    }

    public ContentsDetail(int No, int Lang, String Contents, String Address_ko, String Address_en){
        this.No = No;
        this.Lang  = Lang;
        this.Contents = Contents;
        this.Address_ko = Address_ko;
        this.Address_en = Address_en;
    }

    public String getAddress_ko() {
        return this.Address_ko;
    }

    public void setAddress_ko(String address_ko) {
        this.Address_ko = address_ko;
    }

    public String getAddress_en() {
        return this.Address_en;
    }

    public void setAddress_en(String address_en) {
        this.Address_en = address_en;
    }

    public int getNo() {
        return this.No;
    }

    public void setNo(int no) {
        this.No = no;
    }

    public int getLang() {
        return this.Lang;
    }

    public void setLang(int lang) {
        this.Lang = lang;
    }

    public String getContents() {
        return this.Contents;
    }

    public void setContents(String contents) {
        this.Contents = contents;
    }


}
