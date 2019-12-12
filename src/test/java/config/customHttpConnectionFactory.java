package config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class customHttpConnectionFactory implements FactoryBean<HttpComponentsClientHttpRequestFactory>, InitializingBean {

    private HttpClient httpClient;
    private PoolingHttpClientConnectionManager connectionManager;

    @Override
    public HttpComponentsClientHttpRequestFactory getObject() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Override
    public Class<?> getObjectType() {
        return HttpComponentsClientHttpRequestFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (httpClient == null) {
            connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setDefaultMaxPerRoute(2);
            connectionManager.setMaxTotal(10);
            connectionManager.closeExpiredConnections();
            this.httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setRetryHandler(new DefaultHttpRequestRetryHandler(10, true)).build();
        }
    }
}
