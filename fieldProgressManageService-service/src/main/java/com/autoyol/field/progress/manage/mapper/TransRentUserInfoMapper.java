package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.TransRentUserInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransRentUserInfoMapper {

    TransRentUserInfoEntity selectByOrderNo(@Param("orderNo") String orderNo);

    List<TransRentUserInfoEntity> queryByOrderNoList(@Param("orderNoList") List<String> orderNoList);;

    Integer queryMaxTackBackTimeByRenterNo(@Param("renterNo") String renterNo);

    int updateByOrderNo(TransRentUserInfoEntity record);

    int insertSelective(TransRentUserInfoEntity record);
}