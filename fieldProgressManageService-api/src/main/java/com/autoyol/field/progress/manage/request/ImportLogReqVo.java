package com.autoyol.field.progress.manage.request;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/20
 */
public class ImportLogReqVo extends BasePage {
    private static final long serialVersionUID = 1167621494629726573L;
    /**
     * 导入业务类型
     */
    @AutoDocProperty(value = "业务类型[0:用户人员导入,1:城市等级导入,2:订单高级用户设定导入,3:用户考勤信息导入,4:门店信息批量新增导入,5:门店信息批量修改导入];4、5位门店同一页面的日志,传4或者5即可;返回该页面的业务类型为4、5时相关页面做处理;" +
            "6:供应商批量修改导入", required = true)
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}