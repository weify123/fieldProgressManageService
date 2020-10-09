package com.autoyol.field.progress.manage.response.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.response.city.CityLevelNavBarVo;
import com.autoyol.field.progress.manage.response.city.CityPrefixVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class SupplierRoleVo implements Serializable {
    private static final long serialVersionUID = 5299534177336266813L;

    @AutoDocProperty("角色id;0，非供应商;1，供应商")
    private Integer roleId;

    @AutoDocProperty("服务公司id")
    private Integer companyId;

    @AutoDocProperty(value = "公司一级分类")
    private String firstCategory;

    @AutoDocProperty(value = "公司二级分类")
    private String secondCategory;

    @AutoDocProperty("城市导航栏列表")
    private List<CityLevelNavBarVo> navBarVos;

    @AutoDocProperty("城市首字符列表")
    private List<CityPrefixVo> prefixVos;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public List<CityLevelNavBarVo> getNavBarVos() {
        return navBarVos;
    }

    public void setNavBarVos(List<CityLevelNavBarVo> navBarVos) {
        this.navBarVos = navBarVos;
    }

    public List<CityPrefixVo> getPrefixVos() {
        return prefixVos;
    }

    public void setPrefixVos(List<CityPrefixVo> prefixVos) {
        this.prefixVos = prefixVos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
