
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

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import android.util.Base64;
import java.nio.charset.StandardCharsets;


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

        // initialize client for desired environment and add X-DocuSign-Authentication header
        ApiClient apiClient = new ApiClient(config.getString("host"));

        if(config.hasKey("token")) {
            String token = config.getString("token");

            String authHeader = "Bearer " + token;
            apiClient.addDefaultHeader("Authorization", authHeader);

        } else {
            String username = config.getString("username");
            String password = config.getString("password");
            String integratorKey = config.getString("client_id");

            String authHeader = "{\"Username\":\"" +  username + "\",\"Password\":\"" +  password + "\",\"IntegratorKey\":\"" +  integratorKey + "\"}";
            apiClient.addDefaultHeader("X-DocuSign-Authentication", authHeader);

        }


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

        }
        catch (com.docusign.esign.client.ApiException ex)
        {
          System.out.println("Exception: " + ex);
          callback.invoke("error1");
        }

        

    }

    @ReactMethod
    public void envelopesApi_createEnvelope (
            String accountId,
            ReadableMap params_envelopeDefinition,
            ReadableMap params_envelopeOptions,
            Callback callback
    ) {

        try {
            EnvelopesApi envelopesApi = new EnvelopesApi();

            JSONObject envDefObj;
            JSONObject envOptsObj;
            try {
                envDefObj = JsonConvert.reactToJSON(params_envelopeDefinition);
                envOptsObj = JsonConvert.reactToJSON(params_envelopeOptions);
            }catch(org.json.JSONException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }


            EnvelopeDefinition envDef;
            try {
                envDef = new ObjectMapper().readValue(envDefObj.toString(), EnvelopeDefinition.class);
            }catch(java.io.IOException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            EnvelopesApi.CreateEnvelopeOptions envOpts = envelopesApi.new CreateEnvelopeOptions();
            envOpts.setCdseMode(params_envelopeOptions.getString("cdseMode"));
            envOpts.setMergeRolesOnDraft(params_envelopeOptions.getString("mergeRolesOnDraft"));

//            try {
////                envOpts = new ObjectMapper().readValue(envOptsObj.toString(), EnvelopesApi.CreateEnvelopeOptions.class);
////                envOpts = envOptsObj
//            }catch(java.io.IOException ex){
//                System.out.println("Exception: " + ex);
//                callback.invoke(ex.toString());
//                return;
//            }

            EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef, envOpts);

            ObjectWriter ow = new ObjectMapper().writer();

            String output;
            try {
                output = ow.writeValueAsString(envelopeSummary);
            }catch(com.fasterxml.jackson.core.JsonProcessingException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            JSONObject mainObject;
            WritableMap map;
            try {
                mainObject = new JSONObject(output);
                map = JsonConvert.jsonToReact(mainObject);
            }catch(org.json.JSONException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            callback.invoke(null,map);
            return;
        }
        catch (com.docusign.esign.client.ApiException ex)
        {
            System.out.println("Exception: " + ex);
            callback.invoke(ex.toString());
        }



    }

    @ReactMethod
    public void envelopesApi_createRecipientView (
            String accountId,
            String envelopeId,
            ReadableMap params_returnUrl,
            Callback callback
    ) {

        try {
            EnvelopesApi envelopesApi = new EnvelopesApi();

            JSONObject returnUrlObj;
            try {
                returnUrlObj = JsonConvert.reactToJSON(params_returnUrl);
            }catch(org.json.JSONException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            RecipientViewRequest recipientView;
            try {
                recipientView = new ObjectMapper().readValue(returnUrlObj.toString(), RecipientViewRequest.class);
            }catch(java.io.IOException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, recipientView);

            ObjectWriter ow = new ObjectMapper().writer();

            String output;
            try {
                output = ow.writeValueAsString(viewUrl);
            }catch(com.fasterxml.jackson.core.JsonProcessingException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            JSONObject mainObject;
            WritableMap map;
            try {
                mainObject = new JSONObject(output);
                map = JsonConvert.jsonToReact(mainObject);
            }catch(org.json.JSONException ex){
                System.out.println("Exception: " + ex);
                callback.invoke(ex);
                return;
            }

            callback.invoke(null,map);
            return;

        }
        catch (com.docusign.esign.client.ApiException ex)
        {
            System.out.println("Exception: " + ex);
            callback.invoke(ex.toString());
        }

    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }
}