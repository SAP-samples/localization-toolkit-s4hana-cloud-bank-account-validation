# Deployment with standard tools

Make sure that the following tools are available in your computer:
* [Eclipse for JAVA Enterprise developers](https://www.eclipse.org/downloads/packages/) to build a JAVA application on your compute.  Follow the installation guide for possible implementation of additional libraries.
* Oracle JAVA EE Development Kit (JDK) to compile the Java application. JDK might already be a part of the Eclipse bundle. If Eclipse fails to build the application because JDK is missing, [download](https://www.oracle.com/java/technologies/javaee-8-sdk-downloads.html) and install it on your computer.  
* Apache Maven to build the Java application. Maven should be a part of the Eclipse bundle. If Eclipse fails to build the application because Maven is missing, [download](https://maven.apache.org/download.cgi) and install it on your computer.
* For more comprehensive information about tools or versioning, see the [Tools](https://tools.hana.ondemand.com/#cloud) page.

## Download and installation
* Run Eclipse. You will be prompted for a Workspace directory; this will be a common location for all Java projects.   
* Create a new directory on your computer for the application inside the Workspace directory. This folder will be further referred as **APP_HOME**.
* [Download or clone](https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository) this repository into the APP_HOME directory.  

## Compilation
The source code contains a project file (**.pom**) which will be recognized by Eclipse. 
Open the project file and build the application. 
* The dependent libraries should be automatically downloaded from a Maven repository during the build. You can reconfigure Eclipse to use a local Maven or Nexus directory. 
* You might also need to configure Eclipse network settings if you are using a HTTP Proxy. 
* The deployable artifact of the application should be created in APP_HOME/target with a **.war** suffix.  

## Deployment
Deploy the application (**.war**) into your organization's SAP BTP space. 
* You can either use the SCP Cockpit and the Deploy button in your space, or you can use the [Cloud Foundry Command Line Interface - CLI](https://tools.hana.ondemand.com/#cloud). A useful tutorial on the CLI can be found [here](https://github.com/SAP-samples/hana-developer-cli-tool-example). 
* The parameters of the application (e.g. memory assignment) are set during the deployment. 
* The repository contains an example manifest.yml in APP_HOME directory. It is possible to use the manifest.yml directly after providing user-specific values.
Ensure you save the manifest with unix-style line endings. E.g. replace\name = _<app_name>_ with a value, that is likely to be unique, e.g. name = my_company_com_pl_whitelist

## Consumption of the end points
Continue with the setup of the consumption of the end points described on the [main page](../README.md). 