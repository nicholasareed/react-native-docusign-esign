
import { NativeModules } from 'react-native';

const { RNDocuSignEsign } = NativeModules;

export default RNDocuSignEsign;


const DSEsignBridge = require('react-native').NativeModules.RNDocuSignEsign;


export default {

  setApiClient(config) {
    return DSEsignBridge.setApiClient(config);
  }

  AuthenticationApi: {
  	login(params, callback){
  		return DSEsignBridge.authenticationApi_login(params,callback);
  	}
  }

  FoldersApi: {

  }

}