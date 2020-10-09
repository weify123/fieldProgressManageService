package com.autoyol.field.progress.manage.entity;

import java.util.Date;
import java.util.List;

public class DispatchEntity {

    private List<String> pickDeliverOrderNoList;

    private Integer serverTypeKey;

    private Date pickTime;

    public List<String> getPickDeliverOrderNoList() {
        return pickDeliverOrderNoList;
    }

    public void setPickDeliverOrderNoList(List<String> pickDeliverOrderNoList) {
        this.pickDeliverOrderNoList = pickDeliverOrderNoList;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }
}
