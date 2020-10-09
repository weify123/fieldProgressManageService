package com.autoyol.field.progress.manage.listener;

import com.autoyol.field.progress.manage.cache.AttrLabelCache;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.AttrLabelEntity;
import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.mapper.CityLevelMapper;
import com.autoyol.field.progress.manage.mapper.SysDictEntityMapper;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class LoadCacheListener implements ApplicationListener<ApplicationReadyEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    private CityLevelMapper cityLevelMapper;

    @Resource
    private SysDictEntityMapper sysDictEntityMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("===========pre load cache.==========");
        try {
            List<CityLevelEntity> cacheList = cityCache.getAllCityFromCache();
            if (!CollectionUtils.isEmpty(cacheList)) {
                cityCache.deleteCityCache(cacheList.stream().map(CityLevelEntity::getCityId).map(String::valueOf).toArray());

                List<CityLevelEntity> dbList = cityLevelMapper.selectAll();
                if (Objects.nonNull(dbList)) {
                    dbList.forEach(city -> cityCache.addCityToCache(city));
                }
            }
            logger.info("=========city cache loaded.=========");

            List<SysDictEntity> dictCacheList = dictCache.getAllDictFromCache();
            if (!CollectionUtils.isEmpty(dictCacheList)) {
                dictCache.deleteDictCache(dictCacheList.stream().map(SysDictEntity::getTypeName).toArray());

                List<SysDictEntity> dbList = sysDictEntityMapper.selectAll();
                if (Objects.nonNull(dbList)) {
                    dictCache.addDictMapToCache(dbList.stream().collect(Collectors.groupingBy(SysDictEntity::getTypeName)));
                }
            }
            logger.info("=========dict cache loaded.=========");

            List<AttrLabelEntity> attrLabelEntities = attrLabelCache.getAllAttrListFromCache();
            if (!CollectionUtils.isEmpty(attrLabelEntities)) {
                attrLabelCache.deleteAttrLabelCache(attrLabelEntities.stream().map(AttrLabelEntity::getId).map(String::valueOf).toArray());
            }
            logger.info("=========attrLabel cache loaded.=========");

            logger.info("====================================");
        } catch (Exception e) {
            logger.error("***PRO*** onApplicationEvent load cache error {}", e);
            Cat.logError("onApplicationEvent load cache error {}", e);
        }
        logger.info("==========all cache loaded.=========");
        logger.info("====================================");
    }
}
