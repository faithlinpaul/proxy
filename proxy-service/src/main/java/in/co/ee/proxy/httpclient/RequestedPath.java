package in.co.ee.proxy.httpclient;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

//TODO check usage
public class RequestedPath {
    public static String servletPathWithoutProxidedServiceName(HttpServletRequest request, String serviceName) {
        return servletPath(request).replaceFirst("^/"+serviceName+"/", "/");
    }

    public static String servletPath(HttpServletRequest request) {
        if(request.getContextPath().equals("/")) {
            return request.getRequestURI();
        }
        return StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
    }
}
