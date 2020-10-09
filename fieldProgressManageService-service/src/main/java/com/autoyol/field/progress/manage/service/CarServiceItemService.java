package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.CarServiceItemEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.mapper.CarServiceItemEntityMapper;
import com.autoyol.field.progress.manage.request.AddCarServiceItemReqVO;
import com.autoyol.field.progress.manage.request.UpdateCarServiceItemReqVO;
import com.autoyol.field.progress.manage.request.car.QueryCarServiceItemReqVo;
import com.autoyol.field.progress.manage.response.QueryCarServiceItemExcelVO;
import com.autoyol.field.progress.manage.response.QueryCarServiceItemRespVO;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.buildTypeList;
import static com.autoyol.field.progress.manage.util.OprUtil.getLabelByCode;

@Service
public class CarServiceItemService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CarServiceItemEntityMapper carServiceItemEntityMapper;

    @Resource
    DictCache dictCache;

    private static final String FILE_NAME = "车辆服务项目数据";

    public int addCarServiceItem(AddCarServiceItemReqVO serviceItemReqVO) {
        CarServiceItemEntity carServiceItemsEntity = new CarServiceItemEntity();
        BeanUtils.copyProperties(serviceItemReqVO, carServiceItemsEntity);
        int count = carServiceItemEntityMapper.countByEntity(carServiceItemsEntity);
        if (count > 0) {
            return NEG_ONE;
        }
        carServiceItemsEntity.setServiceType(serviceItemReqVO.getServiceTypeKey());
        carServiceItemsEntity.setIsEffective(serviceItemReqVO.getIsEffectiveKey());
        carServiceItemsEntity.setCreateOp(serviceItemReqVO.getHandleUser());
        return carServiceItemEntityMapper.insert(carServiceItemsEntity);
    }

    public List<QueryCarServiceItemRespVO> queryCarServiceItemsByPage(QueryCarServiceItemReqVo queryCarServiceItemReqVO) throws Exception {
        return queryCarServiceItems(queryCarServiceItemReqVO);
    }

    public QueryCarServiceItemRespVO queryCarServiceItemById(int id) throws Exception {
        CarServiceItemEntity carServiceItemEntity = carServiceItemEntityMapper.selectByPrimaryKey(id);
        QueryCarServiceItemRespVO queryCarServiceItemRespVO = new QueryCarServiceItemRespVO();
        ConvertBeanUtil.copyProperties(queryCarServiceItemRespVO, carServiceItemEntity);
        List<String> typeList = buildTypeList(SERVICE_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        queryCarServiceItemRespVO.setIsEffectiveKey(carServiceItemEntity.getIsEffective());
        queryCarServiceItemRespVO.setIsEffectiveVal(getLabelByCode(map.get(EFFECTIVE_TYPE), String.valueOf(carServiceItemEntity.getIsEffective())));
        queryCarServiceItemRespVO.setServiceTypeKey(carServiceItemEntity.getServiceType());
        queryCarServiceItemRespVO.setServiceTypeVal(getLabelByCode(map.get(SERVICE_TYPE), String.valueOf(carServiceItemEntity.getServiceType())));
        return queryCarServiceItemRespVO;
    }

    public int updateCarServiceItem(UpdateCarServiceItemReqVO updateCarServiceItemReqVO) {
        CarServiceItemEntity carServiceItemsEntity = new CarServiceItemEntity();
        BeanUtils.copyProperties(updateCarServiceItemReqVO, carServiceItemsEntity);
        int count = carServiceItemEntityMapper.countByEntity(carServiceItemsEntity);
        if (count > 0) {
            return NEG_ONE;
        }
        carServiceItemsEntity.setServiceType(updateCarServiceItemReqVO.getServiceTypeKey());
        carServiceItemsEntity.setIsEffective(updateCarServiceItemReqVO.getIsEffectiveKey());
        carServiceItemsEntity.setUpdateOp(updateCarServiceItemReqVO.getHandleUser());
        return carServiceItemEntityMapper.updateByPrimaryKey(carServiceItemsEntity);
    }

    public List<QueryCarServiceItemExcelVO> exportCarServiceItems(QueryCarServiceItemReqVo queryCarServiceItemReqVO) throws Exception {
        List<CarServiceItemEntity> entities = carServiceItemEntityMapper.queryCarServiceItems(queryCarServiceItemReqVO);
        List<QueryCarServiceItemExcelVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(entities)) {
            return list;
        }
        List<String> typeList = buildTypeList(SERVICE_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return entities.stream().sorted(Comparator.comparing(CarServiceItemEntity::getCreateTime).reversed()).map(entity -> {
            try {
                QueryCarServiceItemExcelVO queryCarServiceItemExcelVO = new QueryCarServiceItemExcelVO();
                ConvertBeanUtil.copyProperties(queryCarServiceItemExcelVO, entity);
                queryCarServiceItemExcelVO.setServiceType(getLabelByCode(map.get(SERVICE_TYPE), String.valueOf(entity.getServiceType())));
                queryCarServiceItemExcelVO.setIsEffective(getLabelByCode(map.get(EFFECTIVE_TYPE), String.valueOf(entity.getIsEffective())));
                return queryCarServiceItemExcelVO;
            } catch (Exception e) {
                logger.error("复制错误,{}", e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<QueryCarServiceItemRespVO> queryCarServiceItems(QueryCarServiceItemReqVo queryCarServiceItemReqVO) throws Exception {
        List<CarServiceItemEntity> entities = carServiceItemEntityMapper.queryCarServiceItems(queryCarServiceItemReqVO);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }
        List<String> typeList = buildTypeList(SERVICE_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return entities.stream().sorted(Comparator.comparing(CarServiceItemEntity::getCreateTime).reversed())
                .map(entity -> {
                    QueryCarServiceItemRespVO respVO = new QueryCarServiceItemRespVO();
                    try {
                        ConvertBeanUtil.copyProperties(respVO, entity);
                        respVO.setIsEffectiveKey(entity.getIsEffective());
                        respVO.setIsEffectiveVal(getLabelByCode(map.get(EFFECTIVE_TYPE), String.valueOf(entity.getIsEffective())));
                        respVO.setServiceTypeKey(entity.getServiceType());
                        respVO.setServiceTypeVal(getLabelByCode(map.get(SERVICE_TYPE), String.valueOf(entity.getServiceType())));
                        return respVO;
                    } catch (Exception e) {
                        logger.error("复制错误,{}", e);
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
