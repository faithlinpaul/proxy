package in.co.ee.proxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ConfigurationException;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {

    @Autowired
    private ProxyService service;

    @RequestMapping(value = "/**", method = GET)
    public ResponseEntity<byte[]> processGet(HttpEntity<String> httpEntity, HttpServletRequest request)
            throws Exception {
        return service.get(request, httpEntity);
    }

    @RequestMapping(value = "/**", method = POST)
    public ResponseEntity<byte[]> processPost(HttpEntity<String> httpEntity, HttpServletRequest request)
            throws Exception {
        return service.post(request, httpEntity);
    }

    @RequestMapping(value = "/**", method = PUT)
    public ResponseEntity<byte[]> processPut(HttpEntity<String> httpEntity, HttpServletRequest request)
            throws Exception {
        return service.put(request, httpEntity);
    }

    @RequestMapping(value = "/**", method = { PATCH, OPTIONS, HEAD, DELETE, TRACE })
    public ResponseEntity<byte[]> unSupportedMethods(HttpEntity<String> httpEntity, HttpServletRequest request)
            throws Exception {
        throw new ConfigurationException("Unsupported request method, only GET, POST, PUT, DELETE, TRACE are allowed");
    }
}

