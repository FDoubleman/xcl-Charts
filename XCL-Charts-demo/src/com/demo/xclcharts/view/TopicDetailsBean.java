package com.demo.xclcharts.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fmm on 2016/3/7.
 */
public class TopicDetailsBean implements Serializable {


    //@SerializedName("stid")
    private String optionID;//试题ID
    //@SerializedName("stzql")
    private String accuracy;//试题正确率
    //@SerializedName("stxx")
    private ArrayList<OptionRateBean> optionRightRateList;//试题选项占比

    public TopicDetailsBean(String optionID, String accuracy, ArrayList<OptionRateBean> optionRightRateList) {
        this.optionID = optionID;
        this.accuracy = accuracy;
        this.optionRightRateList = optionRightRateList;
    }



    public String getOptionID() {
        return optionID;
    }

    public void setOptionID(String optionID) {
        this.optionID = optionID;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public ArrayList<OptionRateBean> getOptionRightRateList() {
        return optionRightRateList;
    }

    public void setOptionRightRateList(ArrayList<OptionRateBean> optionRightRateList) {
        this.optionRightRateList = optionRightRateList;
    }

    @Override
    public String toString() {
        return "TopicDetailsBean{" +
                "optionID='" + optionID + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", optionRightRateList=" + optionRightRateList +
                '}';
    }
}
