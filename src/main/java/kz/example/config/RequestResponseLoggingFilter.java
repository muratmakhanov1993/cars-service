package kz.example.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        httpResponse.addHeader("X-Request-ID", httpRequest.getHeader("X-Request-ID"));
        httpResponse.addHeader("X-Caller-ID", httpRequest.getHeader("X-Caller-ID"));

        if (!requestWrapper.getRequestURI().contains("actuator")) {
            logRequest(requestWrapper);
            chain.doFilter(requestWrapper, responseWrapper);
            logResponse(requestWrapper, responseWrapper);
        } else {
            chain.doFilter(requestWrapper, responseWrapper);
        }
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request)
            throws UnsupportedEncodingException, JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(request.getMethod())
                .append(" ").append(request.getRequestURI())
                .append(" ").append(request.getProtocol());

        Map<String, String> requestHeaders = buildHeadersMap(request);
        sb.append("\nheaders: ").append(objectMapper.writeValueAsString(requestHeaders));

        Map<String, String> parameters = buildParametersMap(request);
        if (!parameters.isEmpty()) {
            sb.append("\nparams: ").append(parameters);
        }

        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        if (StringUtils.isNotEmpty(requestBody)) {
            sb.append("\nbody: ").append(requestBody);
        }
        log.info("Request: {}", sb);
    }

    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response)
            throws UnsupportedEncodingException, JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(response.getStatus())
                .append(" ").append(HttpStatus.valueOf(response.getStatus()).getReasonPhrase())
                .append(" ").append(request.getMethod())
                .append(" ").append(request.getRequestURI());

        Map<String, String> responseHeaders = buildHeadersMap(response);
        if (!responseHeaders.isEmpty()) {
            sb.append("\nheaders: ").append(objectMapper.writeValueAsString(responseHeaders));
        }

        String responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());
        sb.append("\nbody: ").append(responseBody);
        log.info("Response: {}", sb);
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }
}

