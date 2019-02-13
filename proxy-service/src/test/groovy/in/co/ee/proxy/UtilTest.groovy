package in.co.ee.proxy

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest


class UtilTest extends Specification {
    def "should get service endpoint url"() {
        given:
        def request = Mock(HttpServletRequest)

        when:
        def actual = Util.potentialEndpointUrl(request)

        then:
        1 * request.getRequestURI() >> '/mock/foo/bar?cache=1'
        2 * request.getContextPath() >> ''
        actual == 'foo/bar?cache=1'
    }
}
