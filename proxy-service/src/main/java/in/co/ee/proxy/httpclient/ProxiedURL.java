package in.co.ee.proxy.httpclient;

import java.net.MalformedURLException;
import java.net.URL;

public class ProxiedURL {

    private final String proxiedServiceUrl;

    public ProxiedURL(String proxiedServiceUrl) {
        this.proxiedServiceUrl = proxiedServiceUrl;
    }

    public URL url() throws MalformedURLException {
        URL url = new URL(proxiedServiceUrl);
        if (url.getPort() == -1) {
            if (url.getProtocol().equalsIgnoreCase("HTTPS")) {
                return urlWithPort(url, ":443");
            }
            return urlWithPort(url, ":80");
        }
        return new URL(this.proxiedServiceUrl);
    }

    private URL urlWithPort(URL url, String port) throws MalformedURLException {
        return new URL(url.getProtocol() + "://" +url.getHost() + port + url.getPath());
    }

}
