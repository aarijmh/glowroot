/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.informant.testkit;

import checkers.nullness.quals.Nullable;

import io.informant.InformantModule;
import io.informant.MainEntryPoint;
import io.informant.config.ConfigModule;
import io.informant.config.ConfigService;
import io.informant.config.GeneralConfig;
import io.informant.config.PluginConfig;

/**
 * @author Trask Stalnaker
 * @since 0.5
 */
public class AppUnderTestServices {

    public static AppUnderTestServices get() {
        return new AppUnderTestServices();
    }

    private AppUnderTestServices() {}

    public void setEnabled(boolean enabled) throws Exception {
        ConfigService configService = getConfigService();
        GeneralConfig base = configService.getGeneralConfig();
        GeneralConfig.Overlay config = GeneralConfig.overlay(base);
        config.setEnabled(enabled);
        configService.updateGeneralConfig(config.build(), base.getVersion());
    }

    public void setPluginEnabled(String pluginId, boolean enabled) throws Exception {
        ConfigService configService = getConfigService();
        PluginConfig base = configService.getPluginConfig(pluginId);
        if (base == null) {
            throw new IllegalStateException("Plugin not found for pluginId: " + pluginId);
        }
        PluginConfig.Builder config = PluginConfig.builder(base);
        config.enabled(enabled);
        configService.updatePluginConfig(config.build(), base.getVersion());
    }

    public void setPluginProperty(String pluginId, String propertyName,
            @Nullable Object propertyValue) throws Exception {
        ConfigService configService = getConfigService();
        PluginConfig base = configService.getPluginConfig(pluginId);
        if (base == null) {
            throw new IllegalStateException("Plugin not found for pluginId: " + pluginId);
        }
        PluginConfig.Builder config = PluginConfig.builder(base);
        config.setProperty(propertyName, propertyValue);
        configService.updatePluginConfig(config.build(), base.getVersion());
    }

    private static ConfigService getConfigService() {
        InformantModule informantModule = MainEntryPoint.getInformantModule();
        ConfigModule configModule = informantModule.getConfigModule();
        return configModule.getConfigService();
    }
}
