package com.demo.xclcharts.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fmm on 2016/3/7.
 */
public class OptionRateBean implements Serializable {


    //@SerializedName("xx")
    private String option;//选项
    //@SerializedName("zb")
    private String optionRatio;//选项占比

    public OptionRateBean(String option, String optionRatio) {
        this.option = option;
        this.optionRatio = optionRatio;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOptionRatio() {
        return optionRatio;
    }

    public void setOptionRatio(String optionRatio) {
        this.optionRatio = optionRatio;
    }

    @Override
    public String toString() {
        return "OptionRateBean{" +
                "option='" + option + '\'' +
                ", optionRatio='" + optionRatio + '\'' +
                '}';
    }
}

