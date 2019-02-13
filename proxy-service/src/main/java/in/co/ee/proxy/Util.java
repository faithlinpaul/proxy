package in.co.ee.proxy;

import in.co.ee.proxy.httpclient.RequestedPath;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Util {
    public static String potentialProxiedServiceName(HttpServletRequest request) {
        String incomingURI = RequestedPath.servletPath(request);
        return incomingURI.split("/")[1];
    }

    public static String potentialEndpointUrl(HttpServletRequest request) {
        String incomingURI = RequestedPath.servletPath(request).replaceFirst("/", "");
        return StringUtils.substringAfter(incomingURI, "/");
    }
}
