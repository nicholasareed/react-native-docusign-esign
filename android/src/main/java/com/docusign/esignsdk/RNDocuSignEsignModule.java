
package com.docusign.esignsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.docusign.esign.api.*;
import com.docusign.esign.client.*;
import com.docusign.esign.model.*;

public class RNDocuSignEsignModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RNDocuSignEsign";
    private static ReactApplicationContext reactContext = null;

    public RNDocuSignEsignModule(ReactApplicationContext context) {
        // Pass in the context to the constructor and save it so you can emit events
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        super(context);

        reactContext = context;
    }

    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getConstants() {
        // Export any constants to be used in your native module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        final Map<String, Object> constants = new HashMap<>();
        // constants.put("EXAMPLE_CONSTANT", "example");

        return constants;
    }

    @ReactMethod
    public void setApiClient (
        ReadableMap config
        ) {
        
        String username = config.getString("username");
        String password = config.getString("password");
        String integratorKey = config.getString("client_id");
        
        // initialize client for desired environment and add X-DocuSign-Authentication header
        ApiClient apiClient = new ApiClient(config.getString("host"));
        
        // configure 'X-DocuSign-Authentication' authentication header
        String authHeader = "{\"Username\":\"" +  username + "\",\"Password\":\"" +  password + "\",\"IntegratorKey\":\"" +  integratorKey + "\"}";
        // If you have an OAuth access token stored in a variable named 'access_token', let's say, then instead, you can set authHeader as following (notice the extra space after 'Bearer'):
        // String authHeader = "{\"Bearer \":\"" +  access_token + "\"}";
        apiClient.addDefaultHeader("X-DocuSign-Authentication", authHeader);
        Configuration.setDefaultApiClient(apiClient);

    }

    @ReactMethod
    public void authenticationApi_login (
        ReadableMap params_loginOptions,
        Callback callback
        ) {

        try {
            AuthenticationApi authApi = new AuthenticationApi();
            LoginInformation loginInfo = authApi.login();

//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            ObjectWriter ow = new ObjectMapper().writer();

            String output;
            try {
                output = ow.writeValueAsString(loginInfo);
            }catch(com.fasterxml.jackson.core.JsonProcessingException ex){
                System.out.println("Exception: " + ex);
                callback.invoke("error1");
                return;
            }

            JSONObject mainObject;
            WritableMap map;
            try {
                mainObject = new JSONObject(output);
                map = JsonConvert.jsonToReact(mainObject);
            }catch(org.json.JSONException ex){
                System.out.println("Exception: " + ex);
                callback.invoke("error2");
                return;
            }

            callback.invoke(null,map);
            return;
//
//            try {
//                String json;
//                try {
//                    JSONObject json1 = new JSONObject(ow.writeValueAsString(loginInfo));
//                    json = json1.toString();
//                } catch (org.json.JSONException ex)
//                {
//                    System.out.println("Exception: " + ex);
//                    callback.invoke("error2");
//                    return;
//                }
//                callback.invoke(null, json);
//            } catch (com.fasterxml.jackson.core.JsonProcessingException ex)
//            {
//                System.out.println("Exception: " + ex);
//                callback.invoke("error1");
//                return;
//            }

//            WritableMap output = Arguments.createMap();

        }
        catch (com.docusign.esign.client.ApiException ex)
        {
          System.out.println("Exception: " + ex);
          callback.invoke("error1");
        }

        

    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }
}

//private <Any> Any castSnapshot(Any snapshot) {
//    String type = snapshot.getClass().getName();
//    if (snapshot.hasChildren()) {
//        WritableMap data = Arguments.createMap();
//        for (DataSnapshot child : snapshot.getChildren()) {
//            Any castedChild = castSnapshot(child);
//            switch (castedChild.getClass().getName()) {
//                case "java.lang.Boolean":
//                    data.putBoolean(child.getKey(), (Boolean) castedChild);
//                    break;
//                case "java.lang.Integer":
//                    data.putInt(child.getKey(), (Integer) castedChild);
//                    break;
//                case "java.lang.Double":
//                    data.putDouble(child.getKey(), (Double) castedChild);
//                    break;
//                case "java.lang.String":
//                    data.putString(child.getKey(), (String) castedChild);
//                    break;
//                case "com.facebook.react.bridge.WritableNativeMap":
//                    data.putMap(child.getKey(), (WritableMap) castedChild);
//                    break;
//            }
//        }
//        return (Any) data;
//    } else {
//        String type = snapshot.getValue().getClass().getName();
//        switch (type) {
//            case "java.lang.Boolean":
//                return (Any)((Boolean) snapshot.getValue());
//            case "java.lang.Long":
//                // TODO check range errors
//                return (Any)((Integer)(((Long) snapshot.getValue()).intValue()));
//            case "java.lang.Double":
//                return (Any)((Double) snapshot.getValue());
//            case "java.lang.String":
//                return (Any)((String) snapshot.getValue());
//            case "java.lang.Object":
//                return (Any)((Object) snapshot.getValue());
//        }
//    }
//}






// import com.facebook.react.bridge.ReactApplicationContext;
// import com.facebook.react.bridge.ReactContextBaseJavaModule;
// import com.facebook.react.bridge.ReactMethod;
// import com.facebook.react.bridge.Callback;

// public class RNDocuSignEsignModule extends ReactContextBaseJavaModule {

//   private final ReactApplicationContext reactContext;

//   public RNDocuSignEsignModule(ReactApplicationContext reactContext) {
//     super(reactContext);
//     this.reactContext = reactContext;
//   }

//   @Override
//   public String getName() {
//     return "RNDocuSignEsign";
//   }
// }