package com.autoyol.field.progress.manage.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.netflix.config.*;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @program: AutoPopConsole
 * @description:
 * @author: Lu
 * @create: 2019-02-25 20:34
 **/
@Configuration
@EnableApolloConfig
public class ApolloDynamicConfig implements WatchedConfigurationSource, InitializingBean {
    private static final String HYSTRIX = "application";

    private List<WatchedUpdateListener> listeners = new CopyOnWriteArrayList<>();

    @ApolloConfigChangeListener({HYSTRIX})
    public void onChange(ConfigChangeEvent changeEvent) {
        fireEvent(changeEvent);
    }

    private void fireEvent(ConfigChangeEvent changeEvent) {
        WatchedUpdateResult result = getWatchedUpdateResult(changeEvent);
        listeners.forEach(listener -> listener.updateConfiguration(result));
        HystrixPropertiesFactory.reset();
    }

    private WatchedUpdateResult getWatchedUpdateResult(ConfigChangeEvent changeEvent) {
        Map<String, Object> added = new HashMap<>();
        Map<String, Object> changed = new HashMap<>();
        Map<String, Object> deleted = new HashMap<>();
        changeEvent.changedKeys().forEach(key -> {
            ConfigChange change = changeEvent.getChange(key);
            switch (change.getChangeType()) {
                case ADDED:
                    added.put(key, change.getNewValue());
                    break;
                case MODIFIED:
                    changed.put(key, change.getNewValue());
                    break;
                case DELETED:
                    deleted.put(key, change.getNewValue());
                    break;
            }
        });
        return WatchedUpdateResult.createIncremental(added, changed, deleted);
    }

    @Override
    public void addUpdateListener(WatchedUpdateListener watchedUpdateListener) {
        if (watchedUpdateListener != null) {
            listeners.add(watchedUpdateListener);
        }
    }

    @Override
    public void removeUpdateListener(WatchedUpdateListener watchedUpdateListener) {
        listeners.remove(watchedUpdateListener);
    }

    @Override
    public Map<String, Object> getCurrentData() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        Config config = ConfigService.getConfig(HYSTRIX);
        config.getPropertyNames().forEach(p -> configMap.put(p, config.getProperty(p, "")));
        return configMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        installDynamicConfiguration(); // 安装动态配置
    }

    private void installDynamicConfiguration() {
        System.setProperty(DynamicPropertyFactory.ENABLE_JMX, "true");
        ConfigurationManager.install(new DynamicWatchedConfiguration(this));
    }

}
