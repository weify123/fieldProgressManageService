package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.AppUserMapper;
import com.autoyol.field.progress.manage.mapper.AttendanceMapper;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.attendance.AttendanceExportReqVo;
import com.autoyol.field.progress.manage.request.attendance.AttendanceQueryReqVo;
import com.autoyol.field.progress.manage.request.attendance.AttendanceReqVo;
import com.autoyol.field.progress.manage.request.attendance.AttendanceUpdateMemoReqVo;
import com.autoyol.field.progress.manage.response.attendance.AttendanceMemoRespVo;
import com.autoyol.field.progress.manage.response.attendance.AttendanceQueryPageRespVo;
import com.autoyol.field.progress.manage.response.attendance.AttendanceQueryVo;
import com.autoyol.field.progress.manage.response.attendance.AttendanceVo;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.field.progress.manage.util.poi.ExcelMergeUtil;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.convert.ConvertAppUser.buildUser;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class AttendanceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    DictCache dictCache;

    @Resource
    CityCache cityCache;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    CommonService commonService;

    @Resource
    ImportService importService;

    @Resource
    AttendanceMapper attendanceMapper;

    @Value("${attendance.display.day:7}")
    private long displayDay;

    @Value("${attendance.max.day:30}")
    private Long maxDay;

    @Value("${attendance.online.day}")
    private String onlineDay;

    @Resource
    MsgService msgService;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    public AttendanceQueryPageRespVo queryByPage(AttendanceQueryReqVo reqVo, Date date) throws Exception {
        AttendanceQueryPageRespVo pageRespVo = new AttendanceQueryPageRespVo();
        pageRespVo.setPageNo(reqVo.getPageNo());
        pageRespVo.setPageSize(reqVo.getPageSize());

        int count = appUserMapper.countAppUserPageByCond(buildUser(reqVo));
        if (count <= NEG_ZERO) return pageRespVo;

        List<AppUserEntity> entities = appUserMapper.queryAppUserPageByCond(buildUser(reqVo));
        if (!CollectionUtils.isEmpty(entities)) {
            List<String> typeList = buildTypeList(USER_STATUS, ATTENDANCE_TYPE, ZONE_TYPE);
            Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
            pageRespVo.setTotal(count);
            pageRespVo.setList(entities.stream()
                    .map(u -> {
                        AttendanceQueryVo respVo = new AttendanceQueryVo();
                        BeanUtils.copyProperties(u, respVo);
                        respVo.setUserId(u.getId());
                        buildAndSetAttendance(date, map, respVo);
                        if (CollectionUtils.isEmpty(respVo.getAttendanceList())) {
                            return null;
                        }
                        respVo.setStatusKey(u.getCurStatus());
                        respVo.setStatusVal(Optional.ofNullable(u.getCurStatus())
                                .map(status -> getLabelByCode(map.get(USER_STATUS), status))
                                .orElse(STRING_EMPTY));
                        respVo.setCityName(commonService.getCityName(u.getCityId()));
                        respVo.setCompanyName(commonService.getCompanyName(u.getEmploymentCompanyId()));
                        return respVo;
                    }).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        return pageRespVo;
    }

    public AttendanceQueryPageRespVo exportAttendance(AttendanceExportReqVo reqVo, Date startDate, Date endDate) throws Exception {
        AttendanceQueryPageRespVo pageRespVo = new AttendanceQueryPageRespVo();

        List<AppUserEntity> entities = appUserMapper.queryAppUserByCond(buildUser(reqVo));
        if (!CollectionUtils.isEmpty(entities)) {
            pageRespVo.setTotal(entities.size());
            List<String> typeList = buildTypeList(USER_STATUS, ATTENDANCE_TYPE);
            Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);

            pageRespVo.setList(entities.stream()
                    .sorted(Comparator.comparing(AppUserEntity::getCreateTime).reversed())
                    .map(u -> {
                        AttendanceQueryVo respVo = new AttendanceQueryVo();
                        BeanUtils.copyProperties(u, respVo);
                        buildAndSetAttendance(startDate, endDate, map, respVo);
                        if (CollectionUtils.isEmpty(respVo.getAttendanceList())) {
                            return null;
                        }
                        respVo.setStatusVal(Optional.ofNullable(u.getCurStatus())
                                .map(status -> getLabelByCode(map.get(USER_STATUS), status))
                                .orElse(STRING_EMPTY));
                        respVo.setCityName(commonService.getCityName(u.getCityId()));
                        respVo.setCompanyName(commonService.getCompanyName(u.getEmploymentCompanyId()));
                        return respVo;
                    }).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        return pageRespVo;
    }

    private static SysDictEntity getDictOrDefault(List<SysDictEntity> list, SysDictEntity defaultEntity) {
        return list.stream().min(Comparator.comparing(SysDictEntity::getCode)).orElse(defaultEntity);
    }

    public AttendanceMemoRespVo queryMemoById(AttendanceReqVo reqVo) {
        AttendanceEntity entity = attendanceMapper.selectById(reqVo.getId());
        return new AttendanceMemoRespVo(getObjOrDefault(entity, AttendanceEntity::getMemo, STRING_EMPTY));
    }

    /**
     * field同步仁云
     *
     * @param record
     * @param operator
     */
    private void syncAttendance(AttendanceEntity record, String operator) {
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            AttendanceEntity entity = attendanceMapper.selectById(record.getId());
            List<SysDictEntity> dictEntityList = null;
            try {
                dictEntityList = dictCache.getDictByTypeNameFromCache(ATTENDANCE_TYPE);
            } catch (Exception e) {
                logger.error("***PRO***查询考勤字典失败，id={}, e={}", record.getId(), e);
            }
            Map<String, Object> map = msgService.buildMap();
            map.put("id", entity.getId());
            map.put("renYunUserId", entity.getUserId());
            map.put("status", getLabelByCode(dictEntityList, ATTENDANCE_TYPE));
            map.put("memo", record.getMemo());
            map.put("operator", operator);
            msgService.sendMq(sqlServer_attendance_mq_exchange, sqlServer_attendance_mq_routing_key, INT_FOUR, map);
        }
    }

    /**
     * 仁云同步流程
     *
     * @param map
     */
    public void updateAttendance(Map map) throws Exception {
        Integer id = (Integer) map.get("id");
        String operator = (String) map.get("operator");
        String memo = (String) map.get("memo");
        String status = (String) map.get("status");
        if (Objects.isNull(id) || StringUtils.isEmpty(operator)) {
            return;
        }

        List<String> typeList = buildTypeList(ATTENDANCE_TYPE, MARK_TYPE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(id);
        int statusKey = getCodeByLabel(dictMap.get(ATTENDANCE_TYPE), status);
        entity.setStatus(getObjOrDefault(statusKey, String::valueOf, null));
        if (statusKey != NEG_ONE) {
            entity.setMark(getMark(dictMap.get(MARK_TYPE), statusKey));
        }
        entity.setMemo(memo);
        entity.setUpdateOp(operator);
        attendanceMapper.updateMemoById(entity);
    }

    public int updateStatusAndMemoById(AttendanceUpdateMemoReqVo reqVo) throws Exception {
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(reqVo.getId());
        entity.setStatus(String.valueOf(reqVo.getStatusKey()));
        List<SysDictEntity> dictEntities = dictCache.getDictByTypeNameFromCache(MARK_TYPE);
        entity.setMark(getMark(dictEntities, reqVo.getStatusKey()));
        entity.setMemo(getObjOrDefault(reqVo.getMemo(), Function.identity(), STRING_EMPTY));
        entity.setUpdateOp(reqVo.getHandleUser());
        entity.setUpdateTime(new Date());
        int count = attendanceMapper.updateMemoById(entity);
        syncAttendance(entity, reqVo.getHandleUser());
        return count;
    }

    public int importAttendance(LocalDate inputDate, Workbook book, String operator) throws Exception {
        int count = NEG_ZERO;
        StringBuilder matchSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer matchFail = new StringBuffer();
        StringBuilder importSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer importFail = new StringBuffer();

        AtomicInteger matchSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger matchFailCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importFailCount = new AtomicInteger(NEG_ZERO);

        List<AttendanceEntity> importList = Lists.newArrayList();
        List<String> typeList = buildTypeList(ZONE_TYPE, ATTENDANCE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);

            List<String> days = ExcelMergeUtil.getMergedRegionValue(sheet, i, CONTAIN_NUMBER);
            List<SysDictEntity> zoneCodes = Lists.newArrayList();
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                if (j == INT_ONE) {
                    List<SysDictEntity> zoneList = map.get(ZONE_TYPE);
                    range(row.getFirstCellNum(), row.getLastCellNum()).forEach(k -> {
                        zoneCodes.add(Optional.ofNullable(PoiUtil.getValue(row.getCell(k)))
                                .map(s -> zoneList.stream()
                                        .filter(zone -> zone.getLabel().equals(s))
                                        .findFirst().orElse(new SysDictEntity()))
                                .orElse(new SysDictEntity()));
                    });
                    continue;
                }

                int userId;
                try {
                    userId = Integer.parseInt(PoiUtil.getValue(row.getCell(0)));
                } catch (NumberFormatException e) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }
                AppUserEntity userEntity;
                try {
                    userEntity = appUserMapper.selectByPrimaryKey(userId);
                    SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(operator.split(SPLIT_COMMA)[1]);
                    if (Objects.nonNull(accountEntity)) {
                        boolean is = objEquals(accountEntity.getCompanyId(), userEntity.getEmploymentCompanyId(), Integer::equals) &&
                                objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), userEntity.getCityId(), List::contains);
                        if (!is) {
                            appendLog(matchFail, matchFailCount, j);
                            continue;
                        }
                    }

                    if (Objects.isNull(userEntity)) {
                        appendLog(matchFail, matchFailCount, j);
                        continue;
                    }
                    if (objEquals(userEntity.getCurStatus(), String.valueOf(INT_THREE), String::equals)) {
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("userEntity 查询异常 {}", e);
                    Cat.logError("userEntity 查询异常 {}", e);
                    break;
                }
                String name = PoiUtil.getValue(row.getCell(1));
                if (StringUtils.isEmpty(name) || !name.equals(userEntity.getUserName())) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }

                try {
                    EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(PoiUtil.getValue(row.getCell(2)));
                    if (Objects.isNull(companyEntity)) {
                        appendLog(matchFail, matchFailCount, j);
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("companyCache 查询异常 {}", e);
                    Cat.logError("companyCache 查询异常 {}", e);
                    break;
                }

                try {
                    Integer cityId = cityCache.getCityIdByCityNameFromCache(PoiUtil.getValue(row.getCell(3)));
                    if (cityId < NEG_ZERO) {
                        appendLog(matchFail, matchFailCount, j);
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("cityCache 查询异常 {}", e);
                    Cat.logError("cityCache 查询异常 {}", e);
                    break;
                }
                matchSucceedCount.incrementAndGet();

                boolean isValid = false;
                List<Integer> statusList = Lists.newArrayList();
                for (int k = row.getFirstCellNum() + INT_FOUR; k < row.getLastCellNum(); k++) {
                    String status = PoiUtil.getValue(row.getCell(k));
                    int statusKQ = map.get(ATTENDANCE_TYPE).stream().filter(dict -> objEquals(dict.getLabel(), status, String::equals))
                            .findFirst()
                            .map(SysDictEntity::getCode).orElse(NEG_ONE);
                    if (statusKQ < NEG_ZERO) {
                        appendLog(importFail, importFailCount, j);
                        break;
                    }
                    statusList.add(statusKQ);
                    isValid = true;
                }
                if (!isValid) {
                    continue;
                }

                importSucceedCount.incrementAndGet();

                setImportList(inputDate, importList, days, zoneCodes, userEntity, statusList, operator.split(SPLIT_COMMA)[0]);
            }
        });
        // 空白Excel直接结束
        List<AttendanceEntity> distinctList = importList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctList) && isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }

        if (matchFailCount.get() + importFailCount.get() > 0 && importSucceedCount.get() > 0) {
            count = NEG_THREE;
        }

        if (matchFailCount.get() + importFailCount.get() > 0 && importSucceedCount.get() <= 0) {
            count = NEG_FOUR;
        }

        matchSucceed.append(matchSucceedCount.get()).append(CH_COUNT);
        importSucceed.append(importSucceedCount.get()).append(CH_COUNT);
        // 保存日志数据库
        importService.saveImportLog(matchSucceed.toString(), matchFailCount, matchFail,
                importSucceed.toString(), importFailCount, importFail, operator.split(SPLIT_COMMA)[0],
                Integer.parseInt(CommonEnum.LOG_TYPE_USER_KQ.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }
        logger.info("***PRO***准备修改考勤导入数据 {}", distinctList.size());
        List<SysDictEntity> dictEntities = dictCache.getDictByTypeNameFromCache(MARK_TYPE);
        importService.attendanceImport(dictEntities, distinctList);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            distinctList.forEach(x -> {
                List<AttendanceEntity> entityList = attendanceMapper.selectByUserId(x.getUserId());
                entityList.stream()
                        .filter(entity -> objEquals(entity.getStartTime(), x.getStartTime(), Date::equals))
                        .forEach(entity -> {
                            syncAttendance(entity, operator);
                        });
            });
        }
        return count;
    }

    private void setImportList(LocalDate inputDate, List<AttendanceEntity> importList, List<String> days,
                               List<SysDictEntity> zoneCodes, AppUserEntity userEntity, List<Integer> statusList,
                               String operator) {
        range(NEG_ZERO, days.size()).forEach(day -> {
            LocalDate tmpDate = inputDate.plusDays(day);
            int tmpDay = day * INT_TOW;
            AttendanceEntity entity = buildImportAttendanceEntity(zoneCodes, userEntity, statusList, operator, tmpDay, tmpDate);
            importList.add(entity);

            AttendanceEntity attendanceEntity = buildImportAttendanceEntity(zoneCodes, userEntity, statusList, operator, tmpDay + INT_ONE, tmpDate);
            importList.add(attendanceEntity);
        });
    }

    private AttendanceEntity buildImportAttendanceEntity(List<SysDictEntity> zoneCodes, AppUserEntity userEntity,
                                                         List<Integer> statusList, String operator, int day, LocalDate tmpDate) {
        zoneCodes = zoneCodes.stream().filter(zone -> Objects.nonNull(zone.getId())).collect(Collectors.toList());

        AttendanceEntity entity = new AttendanceEntity();
        entity.setUserId(userEntity.getId());
        entity.setZoneCode(zoneCodes.get(day).getCode());
        entity.setStartTime(convertToDate(tmpDate.atTime(
                parseLocalTime(zoneCodes.get(day).getLabel1().split(SPLIT_HORIZONTAL)[NEG_ZERO], DATE_TIME))));
        entity.setStatus(String.valueOf(statusList.get(day)));
        entity.setUpdateOp(operator);
        return entity;
    }

    @Async("taskExecutor")
    public void batchInsertAllUserThirtyAttendance(String operator, AppUserEntity entity) throws Exception {

        List<String> typeList = buildTypeList(ZONE_TYPE, ATTENDANCE_TYPE, MARK_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        List<SysDictEntity> zoneList = map.get(ZONE_TYPE);
        logger.info("***PRO*** zoneList {}", zoneList);
        if (CollectionUtils.isEmpty(zoneList)) {
            return;
        }

        SysDictEntity dictEntity = new SysDictEntity();
        dictEntity.setCode(NEG_ZERO);
        dictEntity.setLabel(CQ);
        SysDictEntity attendanceEntity = getDictOrDefault(map.get(ATTENDANCE_TYPE), dictEntity);
        SysDictEntity markEntity = getDictOrDefault(map.get(MARK_TYPE), dictEntity);

        LocalDate online = convertToLocalDate(convertToDate(onlineDay));
        LocalDate toTime = LocalDate.now().plusDays(maxDay - INT_ONE);

        List<AttendanceEntity> attendanceEntities = Lists.newArrayList();

        if (Objects.isNull(entity)) {
            int count = appUserMapper.batchCountQuery();
            BasePage basePage = new BasePage();
            basePage.setPageSize(INT_THOUSAND);
            range(NEG_ZERO, countBatch(count)).forEach(i -> {
                basePage.setPageNo(i + INT_ONE);
                List<AppUserEntity> entities = appUserMapper.batchQueryByCond(basePage);
                processAttendance(operator, zoneList, attendanceEntity, markEntity, online, toTime, attendanceEntities, entities);
                logger.info("***PRO*** 第{}批次执行结束basePage,{}", i, basePage);
            });
        } else {
            List<AppUserEntity> entities = getObjOrDefault(entity, Arrays::asList, null);
            processAttendance(operator, zoneList, attendanceEntity, markEntity, online, toTime, attendanceEntities, entities);
        }
        range(NEG_ZERO, countBatch(attendanceEntities.size())).forEach(i -> {
            List<AttendanceEntity> list = attendanceEntities.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND).collect(Collectors.toList());
            attendanceMapper.batchInsert(list);
        });
        logger.info("***PRO*** 考勤生成完毕");
    }

    private void processAttendance(String operator, List<SysDictEntity> zoneList, SysDictEntity attendanceEntity,
                                   SysDictEntity markEntity, LocalDate online, LocalDate toTime,
                                   List<AttendanceEntity> attendanceEntities, List<AppUserEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        entities.forEach(u -> {
            LocalDate userCreated = convertToLocalDate(u.getCreateTime());
            LocalDate tmp = userCreated.isAfter(online) ? userCreated : online;
            List<AttendanceEntity> list = attendanceMapper.selectByUserId(u.getId());
            for (; tmp.compareTo(toTime) <= 0; tmp = tmp.plusDays(INT_ONE)) {
                if (!CollectionUtils.isEmpty(list) && isExistDate(list, tmp)) {
                    continue;
                }
                buildAttendanceList(attendanceEntity, markEntity, attendanceEntities, u.getId(), tmp, zoneList, operator);
            }
        });
    }

    private boolean isExistDate(List<AttendanceEntity> list, LocalDate tmp) {
        return list.stream().map(AttendanceEntity::getStartTime)
                .map(LocalDateUtil::convertToLocalDate).anyMatch(d -> d.compareTo(tmp) == NEG_ZERO);
    }

    private void buildAttendanceList(SysDictEntity attendanceEntity, SysDictEntity markEntity,
                                     List<AttendanceEntity> attendanceEntities,
                                     Integer userId, LocalDate tmp, List<SysDictEntity> zoneList, String operator) {
        zoneList.forEach(zone -> {
            try {
                AttendanceEntity entity = new AttendanceEntity();
                entity.setStatus(attendanceEntity.getCode() + STRING_EMPTY);
                entity.setMark(markEntity.getCode());
                if (!StringUtils.isEmpty(zone.getLabel1())) {
                    String[] times = zone.getLabel1().split(SPLIT_HORIZONTAL);
                    LocalDateTime startTime = tmp.atTime(parseLocalTime(times[NEG_ZERO], DATE_TIME));
                    entity.setStartTime(convertToDate(startTime));
                    LocalDateTime endTime = tmp.atTime(parseLocalTime(times[INT_ONE], DATE_TIME));
                    entity.setEndTime(convertToDate(endTime));
                }
                entity.setZoneDesc(zone.getLabel());
                entity.setZoneCode(zone.getCode());
                entity.setUserId(userId);
                entity.setCreateOp(operator);
                attendanceEntities.add(entity);
            } catch (Exception e) {
                logger.error("***PRO*** AttendanceEntity 设置异常 {}", e);
            }
        });
    }

    private void buildAndSetAttendance(Date startDate, Date endDate, Map<String, List<SysDictEntity>> map, AttendanceQueryVo respVo) {
        List<AttendanceEntity> list = attendanceMapper.selectByUserId(respVo.getUserId());

        // 包括当天，即减一
        LocalDate start = convertToLocalDate(startDate).minusDays(INT_ONE);
        LocalDate end = convertToLocalDate(endDate).plusDays(INT_ONE);

        if (!CollectionUtils.isEmpty(list)) {
            respVo.setAttendanceList(list.stream()
                    .filter(entity -> convertToLocalDate(entity.getStartTime()).isAfter(start))
                    .filter(entity -> convertToLocalDate(entity.getStartTime()).isBefore(end))
                    .map(entity -> {
                        AttendanceVo vo = new AttendanceVo();
                        vo.setId(entity.getId());
                        vo.setDay(dateToStringFormat(entity.getStartTime(), DATE_YYYY_MM_DD_SPOT));
                        vo.setZoneDesc(entity.getZoneDesc());
                        vo.setStatusKey(entity.getStatus());
                        vo.setStatusVal(getLabelByCode(map.get(ATTENDANCE_TYPE), entity.getStatus()));
                        vo.setMemo(entity.getMemo());
                        return vo;
                    }).collect(Collectors.toList()));
        }
    }

    private void buildAndSetAttendance(Date date, Map<String, List<SysDictEntity>> map, AttendanceQueryVo respVo) {
        List<AttendanceEntity> list = attendanceMapper.selectByUserId(respVo.getUserId());

        // 包括当天，即减一
        LocalDate max = LocalDate.now().plusDays(maxDay - INT_ONE);
        LocalDate inputDate = convertToLocalDate(date);
        int day = getInterDay(max, inputDate) + INT_ONE;

        LocalDate displayDate;
        if (day < displayDay) {
            inputDate = max;
            displayDate = max.minusDays(displayDay - INT_ONE);
        } else {
            displayDate = convertToLocalDate(date).plusDays(displayDay - INT_ONE);
        }

        LocalDate firstDate;
        LocalDate lastDate;
        if (displayDate.isBefore(inputDate)) {
            firstDate = displayDate.minusDays(INT_ONE);
            lastDate = inputDate.plusDays(INT_ONE);
        } else {
            firstDate = inputDate.minusDays(INT_ONE);
            lastDate = displayDate.plusDays(INT_ONE);
        }

        if (!CollectionUtils.isEmpty(list)) {
            List<AttendanceVo> attendanceVos = list.stream()
                    .filter(entity -> convertToLocalDate(entity.getStartTime()).isAfter(firstDate))
                    .filter(entity -> convertToLocalDate(entity.getStartTime()).isBefore(lastDate))
                    .map(entity -> {
                        AttendanceVo vo = new AttendanceVo();
                        vo.setId(entity.getId());
                        vo.setDay(dateToStringFormat(entity.getStartTime(), DATE_YYYY_MM_DD_LINE));
                        vo.setZoneDesc(entity.getZoneDesc());
                        vo.setStatusKey(entity.getStatus());
                        vo.setStatusVal(getLabelByCode(map.get(ATTENDANCE_TYPE), entity.getStatus()));
                        vo.setMemo(entity.getMemo());
                        return vo;
                    }).collect(Collectors.toList());
            // 补数据
            if (attendanceVos.size() >> INT_ONE < displayDay) {
                range(0, (int) displayDay - (attendanceVos.size() >> INT_ONE)).forEach(i -> {
                    map.get(ZONE_TYPE).forEach(zone -> {
                        AttendanceVo vo = new AttendanceVo();
                        vo.setDay(dateToStringFormat(convertToDate(firstDate.plusDays(i + INT_ONE)), DATE_YYYY_MM_DD_LINE));
                        vo.setZoneDesc(zone.getLabel());
                        attendanceVos.add(vo);
                    });
                });
            }
            respVo.setAttendanceList(attendanceVos.stream()
                    .sorted(Comparator.comparing(AttendanceVo::getDay))
                    .peek(vo -> vo.setDay(LocalDate.parse(vo.getDay()).getDayOfMonth() + DAY))
                    .collect(Collectors.toList()));
        }
    }
}
