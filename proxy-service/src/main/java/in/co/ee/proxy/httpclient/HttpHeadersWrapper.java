package in.co.ee.proxy.httpclient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.springframework.http.HttpHeaders;

public class HttpHeadersWrapper extends HttpHeaders {

	private static final String TRANSFER_ENCODING_HEADER_NAME = "Transfer-Encoding";

	public HttpHeadersWrapper(Header[] headers) {
		copyAcceptableHeaders(headers);
    }

	private void copyAcceptableHeaders(Header[] headers) {
		for (Header header : headers) {
			if (isAcceptable(header)) {
				add(header.getName(), header.getValue());
			}
        }
	}

	private boolean isAcceptable(Header header) {
		return isNotTransferEncoding(header);
	}

	private boolean isNotTransferEncoding(Header header) {
		return !StringUtils.equalsIgnoreCase(header.getName(), TRANSFER_ENCODING_HEADER_NAME);
	}
}