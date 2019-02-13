package in.co.ee.proxy.functionalTests

import groovyx.net.http.ContentType


class RestRequestSpec extends IntegrationBaseSpec {

    def "Post request api test"() {
        when:
        def response = http.post(path: 'rest/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : '1234'], body : [ 'foo':'bar'])

        then:
        with(response) {
            status == 201
            headers['Custom-Header'].value == 'I am present'
        }
        with(response.data) {
            id == 2
        }

        when: " with query param"
        response = http.post(path: 'rest/user?id=1', requestContentType: ContentType.JSON, body : [ 'foo':'bar'])

        then:
        with(response) {
            status == 201
        }
        with(response.data) {
            id == 2
        }
    }


    def "Get request api test"() {
        when:
        def response = http.get(path: 'rest/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : '1234'])

        then:
        with(response) {
            status == 200
            headers['Custom-Header'].value == 'I am present, are you?'
        }
        with(response.data) {
            id == 1
        }

        when: " with query param"
        response = http.get(path: 'rest/user?id=1', requestContentType: ContentType.JSON)

        then:
        with(response) {
            status == 200
        }
        with(response.data) {
            id == 1
        }
    }


    def "Put request api test"() {
        when:
        def response = http.put(path: 'rest/user/1', requestContentType: ContentType.JSON, headers: ['My-Custom-Header' : '1234'], body : [ foo:'bar'])

        then:
        with(response) {
            status == 200
            headers['Custom-Header'].value == 'I am present'
        }
        with(response.data) {
            id == 3
        }

        when: " with query param"
        response = http.put(path: 'rest/user?id=1', requestContentType: ContentType.JSON, body : [ foo:'bar'])

        then:
        with(response) {
            status == 200
        }
        with(response.data) {
            id == 3
        }
    }


}