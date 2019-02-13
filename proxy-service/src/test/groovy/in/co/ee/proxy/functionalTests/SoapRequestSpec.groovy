package in.co.ee.proxy.functionalTests

import groovyx.net.http.ContentType


class SoapRequestSpec extends IntegrationBaseSpec {
    def "SOAP Post request api test"() {
        when:
        def response = http.post(path: 'soap/services/ManagePrepayBoltons_1_0',
                requestContentType: ContentType.XML,
                body: """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:man="http://soa.o2.co.uk/manageprepayboltonsdata_1" xmlns:cor="http://soa.o2.co.uk/coredata_1">
                   <soapenv:Header/>
                   <soapenv:Body>
                      <man:getValidBoltons>
                         <man:customerId>
                            <!--You have a CHOICE of the next 3 items at this level-->
                            <cor:portalID>?</cor:portalID>
                            <cor:msisdn>447711011004</cor:msisdn>
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
                """)

        then:
        with(response) {
            status == 200
        }
        with(response.data) {
            Header == 'bf0e9570-d749-4a7a-b49a-1aa42e729088'
        }
    }


    def "SOAP error request api test"() {
        when:
        def response = http.post(path: 'soap/services/ManagePrepayBoltons_1_0',
                requestContentType: ContentType.XML,
                body: """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:man="http://soa.o2.co.uk/manageprepayboltonsdata_1" xmlns:cor="http://soa.o2.co.uk/coredata_1">
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
                """)

        then:
        with(response) {
            status == 500
        }
        with(response.data) {
            Header == 'bf0e9570-d749-4a7a-b49a-1aa42e729089'
        }
    }
}