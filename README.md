
# react-native-docusign-esign

## Getting started

`$ npm install react-native-docusign-esign --save`

### Installation 

`$ react-native link react-native-docusign-esign`


#### iOS 

`$ cd ios`
`$ pod init`

Open up your Podfile and remove the second "tvOS" target, and add the following line a few rows from the top: 

`pod "DocuSign.eSign"`

then run:

`$ pod install`

and open your new "*.xcworkspace" file in XCode 



#### Android 

You should only need to `react-native link react-native-docusign-esign` for this to work. 

If you receive errors about duplicate files, then add the following to your app's "build.gradle" file: 


    packagingOptions {
        pickFirst "META-INF/services/javax.ws.rs.ext.MessageBodyReader"
        pickFirst "META-INF/services/javax.ws.rs.ext.MessageBodyWriter"
        pickFirst "META-INF/services/com.sun.jersey.spi.inject.InjectableProvider"
        pickFirst "META-INF/jersey-module-version"
        pickFirst "META-INF/NOTICE"
        pickFirst "META-INF/LICENSE"
        pickFirst "META-INF/services/com.fasterxml.jackson.databind.Module"
        pickFirst "META-INF/services/com.fasterxml.jackson.core.ObjectCodec"
        pickFirst "META-INF/services/com.fasterxml.jackson.core.JsonFactory"
    }






## Usage
```javascript

var DocuSignBridge  = require('react-native-docusign-esign');
global.DSBridge = global.DocuSignBridge = DocuSignBridge;

var preAuthObj = {
  client_id: '',
  username: '',
  password: '',
  host: 'https://demo.docusign.net/restapi'
};
DocuSignBridge.setApiClient(preAuthObj)


DocuSignBridge.AuthenticationApi.login({}, function(err, result){
  if(err){
    alert('ERROR!');
    return;
  }

  alert(JSON.stringify(result));
  return;

});
...


```
  