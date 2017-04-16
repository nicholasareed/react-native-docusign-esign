
# react-native-docu-sign-esign

## Getting started

`$ npm install react-native-docu-sign-esign --save`

### Mostly automatic installation

`$ react-native link react-native-docu-sign-esign`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-docu-sign-esign` and add `RNDocuSignEsign.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNDocuSignEsign.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNDocuSignEsignPackage;` to the imports at the top of the file
  - Add `new RNDocuSignEsignPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-docu-sign-esign'
  	project(':react-native-docu-sign-esign').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-docu-sign-esign/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-docu-sign-esign')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNDocuSignEsign.sln` in `node_modules/react-native-docu-sign-esign/windows/RNDocuSignEsign.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Com.Reactlibrary.RNDocuSignEsign;` to the usings at the top of the file
  - Add `new RNDocuSignEsignPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript

var DocuSignBridge  = require('react-native-docusign-esign');
global.DSBridge = global.DocuSignBridge = DocuSignBridge;

...


```
  