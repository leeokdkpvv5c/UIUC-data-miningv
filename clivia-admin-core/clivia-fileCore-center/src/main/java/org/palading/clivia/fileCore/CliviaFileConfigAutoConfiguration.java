/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.palading.clivia.fileCore;

import org.palading.clivia.config.api.CliviaApiConfigService;
import org.palading.clivia.config.api.CliviaBlacklistConfigService;
import org.palading.clivia.config.api.CliviaClientSecurityConfigService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author palading_cr
 * @title CliviaFileConfigAutoConfiguration
 * @project clivia
 */
@Conditional(CliviaFileConfigCondition.class)
@Configuration
public class CliviaFileConfigAutoConfiguration {
    /**
     * org.palading.clivia.config.api config service
     *
     * @author palading_cr
     *
     */
    @Conditional(CliviaFileConfigCondition.class)
    @Bean
    public CliviaApiConfigService cliviaApiConfigService(
        ObjectProvider<CliviaFileConfigCommonService> cliviaFileConfigCommonSerivice) {
        return new CliviaFileApiConfigServiceImpl(cliviaFileConfigCommonSerivice.getIfAvailable());
    }

    /**
     * blacklist config service
     *
     * @author palading_cr
     *
     */
    @Conditional(CliviaFileConfigCondition.class)
    @Bean
    public CliviaBlacklistConfigService cliviaBlackListConfigService(
        ObjectProvider<CliviaFileConfigCommonService> cliviaFileConfigCommonSerivice) {
        return new CliviaFileBlacklistConfigServiceImpl(cliviaFileConfigCommonSerivice.getIfAvailable());
    }

    @Conditional(CliviaFileConfigCondition.class)
    @Bean
    public CliviaFileConfigCommonService cliviaFileConfigCommonSerivice() {
        return new CliviaFileConfigCommonService();
    }

    /**
     * client switcher service
     *
     * @author palading_cr
     *
     */
    @Conditional(CliviaFileConfigCondition.class)
    @Bean
    public CliviaClientSecurityConfigService cliviaClientSwitcherConfigService(
        ObjectProvider<CliviaFileConfigCommonService> cliviaFileConfigCommonSerivice) {
        return new CliviaFileClientSecurityConfigServiceImpl(cliviaFileConfigCommonSerivice.getIfAvailable());
    }

    @Conditional(CliviaFileConfigCondition.class)
    @Bean
    public CliviaFileConfigCommandLineRunner cliviaFileConfigCommandLineRunner(
        ObjectProvider<CliviaApiConfigService> cliviaApiConfigService,
        ObjectProvider<CliviaBlacklistConfigService> cliviaBlacklistConfigService) {
        return new CliviaFileConfigCommandLineRunner(cliviaApiConfigService.getIfAvailable(),
            cliviaBlacklistConfigService.getIfAvailable());
    }

}
