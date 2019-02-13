package in.co.ee.proxy.functionalTests

import groovyx.net.http.ContentType

class HttpsSOAPCacheRequestSpec extends IntegrationBaseSpec {
    def SOAPBody = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:man="http://soa.ee.co.uk/manageprepayboltonsdata_1" xmlns:cor="http://soa.ee.co.uk/coredata_1">
                       <soapenv:Header/>
                       <soapenv:Body>
                          <man:getValidBoltons>
                             <man:customerId>
                                <cor:msisdn>447711011004</cor:msisdn>
                             </man:customerId>
                          </man:getValidBoltons>
                       </soapenv:Body>
                    </soapenv:Envelope>"""

    def "should get response from cache, if cache group header provided and delete cache after configured time"() {
        when: 'first request'
        def response = http.post(path: 'ssl/services/ManagePrepayBoltons_1_0', requestContentType: ContentType.XML,
                headers: ['Cache-Group': '447711011004'], body: SOAPBody)

        then:
        with(response) {
            status == 200
            headers['Cached-Response'] == null
        }

        Thread.sleep(100)
        when: 'cached response'
        response = http.post(path: 'ssl/services/ManagePrepayBoltons_1_0', requestContentType: ContentType.XML,
                headers: ['Cache-Group': '447711011004'], body: SOAPBody)

        then:
        with(response) {
            status == 200
            headers['Cached-Response'].value == 'true'
        }

        //sleep for cache TTL expire
        Thread.sleep(2050)

        when: 'response from service as cached deleted'
        response = http.post(path: 'ssl/services/ManagePrepayBoltons_1_0', requestContentType: ContentType.XML,
                headers: ['Cache-Group': '447711011004'], body: SOAPBody)

        then:
        with(response) {
            status == 200
            headers['Cached-Response'] == null
        }
    }

    def "should not cache if SOAP service sends an error"() {

        String request = """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:man="http://soa.ee.co.uk/manageprepayboltonsdata_1" xmlns:cor="http://soa.ee.co.uk/coredata_1">
           <soapenv:Header/>
           <soapenv:Body>
              <man:getValidBoltons>
                 <man:customerId>
                    <!--You have a CHOICE of the next 3 items at this level-->
                    <cor:portalID>?</cor:portalID>
                    <cor:msisdn>447711011005</cor:msisdn>
                    <cor:accountDetail>
                       <cor:billingAccountType>?</cor:billingAccountType>
                       <cor:accountNumber>?</cor:accountNumber>
                       <cor:msisdn>?</cor:msisdn>
                    </cor:accountDetail>
                 </man:customerId>
                 <man:boltonType>?</man:boltonType>
              </man:getValidBoltons>
           </soapenv:Body>
        </soapenv:Envelope>
        """
        when:"error from soa"
        def response = http.post(path: 'ssl/services/ManagePrepayBoltons_1_0', requestContentType: ContentType.XML,
                headers: ['Cache-Group': '447711011005'], body: request)

        then:
        with(response) {
            status == 500
            headers['Cached-Response'] == null
        }
        with(response.data) {
            Header == 'bf0e9570-d749-4a7a-b49a-1aa42e729089'
        }

        Thread.sleep(100)
        when: 'again same request'
        response = http.post(path: 'ssl/services/ManagePrepayBoltons_1_0', requestContentType: ContentType.XML,
                headers: ['Cache-Group': '447711011005'], body: request)

        then:
        with(response) {
            status == 500
            headers['Cached-Response'] == null
        }
    }


}