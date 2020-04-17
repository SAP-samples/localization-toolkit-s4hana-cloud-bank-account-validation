# Poland: Using the White List (Register of Taxpayers)
This repository contains a sample application for the [Poland: Using the White List](https://blogs.sap.com/2020/04/16/poland-using-the-white-list/) tutorial.\
The White list is an electronic register of registered taxpayers, their tax numbers (NIPs) and bank accounts.  In some Polish business scenarios, a payer can only transfer funds to a white-listed bank account of the payee.  
This sample code is one part of the tutorial, so please follow the tutorial before attempting to use this code.

## Description
This application uses the SAP Cloud Platform (SCP) and Cloud Platform Integration (CPI) to integrate your SAP S/4HANA system with the White List, published daily by the Polish tax authorities.\ 
The application can be used to validate business partner tax numbers (NIPs) and bank accounts with the White list, and to manage such an extension on the SAP S/4HANA Cloud.  This extensibility is a part of [Localization Toolkit for SAP S/4HANA Cloud](https://community.sap.com/topics/localization-toolkit-s4hana-cloud).\
You can copy or deploy this sample application to your SAP Cloud Platform space.   

## Implementation
Within SAP S/4HANA, the Online Validation Framework is used to validate business partner tax numbers and bank account details.  This framework conducts checks at key processing points (such as during invoicing or during a payment run).  The framework is also used to complete mass validation checks, enabling validation results to be stored for improved system performance.  
If you would like to integrate your SAP S/4HANA system with the published White list using the SAP Cloud Platform, then you can use this sample application.  The application downloads the White list and serves the requests to validate the tax number and bank account number pairs.  The Online Validation Framework can then refer to this data for mass validation checks of business partners, individual validation checks (during master data creation or change), and validation checks which occur during invoicing and payment processing.
Information provided is published as a minimum working example.  You can use the sample application as a starting point and adapt it to your business requirements.\
### About the White List
The White list is published as a .json file compressed with the 7zip algorithm.  The White list is one-directional and uses specific conventions for the data it contains.

## Requirements
* You have administrative access to your SAP system and have implementation experience on this system.  Coding experience is also required to build the application.
* An SAP Cloud Platform (SCP) [Global account](https://cloudplatform.sap.com/index.html), and an SAP Cloud Platform subaccount in the Cloud Foundry environment.
* SAP Cloud Platform Services with Application Runtime (minimum 3 GB memory), and Application Logging (recommended).
* [Eclipse for JAVA Enterprise developers](https://www.eclipse.org/downloads/packages/) to build a JAVA application on your local PC.  Follow the installation guide for possible implementation of additional libraries.
* Oracle JAVA EE Development Kit (JDK) to compile the Java application. JDK might already be a part of the Eclipse bundle. If Eclipse fails to build the application because JDK is missing, [download](https://www.oracle.com/java/technologies/javaee-8-sdk-downloads.html) and install it on your local PC.  
* Apache Maven to build the Java application.  Maven should be a part of the Eclipse bundle. If Eclipse fails to build the application because Maven is missing, [download](https://maven.apache.org/download.cgi) and install it on your local PC.
* For more comprehensive information about tools or versioning, see the [Tools](https://tools.hana.ondemand.com/#cloud) page.

## Download and installation
* Run Eclipse. You will be prompted for a Workspace directory; this will be a common location for all Java projects.   
* Create a new directory on your PC for the application inside the Workspace directory. This folder will be further referred as **APP_HOME**.\
[Download or clone](https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository) this repository into the APP_HOME directory.  

## Compilation
The source code contains a project file (**.pom**) which will be recognized by Eclipse. 
Open the project file and build the application. 
* The dependent libraries should be automatically downloaded from a Maven repository during the build. You can reconfigure Eclipse to use a local Maven or Nexus directory. 
* You might also need to configure Eclipse network settings if you are using a HTTP Proxy. 
* The deployable artifact of the application should be created in APP_HOME/target with a **.war** suffix.  

## Deployment
Deploy the application (**.war**) into your organization's SCP space. 
* You can either use the SCP Cockpit and the Deploy button in your space, or you can use the [Cloud Foundry Command Line Interface - CLI](https://tools.hana.ondemand.com/#cloud). A useful tutorial on the CLI can be found [here](https://github.com/SAP-samples/hana-developer-cli-tool-example). 
* The parameters of the application (e.g. memory assignment) are set during the deployment. 
* The repository contains an example manifest.yml in APP_HOME directory. It is possible to use the manifest.yml directly after providing user-specific values.\
Ensure you save the manifest with unix-style line endings. E.g. replace\name = _<app_name>_\ with a value, that is likely to be unique, e.g.\ name = my_company_com_pl_whitelist
    

## Consumption of the end points
After the application has been deployed and run, navigate to the application in your space and you will see the application routes in the URL form. This route will be further referred to as **APP_ROUTE**.\
The application has two exposed endpoints, which are expected to be consumed in a client application.  
* **APP_ROUTE/download**\
This endpoint can be called (via HTTP GET) to download the White list data for current date.  Given the White list archive is approximately 200 MB, you should define an appropriate timeout value (minimum 3 minutes) to prevent timeout during downloading. 
* **APP_ROUTE/validate** \
This endpoint can be called (via HTTP GET) to validate the tax number/bank account pairs. The format of the HTTP is described in the Request.xsd and Response.xsd in the APP_HOME directory.  Processing error responses are described in the Error.xsd, for example 'White list data not available'.
       

## How to obtain Support
If you have issues with this sample, please visit the SAP community page or contact your SAP contact to obtain support. 
In case you observe any defect in the product usage itself, kindly use the SAP Product support channel and raise an incident adequately for the defects observed. 
You can also post questions directly to our [Community](https://answers.sap.com/questions/ask.html?primaryTagId=9af4d745-1754-4882-b057-f8f904c0a5f8).

## License
Copyright © 2020 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSE.txt) file.

