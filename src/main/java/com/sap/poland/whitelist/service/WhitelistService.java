package com.sap.poland.whitelist.service;

import com.sap.poland.whitelist.controllers.BadRequestException;
import com.sap.poland.whitelist.controllers.NotFoundException;

/**
 * Poland: Whitelist of taxpayers - Validation service.
 * Polish MoF releases a white list of tax payers everyday. 
 * This service class can download the list and perform validations whether the 
 * validated entity is present in the whitelist. 
 * @author vagrant
 */
public interface WhitelistService {

    /**
     * Download the whitelist from the MoF portal for the day of request. 
     * The download takes about 2 minutes. The http timeout must be set accordingly in the client. 
     * Also consider when 
     * @return OK on success. 
     * @throws NotFoundException: The white list is not available on the expected URL. 
     */
    String downloadWhitelist() throws  BadRequestException, NotFoundException;
    
    /**
     * Perform validation of the taxpayer's bank account. 
     * @param xmlRequest XML with the requested entities for validation. 
     * @return XML with validation results. 
     * @throws BadRequestException The request has an invalid format. 
     */
    String validate(String xmlRequest ) throws BadRequestException;
}
