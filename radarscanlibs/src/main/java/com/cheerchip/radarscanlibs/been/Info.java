package com.cheerchip.radarscanlibs.been;

/**
 * Created by Mr_immortalZ on 2016/5/3.
 * email : mr_immortalz@qq.com
 */
public class Info {
    private int portraitId;//头像id
    private String name;//名字
    private String age;//年龄
    private boolean quipment;//false为男，true为女
    private float distance;//距离

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isQuipment() {
        return quipment;
    }

    public void setQuipment(boolean quipment) {
        this.quipment = quipment;
    }
}
