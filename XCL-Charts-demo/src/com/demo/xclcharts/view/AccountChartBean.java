package com.demo.xclcharts.view;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fmm on 2016/3/7.
 */
public class AccountChartBean implements Serializable {


   // @SerializedName("stxx")
    private String mostoption;//最多选项个数 4 "a,b,c,d,e"
    //@SerializedName("stnum")
    private String topicnum;//试题个数

    //@SerializedName("stxxList")
    private ArrayList<TopicDetailsBean> topiclist;//试题列表

    public AccountChartBean() {
    }

    public AccountChartBean(String mostoption, String topicnum, ArrayList<TopicDetailsBean> topiclist) {
        this.mostoption = mostoption;
        this.topicnum = topicnum;
        this.topiclist = topiclist;
    }

    public String getMostoption() {
        return mostoption;
    }

    public void setMostoption(String mostoption) {
        this.mostoption = mostoption;
    }

    public String getTopicnum() {
        return topicnum;
    }
    public void setTopicnum(String topicnum) {
        this.topicnum = topicnum;
    }

    public ArrayList<TopicDetailsBean> getTopiclist() {
        return topiclist;
    }

    public void setTopiclist(ArrayList<TopicDetailsBean> topiclist) {
        this.topiclist = topiclist;
    }

    //获得做多选项个数
    public int getMostOptionNum(){
        String[] StrArry = getMostoption().split(",");
        return StrArry.length;
    }

    @Override
    public String toString() {
        return "AccountChartBean{" +
                "mostoption='" + mostoption + '\'' +
                ", topicnum='" + topicnum + '\'' +
                ", topiclist=" + topiclist +
                '}';
    }
}
