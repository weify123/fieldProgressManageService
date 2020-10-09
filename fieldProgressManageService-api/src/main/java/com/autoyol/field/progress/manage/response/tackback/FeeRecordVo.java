package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

public class FeeRecordVo implements Serializable {
    private static final long serialVersionUID = 6523560672868360815L;

    @AutoDocProperty(value = "报销类型code")
    private String expenseType;

    @AutoDocProperty(value = "费用类型,字典根据报销类型取不同的类型")
    private Integer feeTypeKey;

    @AutoDocProperty(value = "费用类型")
    private String feeTypeVal;

    @AutoDocProperty(value = "费用code")
    private String attrCode;

    @AutoDocProperty(value = "费用名称")
    private String attrName;

    @AutoDocProperty(value = "费用")
    @Digits(integer = 8, fraction = 0, message = "只能为数字")
    private BigDecimal feeValue;

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Integer getFeeTypeKey() {
        return feeTypeKey;
    }

    public void setFeeTypeKey(Integer feeTypeKey) {
        this.feeTypeKey = feeTypeKey;
    }

    public String getFeeTypeVal() {
        return feeTypeVal;
    }

    public void setFeeTypeVal(String feeTypeVal) {
        this.feeTypeVal = feeTypeVal;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
