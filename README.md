
# react-native-docusign-esign

## Getting started

`$ npm install react-native-docusign-esign --save`

### Installation 

`$ react-native link react-native-docusign-esign`
`$ cd ios`
`$ pod init`

Open up your Podfile and remove the second "tvOS" target, and add the following line a few rows from the top: 

`pod "DocuSign.eSign"`

then run:

`$ pod install`

and open your new "*.xcworkspace" file in XCode 



## Usage
```javascript

var DocuSignBridge  = require('react-native-docusign-esign');
global.DSBridge = global.DocuSignBridge = DocuSignBridge;

...


```
  