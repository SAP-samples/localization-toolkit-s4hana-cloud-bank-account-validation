package com.sap.poland.whitelist.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.poland.whitelist.controllers.NotFoundException;
import com.sap.poland.whitelist.controllers.ProcessingFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class WhitelistProcessor {
    private static final String URL_PREFIX = "https://plikplaski.mf.gov.pl/pliki/";
    private final int TIMEOUT_SEC = 10 * 60;  // 10 mins 
    private static final String SEVENZIP_SUFFIX = ".7z";
    private static final String JSON_SUFFIX = ".json";
    private static final String SHA512_SUFFIX = ".sha512sum";
    private static final String SHA512 = "SHA512";
    private static final Logger logger = LoggerFactory.getLogger(WhitelistProcessor.class);
    
    /**
     * Download and parse the Whitelist data. 
     * @param keyDate Date of the whitelist (yyyyMMdd format)
     * @return Complete whitelist data. 
     * @throws Exception
     */
    public Whitelist downloadAndProcess(String keyDate) throws NotFoundException {
        String zipName = keyDate + SEVENZIP_SUFFIX;
        String jsonName = keyDate + JSON_SUFFIX;
        String sha512File = jsonName + SHA512_SUFFIX;

        logger.atTrace().log("Download for " + keyDate + " started.");
        try {
            // Download
            byte[] zippedData = download(zipName);
            
            if (zippedData == null) {
                throw new IOException("White list cannot be downloaded. ");
            }
            // Extract
            byte[] rawData = SevenZip.extract(zippedData, jsonName);
            if (rawData == null) {
                throw new IOException("Json file cannot be extracted from the 7zip file.");
            }

            // Parse
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            Whitelist whitelist = mapper.readValue(rawData, Whitelist.class);
            
            byte[] checkSumData = SevenZip.extract(zippedData, sha512File);
            if ( checkSumData == null ) {
                throw new IOException("Json file cannot be extracted from the 7zip file.");
            }
            
            String checkSum = new String(checkSumData, StandardCharsets.UTF_8);
            String[] checkSumComponents = checkSum.split(" ");
            if(checkSumComponents.length > 0){
                whitelist.setCheckSum(checkSumComponents[0], SHA512);
            } else {
                logger.atInfo().log("Download for " + keyDate + " started.");
            }

            logger.atTrace().log("Download for " + keyDate + " finished.");
         
            return whitelist;
        } catch (IOException ioEx) {
            throw new NotFoundException(ioEx.getMessage());
        } catch (Exception ex) {
            throw new ProcessingFailedException(ex);
        }
    }
    

    private byte[] download(String filename) throws MalformedURLException, IOException {
        String address = URL_PREFIX + filename;
        URL url = new URL(address);
        URLConnection conn = url.openConnection(); // can throw exception if bad url
        conn.setConnectTimeout(TIMEOUT_SEC * 1000);
        conn.setReadTimeout(TIMEOUT_SEC * 1000);
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try (InputStream is = conn.getInputStream()) {
            try (BufferedInputStream in = new BufferedInputStream(is)) {
                final byte data[] = new byte[8192];
                int count;
                while ((count = in.read(data)) > 0) {
                    bout.write(data, 0, count);
                }
            }
        }
        return bout.toByteArray();
        
    }
}
