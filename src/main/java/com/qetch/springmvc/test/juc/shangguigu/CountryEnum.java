package com.qetch.springmvc.test.juc.shangguigu;

public enum CountryEnum {
    HAN(1, "韩"),
    WEI(2, "魏"),
    ZHAO(3, "赵"),
    QI(4, "齐"),
    CHU(5, "楚"),
    YAN(6, "燕");

    private Integer retCode;
    private String retMsg;

    CountryEnum(Integer retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public static CountryEnum forEachCountryEnum(Integer index) {
        for (CountryEnum element : values()) {
            if (element.getRetCode() == index) {
                return element;
            }
        }
        return null;
    }
}
