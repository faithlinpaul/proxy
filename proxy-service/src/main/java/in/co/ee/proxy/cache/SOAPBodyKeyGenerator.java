package in.co.ee.proxy.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

@Component
public class SOAPBodyKeyGenerator {

    private TransformerFactory transformerFactory;

    @Autowired
    public SOAPBodyKeyGenerator(TransformerFactory transformerFactory) {
        this.transformerFactory = transformerFactory;
    }


    public String generateKey(String soapBody) {
        if (soapBody != null) {
            try {
                return generateKeyFrom(soapBody);
            } catch (TransformerFactoryConfigurationError e) {
                rethrowTransformException(e);
            } catch (TransformerException e) {
                rethrowTransformException(e);
            } catch (Exception e) {
                // Return soapBody as the cache key
            }
        }
        return soapBody;
    }

    private String generateKeyFrom(String soapMessage) throws SOAPException, IOException, TransformerFactoryConfigurationError,
            TransformerException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage(null, new ByteArrayInputStream(soapMessage.getBytes()));
        SOAPBody soapBody = message.getSOAPBody();
        return soapBody == null ? soapMessage : toXmlString(soapBody);
    }

    private String toXmlString(Element element) throws TransformerFactoryConfigurationError, TransformerException {
        StringWriter stringWriter = new StringWriter();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(element), new StreamResult(stringWriter));
        return stringWriter.getBuffer().toString();
    }
    
    private void rethrowTransformException(Throwable t) {
        throw new RuntimeException("Could not convert Document object to XML string.", t);
    }
}
