package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.DispatchEntity;
import com.autoyol.field.progress.manage.entity.PageCondTackBackEntity;
import com.autoyol.field.progress.manage.entity.PickDeliverOrderInfoEntity;
import com.autoyol.field.progress.manage.request.tackback.TackBackPageReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverOrderInfoMapper {

    List<PickDeliverOrderInfoEntity> queryWaitDispatch(DispatchEntity record);

    List<PageCondTackBackEntity> queryByCond(TackBackPageReqVo record);

    List<PickDeliverOrderInfoEntity> selectByCond(PickDeliverOrderInfoEntity record);

    int queryCountByCond(TackBackPageReqVo record);

    int batchUpdate(@Param("list") List<PickDeliverOrderInfoEntity> list);

    int batchBackUpdate(@Param("list") List<PickDeliverOrderInfoEntity> list);

    int transCancelBatchUpdate(@Param("list") List<PickDeliverOrderInfoEntity> list);

    int updateSelectById(PickDeliverOrderInfoEntity record);

    int updateByCond(PickDeliverOrderInfoEntity record);

    int insertSelective(PickDeliverOrderInfoEntity record);
}