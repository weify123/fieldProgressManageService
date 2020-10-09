package com.autoyol.field.progress.manage.entity;

public class TransOrderInfoEntity extends TransOrderInfoLogEntity{

    /**
     * 编辑状态:0:未提交1,已提交
     */
    private Integer editStatus;

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }
}
