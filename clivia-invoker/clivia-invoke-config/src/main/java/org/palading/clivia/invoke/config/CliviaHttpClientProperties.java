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
package org.palading.clivia.invoke.config;

import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Max;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ResourceUtils;
import org.springframework.util.unit.DataSize;

import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.SslProvider;

/**
 * @author palading_cr
 * @title CliviaHttpClientProperties
 * @project clivia
 */
@ConfigurationProperties(prefix = "clivia.gateway.httpclient")
public class CliviaHttpClientProperties {

    /** The connect timeout in millis, the default is 45s. */
    private Integer connectTimeout;

    /** The response timeout. */
    private Duration responseTimeout;

    /** The max response header size. */
    private DataSize maxHeaderSize;

    /** Pool configuration for Netty HttpClient. */
    private Pool pool = new Pool();

    /** Proxy configuration for Netty HttpClient. */
    private Proxy proxy = new Proxy();

    /** SSL configuration for Netty HttpClient. */
    private Ssl ssl = new Ssl();

    /** Enables wiretap debugging for Netty HttpClient. */
    private boolean wiretap;

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    @Max(Integer.MAX_VALUE)
    public DataSize getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public void setMaxHeaderSize(DataSize maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Ssl getSsl() {
        return ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public boolean isWiretap() {
        return this.wiretap;
    }

    public void setWiretap(boolean wiretap) {
        this.wiretap = wiretap;
    }

    @Override
    public String toString() {
        // @formatter:off
        return new ToStringCreator(this)
                .append("connectTimeout", connectTimeout)
                .append("responseTimeout", responseTimeout)
                .append("maxHeaderSize", maxHeaderSize)
                .append("pool", pool)
                .append("proxy", proxy)
                .append("ssl", ssl)
                .append("wiretap", wiretap)
                .toString();
        // @formatter:on
    }

    public static class Pool {

        /** Type of pool for HttpClient to use, defaults to ELASTIC. */
        private PoolType type = PoolType.ELASTIC;

        /** The channel pool map name, defaults to proxy. */
        private String name = "proxy";

        /**
         * Only for type FIXED, the maximum number of connections before starting pending acquisition on existing ones.
         */
        private Integer maxConnections = ConnectionProvider.DEFAULT_POOL_MAX_CONNECTIONS;

        /** Only for type FIXED, the maximum time in millis to wait for aquiring. */
        private Long acquireTimeout = ConnectionProvider.DEFAULT_POOL_ACQUIRE_TIMEOUT;

        public PoolType getType() {
            return type;
        }

        public void setType(PoolType type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMaxConnections() {
            return maxConnections;
        }

        public void setMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
        }

        public Long getAcquireTimeout() {
            return acquireTimeout;
        }

        public void setAcquireTimeout(Long acquireTimeout) {
            this.acquireTimeout = acquireTimeout;
        }

        @Override
        public String toString() {
            return "Pool{" + "type=" + type + ", name='" + name + '\'' + ", maxConnections=" + maxConnections
                + ", acquireTimeout=" + acquireTimeout + '}';
        }

        public enum PoolType {

            /**
             * Elastic pool type.
             */
            ELASTIC,

            /**
             * Fixed pool type.
             */
            FIXED,

            /**
             * Disabled pool type.
             */
            DISABLED

        }

    }

    public class Proxy {

        /** Hostname for proxy configuration of Netty HttpClient. */
        private String host;

        /** Port for proxy configuration of Netty HttpClient. */
        private Integer port;

        /** Username for proxy configuration of Netty HttpClient. */
        private String username;

        /** Password for proxy configuration of Netty HttpClient. */
        private String password;

        /**
         * Regular expression (Java) for a configured list of hosts. that should be reached directly, bypassing the
         * proxy
         */
        private String nonProxyHostsPattern;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNonProxyHostsPattern() {
            return nonProxyHostsPattern;
        }

        public void setNonProxyHostsPattern(String nonProxyHostsPattern) {
            this.nonProxyHostsPattern = nonProxyHostsPattern;
        }

        @Override
        public String toString() {
            return "Proxy{" + "host='" + host + '\'' + ", port=" + port + ", username='" + username + '\'' + ", password='"
                + password + '\'' + ", nonProxyHostsPattern='" + nonProxyHostsPattern + '\'' + '}';
        }

    }

    public class Ssl {

        /**
         * Installs the netty InsecureTrustManagerFactory. This is insecure and not suitable for production.
         */
        private boolean useInsecureTrustManager = false;

        /** Trusted certificates for verifying the remote endpoint's certificate. */
        private List<String> trustedX509Certificates = new ArrayList<>();

        // use netty default SSL timeouts
        /** SSL handshake timeout. Default to 10000 ms */
        private Duration handshakeTimeout = Duration.ofMillis(10000);

        /** SSL close_notify flush timeout. Default to 3000 ms. */
        private Duration closeNotifyFlushTimeout = Duration.ofMillis(3000);

        /** SSL close_notify read timeout. Default to 0 ms. */
        private Duration closeNotifyReadTimeout = Duration.ZERO;

        /** The default ssl configuration type. Defaults to TCP. */
        private SslProvider.DefaultConfigurationType defaultConfigurationType = SslProvider.DefaultConfigurationType.TCP;

        public List<String> getTrustedX509Certificates() {
            return trustedX509Certificates;
        }

        public void setTrustedX509Certificates(List<String> trustedX509) {
            this.trustedX509Certificates = trustedX509;
        }

        public X509Certificate[] getTrustedX509CertificatesForTrustManager() {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                ArrayList<Certificate> allCerts = new ArrayList<>();
                for (String trustedCert : ssl.getTrustedX509Certificates()) {
                    try {
                        URL url = ResourceUtils.getURL(trustedCert);
                        Collection<? extends Certificate> certs = certificateFactory.generateCertificates(url.openStream());
                        allCerts.addAll(certs);
                    } catch (IOException e) {
                        throw new WebServerException("Could not load certificate '" + trustedCert + "'", e);
                    }
                }
                return allCerts.toArray(new X509Certificate[allCerts.size()]);
            } catch (CertificateException e1) {
                throw new WebServerException("Could not load CertificateFactory X.509", e1);
            }
        }

        // TODO: support configuration of other trust manager factories

        public boolean isUseInsecureTrustManager() {
            return useInsecureTrustManager;
        }

        public void setUseInsecureTrustManager(boolean useInsecureTrustManager) {
            this.useInsecureTrustManager = useInsecureTrustManager;
        }

        public Duration getHandshakeTimeout() {
            return handshakeTimeout;
        }

        public void setHandshakeTimeout(Duration handshakeTimeout) {
            this.handshakeTimeout = handshakeTimeout;
        }

        public Duration getCloseNotifyFlushTimeout() {
            return closeNotifyFlushTimeout;
        }

        public void setCloseNotifyFlushTimeout(Duration closeNotifyFlushTimeout) {
            this.closeNotifyFlushTimeout = closeNotifyFlushTimeout;
        }

        public Duration getCloseNotifyReadTimeout() {
            return closeNotifyReadTimeout;
        }

        public void setCloseNotifyReadTimeout(Duration closeNotifyReadTimeout) {
            this.closeNotifyReadTimeout = closeNotifyReadTimeout;
        }

        public SslProvider.DefaultConfigurationType getDefaultConfigurationType() {
            return defaultConfigurationType;
        }

        public void setDefaultConfigurationType(SslProvider.DefaultConfigurationType defaultConfigurationType) {
            this.defaultConfigurationType = defaultConfigurationType;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this).append("useInsecureTrustManager", useInsecureTrustManager)
                .append("trustedX509Certificates", trustedX509Certificates).append("handshakeTimeout", handshakeTimeout)
                .append("closeNotifyFlushTimeout", closeNotifyFlushTimeout)
                .append("closeNotifyReadTimeout", closeNotifyReadTimeout)
                .append("defaultConfigurationType", defaultConfigurationType).toString();
        }

    }

}
