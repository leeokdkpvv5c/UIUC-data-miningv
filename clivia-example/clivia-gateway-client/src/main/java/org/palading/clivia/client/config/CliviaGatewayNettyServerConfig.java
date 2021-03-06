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
package org.palading.clivia.client.config;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

/**
 * nettyReactiveWebServerFactory config
 * 
 * @author palading_cr
 * @title CliviaGatewayNettyServerConfig
 * @project clivia
 */
@Configuration
public class CliviaGatewayNettyServerConfig {

    @Bean
    public NettyReactiveWebServerFactory reactiveWebServerFactory() {
        NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
        webServerFactory.addServerCustomizers(new EventLoopNettyServerCustomizer());
        return webServerFactory;
    }

    /**
     * @author palading_cr
     *
     */
    class EventLoopNettyServerCustomizer implements NettyServerCustomizer {
        @Override
        public HttpServer apply(final HttpServer httpServer) {
            return httpServer
                .tcpConfiguration(tcpServer -> tcpServer
                    .runOn(LoopResources.create("clivia-client-netty", 1, LoopResources.DEFAULT_IO_WORKER_COUNT, true),
                        false).selectorOption(ChannelOption.SO_REUSEADDR, true)
                    .selectorOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT));
        }
    }

}
