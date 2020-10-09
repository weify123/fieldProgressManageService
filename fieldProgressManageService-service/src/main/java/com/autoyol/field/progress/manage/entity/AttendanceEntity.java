package com.autoyol.field.progress.manage.entity;

import java.util.Date;
import java.util.Objects;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/23
 */
public class AttendanceEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 人员id
     */
    private Integer userId;

    /**
     * 人员id
     */
    private Integer zoneCode;

    /**
     * 区间描述:如[上午],[下午]
     */
    private String zoneDesc;

    /**
     * 考勤开始时间
     */
    private Date startTime;

    /**
     * 考勤结束时间
     */
    private Date endTime;

    /**
     * 考勤状态:0,出勤;1,休息;2,年假;3,事假;4,病假;5,调休6,加班
     */
    private String status;

    /**
     * 考勤标记:0,出勤;1,休息
     */
    private Integer mark;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否删除:0:正常;1,已删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private String createOp;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String updateOp;

    /**
     * 
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(Integer zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc == null ? null : zoneDesc.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp == null ? null : createOp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp == null ? null : updateOp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttendanceEntity entity = (AttendanceEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(userId, entity.userId) &&
                Objects.equals(zoneCode, entity.zoneCode) &&
                Objects.equals(startTime, entity.startTime) &&
                Objects.equals(status, entity.status) &&
                Objects.equals(mark, entity.mark) &&
                Objects.equals(memo, entity.memo) &&
                Objects.equals(isDelete, entity.isDelete) &&
                Objects.equals(createOp, entity.createOp) &&
                Objects.equals(createTime, entity.createTime) &&
                Objects.equals(updateOp, entity.updateOp) &&
                Objects.equals(updateTime, entity.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, zoneCode, startTime, status, mark, memo, isDelete, createOp, createTime, updateOp, updateTime);
    }
}