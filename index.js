
// import { 
// 	NativeModules,
// 	Platform
// } from 'react-native';

// const { RNDocuSignEsign } = NativeModules;

// export default RNDocuSignEsign;


const DSEsignBridge = require('react-native').NativeModules.RNDocuSignEsign;


module.exports = {

  setApiClient(config) {
    return DSEsignBridge.setApiClient(config);
  },

  AuthenticationApi: {
  	login(params, callback){
  		return DSEsignBridge.authenticationApi_login(params,callback);
  	}
  },

  FoldersApi: {

  }

}