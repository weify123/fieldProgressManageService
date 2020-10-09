package com.autoyol.field.progress.manage.service.sqlserver;

import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.cache.AttrLabelCache;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.config.dynamic.DynamicSource;
import com.autoyol.field.progress.manage.convertsqlserver.TackBackConvertEntity;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.entity.sqlserver.*;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.mapper.sqlserver.*;
import com.autoyol.field.progress.manage.request.tackback.TackBackPageReqVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.parse;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class SqlServerTackBackService {

    @Resource
    AotuInfor005Mapper aotuInfor005EntityMapper;

    @Resource
    AutoDictDataMapper autoDictDataMapper;

    @Resource
    AutoOther001Mapper autoOther001EntityMapper;

    @Resource
    SyspUserMapper syspUserMapper;

    @Resource
    AutoFlow005Mapper autoFlow005Mapper;

    @Resource
    AutoOrdtraposinfoMapper autoOrdtraposinfoMapper;

    @Resource
    AutoCompinfoMapper autoCompinfoMapper;

    @Resource
    AutoSyncinfoMapper autoSyncinfoMapper;

    @Resource
    AutoRecordInfoMapper autoRecordInfoMapper;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransVehicleInfoMapper transVehicleInfoMapper;

    @Resource
    TransRentUserInfoMapper transRentUserInfoMapper;

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverPickInfoMapper pickDeliverPickInfoMapper;

    @Resource
    PickDeliverDeliverInfoMapper pickDeliverDeliverInfoMapper;

    @Resource
    PickDeliverFeeMapper pickDeliverFeeMapper;

    @Resource
    PickDeliverFileMapper pickDeliverFileMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoRecordInfoEntity> selectByCond(String transOrderNo, String serverType) {
        AutoRecordInfoEntity recordInfoEntity = new AutoRecordInfoEntity();
        recordInfoEntity.setAutoRedOrderno(transOrderNo);
        recordInfoEntity.setAutoRedStype(getObjOrDefault(getAutoDictDataEntities(sqlServer_service_type, serverType), x -> x.get(0).getDdtCode(), null));
        return autoRecordInfoMapper.selectByCond(recordInfoEntity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AutoSyncinfoEntity selectSyncInfoByMsgId(String msgId) {
        return autoSyncinfoMapper.selectByMsgId(msgId);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AutoDictDataEntity getDictData(String code, String ddtCode) {
        AutoDictDataEntity autoDictDataEntity = new AutoDictDataEntity();
        autoDictDataEntity.setDtpCode(code);
        autoDictDataEntity.setDdtCode(ddtCode);
        return autoDictDataMapper.selectDictByCond(autoDictDataEntity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoCompinfoEntity> querySupplierCompany() {
        return autoCompinfoMapper.selectAll();
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AutoCompinfoEntity queryCompanyById(Long id) {
        return autoCompinfoMapper.selectByPrimaryKey(id);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AutoCompinfoEntity queryCompanyBySecondName(String name) {
        return autoCompinfoMapper.queryCompanyBySecondName(name);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public String selectCurByCond(Long id) {
        return autoOrdtraposinfoMapper.selectCurByCond(id);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AutoOther001Entity getTackBackPerson(Long id) {
        return autoOther001EntityMapper.selectByPrimaryKey(id);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoOther001Entity> getTackBackPersonByName(String name) {
        return autoOther001EntityMapper.selectByByName(name);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public int countTackBackPersonByCond(AutoOther001Entity entity) {
        return autoOther001EntityMapper.countByCond(entity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoOther001Entity> getTackBackPersonByCond(AutoOther001Entity entity) {
        return autoOther001EntityMapper.selectByCond(entity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoFlow005Entity> queryFlow(Long id, String str1, String str2, String str3, String str6, String str9, String flowState) {
        AutoFlow005Entity autoFlow005Entity = new AutoFlow005Entity();
        autoFlow005Entity.setAutoFw05Billid(id);
        autoFlow005Entity.setAutoFw05C001(str1);
        autoFlow005Entity.setAutoFw05C002(str2);
        autoFlow005Entity.setAutoFw05C003(str3);
        autoFlow005Entity.setAutoFw05C006(str6);
        autoFlow005Entity.setAutoFw05C009(str9);
        autoFlow005Entity.setPubFlowstate(flowState);
        return autoFlow005Mapper.selectByCond(autoFlow005Entity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public int queryCountByCond(TackBackPageReqVo reqVo, String companyName, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        return aotuInfor005EntityMapper.queryCountByCond(buildReqEntity(reqVo, companyName, dictMap));
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AotuInfor005Entity> queryListByCond(TackBackPageReqVo reqVo, String companyName, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        return aotuInfor005EntityMapper.queryListByCond(buildReqEntity(reqVo, companyName, dictMap));
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public SyspUserEntity selectUserById(Long userId) {
        return syspUserMapper.selectByPrimaryKey(userId);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<SyspUserEntity> selectSupplierUser() {
        return syspUserMapper.selectSupplierUser();
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AotuInfor005Entity selectTransByCond(String transOrderNo) {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setAutoInf05C050(transOrderNo);
        return Optional.ofNullable(aotuInfor005EntityMapper.selectTransByCond(entity)).flatMap(ls -> ls.stream().findFirst()).orElse(null);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public AotuInfor005Entity selectSingleByCond(String tackBackOrderNo, String serverType, String transOrderNo, Date pickTime, String newUserId) {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setAutoInf05C050(transOrderNo);
        entity.setAutoInf05C073(Long.parseLong(tackBackOrderNo));

        AutoDictDataEntity autoDictDataEntity = new AutoDictDataEntity();
        autoDictDataEntity.setDtpCode(sqlServer_serverType_code);
        autoDictDataEntity.setName(serverType);
        List<AutoDictDataEntity> dataEntityList = autoDictDataMapper.selectByCond(autoDictDataEntity);
        entity.setAutoInf05C002(Optional.ofNullable(dataEntityList).flatMap(x -> x.stream().map(AutoDictDataEntity::getCode).findFirst()).orElse(null));
        entity.setAutoInf05C004(pickTime);
        if (StringUtils.isNotEmpty(newUserId)) {
            SyspUserEntity userEntity = syspUserMapper.selectByPrimaryKey(Long.parseLong(newUserId));
            Optional.ofNullable(userEntity).ifPresent(u -> {
                entity.setAutoInf05Sercompany(Optional.ofNullable(u.getSyspUseSercompany()).filter(StringUtils::isNotEmpty).orElse(null));
            });
        }
        return Optional.ofNullable(aotuInfor005EntityMapper.selectSingleByCond(entity)).flatMap(ls -> ls.stream().findFirst()).orElse(null);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AotuInfor005Entity> selectListByCond(List<String> tackBackOrderNoList, String serverType, String transOrderNo, Date pickTime, String newUserId) {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setAutoInf05C050(transOrderNo);
        entity.setTackBackOrderNoList(tackBackOrderNoList);

        AutoDictDataEntity autoDictDataEntity = new AutoDictDataEntity();
        autoDictDataEntity.setDtpCode(sqlServer_serverType_code);
        autoDictDataEntity.setName(serverType);
        List<AutoDictDataEntity> dataEntityList = autoDictDataMapper.selectByCond(autoDictDataEntity);
        entity.setAutoInf05C002(Optional.ofNullable(dataEntityList).flatMap(x -> x.stream().map(AutoDictDataEntity::getCode).findFirst()).orElse(null));
        entity.setAutoInf05C004(pickTime);
        if (StringUtils.isNotEmpty(newUserId)) {
            SyspUserEntity userEntity = syspUserMapper.selectByPrimaryKey(Long.parseLong(newUserId));
            Optional.ofNullable(userEntity).ifPresent(u -> {
                entity.setAutoInf05Sercompany(Optional.ofNullable(u.getSyspUseSercompany()).filter(StringUtils::isNotEmpty).orElse(null));
            });
        }
        return getObjOrDefault(aotuInfor005EntityMapper.selectListByCond(entity), Function.identity(), null);
    }

    private AotuInfor005Entity buildReqEntity(TackBackPageReqVo reqVo, String companyName, Map<String, List<SysDictEntity>> map) throws Exception {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setIsUploadRecord(reqVo.getIsUploadRecord());
        entity.setAutoInf05C050(reqVo.getTransOrderNo());
        entity.setAutoInf05C073(Optional.ofNullable(reqVo.getPickDeliverOrderNo()).filter(StringUtils::isNotEmpty).map(Long::parseLong).orElse(null));

        List<AutoDictDataEntity> dataEntityList = getAutoDictDataEntities(sqlServer_serverType_code,
                getLabelByCode(map.get(FLOW_SERVER_TYPE), String.valueOf(reqVo.getServerTypeKey())));
        entity.setAutoInf05C002(Optional.ofNullable(dataEntityList).flatMap(l -> l.stream().map(AutoDictDataEntity::getCode).findFirst()).orElse(null));
        entity.setAutoInf05C004(parse(reqVo.getPickTimeYear() + Optional.ofNullable(reqVo.getPickTimeMonthDay()).orElse("0101"),
                DATE_YYYY_MM_DD_CONTACT));
        entity.setPickTimeMonthDay(reqVo.getPickTimeMonthDay());

        if (StringUtils.isNotEmpty(companyName)) {
            AutoCompinfoEntity compinfoEntity = autoCompinfoMapper.queryCompanyBySecondName(companyName);
            entity.setAutoInf05Sercompany(getObjOrDefault(compinfoEntity, AutoCompinfoEntity::getAutoCpinfoComp2, null));
        }

        if (StringUtils.isNotEmpty(reqVo.getUserName())) {
            List<Long> userIdList = Optional.ofNullable(getTackBackPersonByName(reqVo.getUserName()))
                    .map(ls -> ls.stream().map(AutoOther001Entity::getAutoOt01Id)
                            .collect(Collectors.toList())).orElse(null);
            entity.setUserIdList(userIdList);
        }
        entity.setAutoInf05C023(reqVo.getUserPhone());
        entity.setAutoInf05C006(reqVo.getVehicleNo());
        entity.setAutoInf05C071(getLabelByCode(map.get(VEHICLE_TYPE), String.valueOf(reqVo.getVehicleTypeKey())));
        entity.setAutoInf05Source(getObjOrDefault(getLabel1ByCode(map.get(TRANS_SOURCE), String.valueOf(reqVo.getTransSourceKey())), Function.identity(), null));
        entity.setAutoInf05C072(getLabelByCode(map.get(TRANS_SCENE_SOURCE), String.valueOf(reqVo.getSceneSourceKey())));
        entity.setAutoInf05Offlineordertype(getLabel1ByCode(map.get(OFFLINE_ORDER_TYPE), String.valueOf(reqVo.getOfflineOrderTypeKey())));

        List<AutoDictDataEntity> dictList = getAutoDictDataEntities(sqlServer_city_code, reqVo.getCityNameStr());
        List<String> cityList = Optional.ofNullable(dictList).filter(CollectionUtils::isNotEmpty).map(d -> d.stream().map(AutoDictDataEntity::getCode)
                .collect(Collectors.toList())).orElse(null);
        entity.setCityList(cityList);
        entity.setScheduleStatus(reqVo.getScheduleStatusKey());
        entity.setPageSize(reqVo.getPageSize());
        entity.setPage(reqVo.getStart());
        return entity;
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AutoDictDataEntity> getAutoDictDataEntities(String code, String name) {
        if (StringUtils.isEmpty(name)) {
            return Collections.EMPTY_LIST;
        }

        AutoDictDataEntity autoDictDataEntity = new AutoDictDataEntity();
        autoDictDataEntity.setDtpCode(code);
        if (StringUtils.contains(name, SPLIT_COMMA)){
            autoDictDataEntity.setNameList(getList(name, SPLIT_COMMA, Function.identity()));
        }else {
            autoDictDataEntity.setName(name);
        }
        return autoDictDataMapper.selectByCond(autoDictDataEntity);
    }

    @Transactional
    public void saveLocal(TransOrderInfoEntity transOrderInfoEntity, TransVehicleInfoEntity transVehicleInfoEntity, TransRentUserInfoEntity transUserInfoEntity,
                          PickDeliverOrderInfoEntity tackBackOrder, PickDeliverScheduleInfoEntity scheduleInfo, PickDeliverPickInfoEntity pickInfo,
                          PickDeliverDeliverInfoEntity deliverInfo, PickDeliverFeeWithMemoEntity feeMemoInfo,
                          AotuInfor005Entity entity, Map<String, List<SysDictEntity>> dictMap) throws Exception {

        if (Objects.isNull(transOrderInfoMapper.queryByOrderNo(transOrderInfoEntity.getOrderNo()))) {
            transOrderInfoMapper.insertSelective(transOrderInfoEntity);
        } else {
            transOrderInfoMapper.updateByOrderNo(transOrderInfoEntity);
        }

        if (Objects.isNull(transVehicleInfoMapper.queryByOrderNo(transVehicleInfoEntity.getOrderNo()))) {
            transVehicleInfoMapper.insertSelective(transVehicleInfoEntity);
        } else {
            transVehicleInfoMapper.updateByOrderNo(transVehicleInfoEntity);
        }

        if (Objects.isNull(transRentUserInfoMapper.selectByOrderNo(transUserInfoEntity.getOrderNo()))) {
            transRentUserInfoMapper.insertSelective(transUserInfoEntity);
        } else {
            transRentUserInfoMapper.updateByOrderNo(transUserInfoEntity);
        }

        if (CollectionUtils.isEmpty(pickDeliverOrderInfoMapper.selectByCond(tackBackOrder))) {
            pickDeliverOrderInfoMapper.insertSelective(tackBackOrder);
        } else {
            pickDeliverOrderInfoMapper.updateByCond(tackBackOrder);
        }

        if (Objects.isNull(pickDeliverScheduleInfoMapper.selectByCond(scheduleInfo))) {
            pickDeliverScheduleInfoMapper.insertSelective(scheduleInfo);
        } else {
            pickDeliverScheduleInfoMapper.updateByCond(scheduleInfo);
        }

        if (Objects.isNull(pickDeliverPickInfoMapper.selectByCond(pickInfo))) {
            pickDeliverPickInfoMapper.insertSelective(pickInfo);
        } else {
            pickDeliverPickInfoMapper.updateByTackBackOrderNo(pickInfo);
        }

        if (Objects.isNull(pickDeliverDeliverInfoMapper.selectByCond(deliverInfo))) {
            pickDeliverDeliverInfoMapper.insertSelective(deliverInfo);
        } else {
            pickDeliverDeliverInfoMapper.updateByTackBackOrderNo(deliverInfo);
        }

        PickDeliverFeeWithMemoEntity fee = pickDeliverFeeMapper.selectByCond(feeMemoInfo);
        if (Objects.isNull(fee)) {
            pickDeliverFeeMapper.insertSelective(feeMemoInfo);
        } else {
            feeMemoInfo.setId(fee.getId());
            pickDeliverFeeMapper.updateByCond(feeMemoInfo);
        }

        List<AttrLabelEntity> labelEntities = attrLabelCache.getAttrListByBelongSysFromCache(tackBackOrder.getServiceType());
        List<FeeRecordEntity> feeRecordList = TackBackConvertEntity.convertFee(entity, this, dictMap,
                feeMemoInfo.getId(), labelEntities);
        List<FeeRecordEntity> feeRecordAddList = Lists.newArrayList();
        List<FeeRecordEntity> feeRecordUpdateList = Lists.newArrayList();
        feeRecordList.forEach(x -> {
            if (feeRecordMapper.countByCond(x) > 0) {
                feeRecordUpdateList.add(x);
            } else {
                feeRecordAddList.add(x);
            }
        });
        if (CollectionUtils.isNotEmpty(feeRecordAddList)) {
            feeRecordMapper.batchInsert(feeRecordAddList);
        }
        if (CollectionUtils.isNotEmpty(feeRecordUpdateList)) {
            feeRecordMapper.batchUpdate(feeRecordUpdateList);
        }

    }


    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public int selectHisCountByCond(Date pickTime) {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setAutoInf05C004(pickTime);
        return aotuInfor005EntityMapper.selectHisCountByCond(entity);
    }

    @DynamicSource(value = CacheConstraint.sqlServer_datasource)
    public List<AotuInfor005Entity> selectHisListByCond(Date pickTime, int page, int pageSize) {
        AotuInfor005Entity entity = new AotuInfor005Entity();
        entity.setAutoInf05C004(pickTime);
        entity.setPage(page);
        entity.setPageSize(pageSize);
        return aotuInfor005EntityMapper.selectHisListByCond(entity);
    }

    public void syncOrder(TransOrderInfoEntity transOrderInfoEntity, TransVehicleInfoEntity transVehicleInfoEntity, TransRentUserInfoEntity transUserInfoEntity,
                          PickDeliverOrderInfoEntity tackBackOrder, PickDeliverScheduleInfoEntity scheduleInfo, PickDeliverPickInfoEntity pickInfo,
                          PickDeliverDeliverInfoEntity deliverInfo, PickDeliverFeeWithMemoEntity feeMemoInfo,
                          AotuInfor005Entity entity, Map<String, List<SysDictEntity>> dictMap) throws Exception {

        if (Objects.isNull(transOrderInfoMapper.queryByOrderNo(transOrderInfoEntity.getOrderNo()))) {
            transOrderInfoMapper.insertSelective(transOrderInfoEntity);
        } else {
            transOrderInfoMapper.updateByOrderNo(transOrderInfoEntity);
        }

        if (Objects.isNull(transVehicleInfoMapper.queryByOrderNo(transVehicleInfoEntity.getOrderNo()))) {
            transVehicleInfoMapper.insertSelective(transVehicleInfoEntity);
        } else {
            transVehicleInfoMapper.updateByOrderNo(transVehicleInfoEntity);
        }

        if (Objects.isNull(transRentUserInfoMapper.selectByOrderNo(transUserInfoEntity.getOrderNo()))) {
            transRentUserInfoMapper.insertSelective(transUserInfoEntity);
        } else {
            transRentUserInfoMapper.updateByOrderNo(transUserInfoEntity);
        }

        pickDeliverOrderInfoMapper.insertSelective(tackBackOrder);

        pickDeliverScheduleInfoMapper.insertSelective(scheduleInfo);

        pickDeliverPickInfoMapper.insertSelective(pickInfo);

        pickDeliverDeliverInfoMapper.insertSelective(deliverInfo);

        pickDeliverFeeMapper.insertSelective(feeMemoInfo);

        String pic = entity.getPic();
        if (StringUtils.isNotEmpty(pic)){
            List<Map> list = JSON.parseObject(pic, List.class);
            pickDeliverFileMapper.batchInsert(list.stream().map(x -> {
                PickDeliverFileEntity fileEntity = new PickDeliverFileEntity();
                fileEntity.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
                fileEntity.setServiceType(tackBackOrder.getServiceType());
                fileEntity.setPickDeliverType(NEG_ONE);
                fileEntity.setFilePath( (String) x.get("picName"));
                return fileEntity;
            }).collect(Collectors.toList()));
        }


        List<AttrLabelEntity> labelEntities = attrLabelCache.getAttrListByBelongSysFromCache(tackBackOrder.getServiceType());
        List<FeeRecordEntity> feeRecordList = TackBackConvertEntity.convertFee(entity, this, dictMap,
                feeMemoInfo.getId(), labelEntities);
        feeRecordMapper.batchInsert(feeRecordList);

    }

    public static void main(String[] args) {
        String pic = "[{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto001569893687657/storage/emulated/0/DCIM/Screenshots/Screenshot_2019-10-01-07-58-15-799_com.rongyun.auto.png\",\"requestId\":\"5D92AD3E939900C7C8ED4E7A\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto141569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_46_43.jpg\",\"requestId\":\"5D92AD41939900C7C8ED65F3\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto261569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_43_18.jpg\",\"requestId\":\"5D92AD46939900C7C8ED8C21\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto391569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_32_03.jpg\",\"requestId\":\"5D92AD4B939900C7C8EDADA6\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto401569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_53.jpg\",\"requestId\":\"5D92AD4F939900C7C8EDD02C\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto591569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_47.jpg\",\"requestId\":\"5D92AD55939900C7C8EDF873\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto611569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_37.jpg\",\"requestId\":\"5D92AD59939900C7C8EE202D\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto741569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_29.jpg\",\"requestId\":\"5D92AD5E939900C7C8EE46E3\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto811569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_21.jpg\",\"requestId\":\"5D92AD63939900C7C8EE6AFC\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto901569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_31_02.jpg\",\"requestId\":\"5D92AD68939900C7C8EE9201\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1041569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_30_53.jpg\",\"requestId\":\"5D92AD6D939900C7C8EEBAE6\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1121569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_22_45.jpg\",\"requestId\":\"5D92AD71939900C7C8EED765\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1221569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_28_43.jpg\",\"requestId\":\"5D92AD76939900C7C8EEFB26\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1331569893687657/storage/emulated/0/DCIM/Camera/2019_10_01_07_30_17.jpg\",\"requestId\":\"5D92AD7B939900C7C8EF23E2\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1461569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_28_39.jpg\",\"requestId\":\"5D92AD7F939900C7C8EF3CA1\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1551569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_28_35.jpg\",\"requestId\":\"5D92AD84939900C7C8EF6038\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1651569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_28_01.jpg\",\"requestId\":\"5D92AD89939900C7C8EF8671\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1771569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_27_28.jpg\",\"requestId\":\"5D92AD8D939900C7C8EFAA1C\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1831569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_27_20.jpg\",\"requestId\":\"5D92AD91939900C7C8EFC55F\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto1931569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_27_13.jpg\",\"requestId\":\"5D92AD95939900C7C8EFE547\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2051569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_27_11.jpg\",\"requestId\":\"5D92AD99939900C7C8F00904\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2131569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_26_53.jpg\",\"requestId\":\"5D92AD9E939900C7C8F029A5\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2241569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_25_49.jpg\",\"requestId\":\"5D92ADA5939900C7C8F06021\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2331569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_25_41.jpg\",\"requestId\":\"5D92ADAB939900C7C8F08D7A\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2411569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_25_22.jpg\",\"requestId\":\"5D92ADAF939900C7C8F0AEED\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2531569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_25_08.jpg\",\"requestId\":\"5D92ADB2939900C7C8F0C8BB\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2601569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_24_59.jpg\",\"requestId\":\"5D92ADBA939900C7C8F0FF16\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2701569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_24_49.jpg\",\"requestId\":\"5D92ADBE939900C7C8F11B59\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2871569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_24_46.jpg\",\"requestId\":\"5D92ADC2939900C7C8F1396C\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto2971569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_24_29.jpg\",\"requestId\":\"5D92ADC7939900C7C8F16005\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3071569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_24_14.jpg\",\"requestId\":\"5D92ADCA939900C7C8F17D40\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3161569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_23_48.jpg\",\"requestId\":\"5D92ADCF939900C7C8F1A091\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3251569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_23_26.jpg\",\"requestId\":\"5D92ADD2939900C7C8F1B960\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3381569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_23_19.jpg\",\"requestId\":\"5D92ADD7939900C7C8F1DCBB\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3401569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_23_14.jpg\",\"requestId\":\"5D92ADDA939900C7C8F1F3B8\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3501569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_23_05.jpg\",\"requestId\":\"5D92ADDE939900C7C8F21034\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3601569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_58.jpg\",\"requestId\":\"5D92ADE3939900C7C8F22E7C\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3771569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_51.jpg\",\"requestId\":\"5D92ADE6939900C7C8F249B2\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3831569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_42.jpg\",\"requestId\":\"5D92ADEA939900C7C8F26583\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto3961569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_32.jpg\",\"requestId\":\"5D92ADEE939900C7C8F28280\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto4091569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_26.jpg\",\"requestId\":\"5D92ADF1939900C7C8F29CBE\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto4111569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_19.jpg\",\"requestId\":\"5D92ADF6939900C7C8F2BBF8\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto4291569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_22_09.jpg\",\"requestId\":\"5D92ADFA939900C7C8F2DEE2\",\"title\":\"\"},{\"picName\":\"http://at-images.oss-cn-hangzhou.aliyuncs.com/hrocloud/cgh/app/auto4351569893687657/storage/emulated/0/DCIM/Camera/2019_09_30_22_21_54.jpg\",\"requestId\":\"5D92ADFE939900C7C8F2FCCD\",\"title\":\"\"}]";
        if (StringUtils.isNotEmpty(pic)){
            List<Map> list = JSON.parseObject(pic, List.class);
            list.forEach(x -> System.out.println(x.get("picName")));
        }
    }
}
