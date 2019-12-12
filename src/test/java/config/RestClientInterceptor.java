package config;

import com.consol.citrus.message.RawMessage;
import com.consol.citrus.report.MessageListeners;
import org.apache.logging.log4j.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class RestClientInterceptor implements ClientHttpRequestInterceptor {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static Logger log = (Logger) LoggerFactory.getLogger(RestClientInterceptor.class);

    @Autowired(required = false)
    private MessageListeners messageListeners;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        handleRequest(getRequestContent(httpRequest, new String(bytes)));
        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        CachingClientHttpResponseWrapper bufferedResponse = new CachingClientHttpResponseWrapper(response);
        handleRequest(getResponseContent(bufferedResponse));
        return bufferedResponse;
    }

    private String getResponseContent(CachingClientHttpResponseWrapper response) throws IOException {
        if (response != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("HTTP/1.1 ");
            builder.append(response.getRawStatusCode());
            builder.append(response.getStatusText());
            builder.append(NEWLINE);

            appendHeaders(response.getHeaders(), builder);

            builder.append(NEWLINE);
            builder.append(response.getBodyContent());
            return builder.toString();
        } else {
            return "";
        }
    }

    public void handleRequest(String response) {
        if (messageListeners != null) {
            log.debug("Sending http request message");
            messageListeners.onOutboundMessage(new RawMessage(response), null);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Sending Http request message: " + NEWLINE + response);
            }
        }
    }

    public String getRequestContent(HttpRequest request, String body) {
        StringBuilder builder = new StringBuilder();

        builder.append(request.getMethod());
        builder.append(" ");
        builder.append(request.getURI());
        builder.append(NEWLINE);

        appendHeaders(request.getHeaders(), builder);

        builder.append(NEWLINE);
        builder.append(body);
        return builder.toString();
    }

    public void appendHeaders(HttpHeaders headers, StringBuilder builder) {
        for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
            // This damn key
            builder.append(headerEntry.getKey());
            builder.append(":");
            builder.append(StringUtils.arrayToCommaDelimitedString(headerEntry.getValue().toArray()));
            builder.append(NEWLINE);
        }
    }

    private static final class CachingClientHttpResponseWrapper implements ClientHttpResponse {
        private final ClientHttpResponse response;
        private byte[] body;

        CachingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return this.response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.getStatusText();
        }

        @Override
        public void close() {
            this.response.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                if (response.getBody() != null) {
                    this.body = FileCopyUtils.copyToByteArray(response.getBody());
                } else {
                    body = new byte[]{};
                }
            }
            return new ByteArrayInputStream(this.body);
        }

        public String getBodyContent() throws IOException {
            if (this.body == null) {
                getBody();
            }
            return new String(body, Charset.forName("UTF-8"));
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.response.getHeaders();
        }
    }
}
