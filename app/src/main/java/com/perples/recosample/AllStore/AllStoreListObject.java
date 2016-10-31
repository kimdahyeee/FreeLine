package com.perples.recosample.AllStore;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreListObject {

    public static final String SERVER_URL = "http://cslab2.kku.ac.kr/~201341307/images/";
    private String name;
    private String image;
    private String category;
    private String tel;
    private String introduction;
    private String location;

    public String getName() { return name; }
    public void setName(String name){ this.name = name; }

    public String getImage(){ return image; }
    public void setImage(String image){ this.image = SERVER_URL + image.replace("\\","");}

    public String getCategory(){return category;}
    public void setCategory(String category){this.category = category;}

    public String getTel(){return tel; }
    public void setTel(String tel){ this.tel = tel; }

    public String getIntroduction(){ return introduction; }
    public void setIntroduction(String introduction){ this.introduction = introduction;}

    public String getLocation() { return location; }
    public void setLocation(String location){ this.location = location; }
}
