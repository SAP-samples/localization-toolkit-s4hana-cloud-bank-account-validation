package com.sap.poland.whitelist.service;

import com.sap.poland.whitelist.controllers.BadRequestException;
import com.sap.poland.whitelist.controllers.NotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class WhitelistServiceImpl implements WhitelistService {

    private static final Character EXCEMPTED_TAXPAYER = 'E';
    private static final Character ACTIVE_TAXPAYER = 'A';
    /*
  private final HeaderRepository _headers;
  private final ItemRepository _items;
  private final MaskRepository _masks; 
  private final EntityManagerFactory _entityManagerFactory;
     */
    private Whitelist _whitelist;
    private static final String REQUEST_ELEMENT = "Req";
    private static final String RESULT_ELEMENT = "Res";
    private static final String BANK_ACC_ELEMENT = "BA";
    private static final String TAX_NO_ELEMENT = "TaxNo";
    private static final String VALID_ELEMENT = "Valid";
    private static final String REQUESTSET_ELEMENT = "ValRequests";
    private static final String RESULTSET_ELEMENT = "ValResponses";
    private static final String RESULT_VALID = "1";
    private static final String RESULT_INVALID = "0";
    private final WhitelistProcessor _whitelistProcessor;

    /*
  public WhitelistServiceImpl(ItemRepository items, MaskRepository masks, HeaderRepository headers, EntityManagerFactory entityManagerFactory) {
    
      _items = items;
    _masks = masks;
    _headers = headers;
    _entityManagerFactory = entityManagerFactory;
    
  }
     */
    
    @Inject
    public WhitelistServiceImpl(WhitelistProcessor processor) {
        _whitelistProcessor = processor;
    }
    
    @Override
    public String downloadWhitelist() {
        try {
            String dateNow = KeyDate.now().yyyyMMdd();
            if (_whitelist != null && _whitelist.getDate().equals(dateNow)) {
                return "OK";
            }
            _whitelist = null; 
            System.gc();
            Whitelist list = _whitelistProcessor.downloadAndProcess(dateNow);
            _whitelist = list;
            System.gc();
            return "OK";
        } catch (Exception ex) {
            Logger.getLogger(WhitelistServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
    
    /**
     * Validates the Tax Number/Bank account pairs againts the whitelist. 
     * @param validator Instance of the validator.
     * @param xmlRequest XML with the requests
     * @return XML with the results
     * @throws BadRequestException Incorrect format of the request xml. 
     * @throws NullPointerException Any of the input parameter is null. 
     */
    String performValidation(Validator validator, String xmlRequest) throws BadRequestException {
        Objects.requireNonNull(validator, "validator");
        Objects.requireNonNull(xmlRequest, "xmlRequest");
        try {
            Document sourceDocument = XMLUtils.createDocumentFromString(xmlRequest);
            Document targetDocument = XMLUtils.newDocument();
            Element resultRoot = targetDocument.createElement(RESULTSET_ELEMENT);
            targetDocument.appendChild(resultRoot);

            // Get list of separated requests
            Element sourceRoot = sourceDocument.getDocumentElement();
            if(!sourceRoot.getNodeName().equals(REQUESTSET_ELEMENT)) {
                String msg = "XML parse error: ValRequest expected, found: " + sourceRoot.getNodeName();
                throw new BadRequestException(msg);
            }
            
            List<Element> requests = XMLUtils.getChildElements(sourceRoot, REQUEST_ELEMENT);
            for (Element request : requests) {
                String bankAccount = XMLUtils.getValueOfFirstChild(request, BANK_ACC_ELEMENT);
                String taxNo = XMLUtils.getValueOfFirstChild(request, TAX_NO_ELEMENT);
                boolean isValid = validator.validate(taxNo, bankAccount);
                appendResult(resultRoot, bankAccount, taxNo, isValid);
            }

            return XMLUtils.documentToString(targetDocument);
        } catch (XMLUtils.XMLException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    void appendResult(Element responseRoot, String bankAccount, String taxNo, boolean isValid) {
        String result = isValid ? RESULT_VALID : RESULT_INVALID;
        Element response = XMLUtils.createAppendChildElement(responseRoot, RESULT_ELEMENT, null);
        XMLUtils.createAppendChildElement(response, TAX_NO_ELEMENT, taxNo);
        XMLUtils.createAppendChildElement(response, BANK_ACC_ELEMENT, bankAccount);
        XMLUtils.createAppendChildElement(response, VALID_ELEMENT, result);
    }

    @Override
    public String validate(String xmlRequest) {
        String dateNow = KeyDate.now().yyyyMMdd();
        
        if (_whitelist == null || _whitelist.getDate() == null || !_whitelist.getDate().equals(dateNow)) {
            String msg = "Data not downloaded for " + dateNow;
            throw new NotFoundException(msg);
        }

        ValidatorMem validator = new ValidatorMem(_whitelist);
        return performValidation(validator, xmlRequest);
    }

}
