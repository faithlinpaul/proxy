package in.co.ee.proxy.functionalTests

import groovyx.net.http.ContentType


class CacheRequestSpec extends IntegrationBaseSpec {
    def "should get response from cache, if cache group header provided and delete cache after configured time"() {
        when: 'first request'
        def response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'userservice'])

        then:
        with(response) {
            status == 200
            headers['Custom-Header'].value == 'I am present, are you?'
            headers['Cached-Response'] == null
        }
        with(response.data) {
            id == 1
        }

        Thread.sleep(500)

        when: 'should not cache response for rest post'
        response = http.post(path: 'rest/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'userservice'], body : [ 'foo':'bar'])

        then:
        with(response) {
            status == 201
            headers['Cached-Response'] == null
        }

        when: 'cached response'
        response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'userservice'])

        then:
        with(response) {
            status == 200
            headers['Custom-Header'].value == 'I am present, are you?'
            headers['Cached-Response'].value == 'true'
        }
        with(response.data) {
            id == 1
        }

        //sleep for cache TTL expire
        Thread.sleep(2050)

        when: 'response from service as cached deleted'
        response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'userservice'])

        then:
        with(response) {
            status == 200
            headers['Custom-Header'].value == 'I am present, are you?'
            headers['Cached-Response'] == null
        }
        with(response.data) {
            id == 1
        }
    }

    def "should invalidate cache"() {
        when: 'first request'
        def response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'anotherservice'])
        then:
        with(response) {
            status == 200
            headers['Cached-Response'] == null
        }
        Thread.sleep(100)

        when: 'cached response'
        response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'anotherservice'])

        then:
        with(response) {
            status == 200
            headers['Cached-Response'].value == 'true'
        }

        when: 'invalidate cache'
        response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'anotherservice', 'Expire-Cache-Group' : 'true'])

        then:
        with(response) {
            status == 200
            headers['Cached-Response'] == null
        }

        Thread.sleep(100)

        when: 'fresh request made'
        response = http.get(path: 'cache/user/1', requestContentType: ContentType.JSON, headers: ['transaction-id' : transactionId(), 'Cache-Group' : 'anotherservice'])

        then:
        with(response) {
            status == 200
            headers['Cached-Response'] == null
        }
    }


}