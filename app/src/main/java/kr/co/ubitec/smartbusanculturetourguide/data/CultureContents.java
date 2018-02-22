package kr.co.ubitec.smartbusanculturetourguide.data;

/**
 * Created by Administrator on 2017-09-21.
 */

public class CultureContents {
    int No;
    String district;
    String name;
    String main_image;


    public CultureContents() {
    }

    public CultureContents(int No, String district, String name, String main_image) {
        this.No = No;
        this.district = district;
        this.name = name;
        this.main_image = main_image;
    }

    @Override
    public String toString() {
        return "CultureContents{" +
                "No=" + No +
                ",district=" + district +
                ",title_kor=" + name+
                ",main_image=" + main_image +
                '}';
    }

    public int getNo() {
        return No;
    }

    public void setNo(int No) {
        this.No = No;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
