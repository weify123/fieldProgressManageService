package com.autoyol.field.progress.manage.entity;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverFeeWithMemoEntity extends PickDeliverFeeEntity {
    /**
     * 费用备注
     */
    private String feeMemo;

    /**
     * 报备备注
     */
    private String reportMemo;

    /**
     * 罚款备注
     */
    private String fineMemo;

    public String getFeeMemo() {
        return feeMemo;
    }

    public void setFeeMemo(String feeMemo) {
        this.feeMemo = feeMemo == null ? null : feeMemo.trim();
    }

    public String getReportMemo() {
        return reportMemo;
    }

    public void setReportMemo(String reportMemo) {
        this.reportMemo = reportMemo == null ? null : reportMemo.trim();
    }

    public String getFineMemo() {
        return fineMemo;
    }

    public void setFineMemo(String fineMemo) {
        this.fineMemo = fineMemo == null ? null : fineMemo.trim();
    }
}