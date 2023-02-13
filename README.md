[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/localization-toolkit-s4hana-cloud-bank-account-validation)](https://api.reuse.software/info/github.com/SAP-samples/localization-toolkit-s4hana-cloud-bank-account-validation)

# Poland: Using the List of VAT payers
<!-- This repository contains a sample application for the [Poland: Using the List of VAT payers](https://blogs.sap.com/2020/04/16/poland-using-the-white-list/) tutorial. -->
The List of VAT payers is an electronic register of registered taxpayers, their tax numbers (NIPs) and bank accounts.  In some Polish business scenarios, a payer can only transfer funds to a white-listed bank account of the payee.  

<!-- *This sample code is one part of the tutorial, so please follow the tutorial before attempting to use this code.* -->

## Description
This application uses the SAP Business Technology Platform (SAP BTP)[1] <!-- SAP Cloud Platform (SCP) --> and Cloud Platform Integration (CPI) to integrate your SAP S/4HANA system with the List of VAT payers, published daily by the Polish tax authorities. The application can be used to validate business partner tax numbers (NIPs) and bank accounts with the List of VAT payers, and to manage such an extension on the SAP S/4HANA Cloud. This extensibility is a part of [Localization Toolkit for SAP S/4HANA Cloud](https://community.sap.com/topics/localization-toolkit-s4hana-cloud).
You can copy or deploy this sample application to your SAP Business Technology Platform space.   
 

## Implementation
Within SAP S/4HANA, the Online Validation Framework is used to validate business partner tax numbers and bank account details.  This framework conducts checks at key processing points (such as during invoicing or during a payment run). The framework is also used to complete mass validation checks, enabling validation results to be stored for improved system performance.  
If you would like to integrate your SAP S/4HANA system with the published List of VAT payers using the Business Technology Platform, then you can use this sample application. The application downloads the List of VAT payers and serves the requests to validate the tax number and bank account number pairs. The Online Validation Framework can then refer to this data for mass validation checks of business partners, individual validation checks (during master data creation or change), and validation checks which occur during invoicing and payment processing.
Information provided is published as a minimum working example.  You can use the sample application as a starting point and adapt it to your business requirements.
### About the List of VAT payers
The List of VAT payers is published as a .json file compressed with the 7zip algorithm. The List of VAT payers is one-directional and uses specific conventions for the data it contains.

The application validates the bank accounts with the List of VAT payers and returns the checksum of the .json file in the form of SHA512 hash for each bank account successfully validated. This checksum is a confirmation that your data has been validated with the List of VAT payers. The Polish tax authorities considers this file and the validated data as proof of the validation. For more information about how to store and read the checksum, see the SAP Note [3041667](https://launchpad.support.sap.com/#/notes/3041667).
Note that, as part of SAP’s commitment to social justice and equality, SAP is replacing insensitive terms in our software and documentation with inclusive language – defined as “language free from expressions or words that reflect prejudice”. One of the terms we are replacing is the term “whitelist” in SAP documentation with “List of VAT payers” across our solution over time.


## Requirements
* You have administrative access to your SAP system and have implementation experience on this system. Coding experience is also required to build the application.
* An SAP Business Technology Platform (SAP BTP)  [Global account](https://cloudplatform.sap.com/index.html), and an SAP BTP subaccount in the Cloud Foundry environment.
* SAP BTP Services with Application Runtime (minimum 4 GB memory), and Application Logging (recommended). The recommened memory might change in the future as the published .7zip file will grow. 
* One of the following tools to build the app
### Business Application Studio
The [Business Application Studio](https://help.sap.com/docs/SAP%20Business%20Application%20Studio/9d1db9835307451daa8c930fbd9ab264/8f46c6e6f86641cc900871c903761fd4.html?locale=en-US) is a SAP provided all-in-one tool that allows you to build the application. Follow the [instructions](doc/BAS.md) for BAS. 
### Standard SDK tools
You can also use standard tools to build the application. The list of tools and necessary steps are described [here](doc/Eclipse.md). 

    
## Consumption of the end points
After the application has been deployed and run, navigate to the application in your space and you will see the application routes in the URL form. This route will be further referred to as **APP_ROUTE**.
The application has two exposed endpoints, which are expected to be consumed in a client application.  
* **APP_ROUTE/download**
This endpoint can be called (via HTTP GET) to download the List of VAT payers data for current date.  Given the List of VAT payers archive is approximately 200 MB, you should define an appropriate timeout value (minimum 3 minutes) to prevent timeout during downloading. 
* **APP_ROUTE/validate**
This endpoint can be called (via HTTP GET) to validate the tax number/bank account pairs. The format of the HTTP is described in the Request.xsd and Response.xsd in the APP_HOME directory.  Processing error responses are described in the Error.xsd, for example 'List of VAT payers data not available'.

## How to obtain Support
In case you have issues with this sample, you can:
* Check the GitHub repository for the latest changes. Alternatively you can check the SAP Note [3034848](https://launchpad.support.sap.com/#/notes/3034848). All changes of the GitHub repository will be also documented there. 
* Contact your SAP contact to obtain support. 
* Post questions directly to our [SAP Community](https://answers.sap.com/questions/ask.html?primaryTagId=9af4d745-1754-4882-b057-f8f904c0a5f8).
* Report an incident on [SAP Support Portal](https://support.sap.com/en/index.html), under the FI-LOC-OVF component

## License
Copyright © 2020 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSES/Apache-2.0.txt) file.

[1]: SAP decided to replace the previously used term SAP Cloud Platform with Business Technology Platform.