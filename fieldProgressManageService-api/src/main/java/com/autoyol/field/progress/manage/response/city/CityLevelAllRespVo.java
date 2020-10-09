package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/16
 */
public class CityLevelAllRespVo implements Serializable {
    private static final long serialVersionUID = 7494009011876066553L;
    /**
     * 
     */
    @AutoDocProperty("城市导航栏列表")
    private List<CityLevelNavBarVo> navBarVos;

    /**
     * 城市首字符
     */
    @AutoDocProperty("城市首字符列表")
    private List<CityPrefixVo> prefixVos;


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