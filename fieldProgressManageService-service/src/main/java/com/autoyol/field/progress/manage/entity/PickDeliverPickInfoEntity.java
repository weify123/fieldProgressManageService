package com.autoyol.field.progress.manage.entity;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverPickInfoEntity extends PickDeliverPickInfoLogEntity{

    /**
     * 编辑状态:0:未提交,1:已提交
     */
    private Integer editStatus;

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }
}