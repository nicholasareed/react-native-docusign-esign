

#import "RNDocuSignEsign.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTConvert.h>

#import <DocuSign.eSign/DSApiClient.h>
#import <DocuSign.eSign/DSAuthenticationApi.h>
#import <DocuSign.eSign/DSEnvelopesApi.h>
#import <DocuSign.eSign/DSFoldersApi.h>

@implementation RNDocuSignEsign
@synthesize bridge = _bridge;

// global/shared variables for authentication
NSMutableString *DS_AUTH;
NSMutableString *DS_AUTH_HEADER;
DSApiClient* apiClient;


- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

// Export a native module
// https://facebook.github.io/react-native/docs/native-modules-ios.html
RCT_EXPORT_MODULE();

- (NSDictionary *)constantsToExport
{
  return @{
           // @"EXAMPLE": @"example"
         };
}

// Return the native view that represents your React component
- (UIView *)view
{
  return [[UIView alloc] init];
}

// Export methods to a native module
// https://facebook.github.io/react-native/docs/native-modules-ios.html
// (NSString *)name details:(NSDictionary *)details
RCT_EXPORT_METHOD(setApiClient:(NSDictionary *)authInfo)
{
  
  //NSString *IntegratorKey = @"e2697956-221c-40ad-bdae-57305eb5b389";
  NSString *IntegratorKey = [RCTConvert NSString:authInfo[@"client_id"]];
  NSString *username = [RCTConvert NSString:authInfo[@"username"]];
  NSString *password = [RCTConvert NSString:authInfo[@"password"]];
  NSString *host = [RCTConvert NSString:authInfo[@"host"]];
  
  // create authentication JSON string and header
  // NSString *const DS_AUTH
  DS_AUTH = [NSMutableString stringWithFormat:@"{\"Username\":\"%@\",\"Password\":\"%@\",\"IntegratorKey\":\"%@\"}", username, password, IntegratorKey];
  DS_AUTH_HEADER = @"X-DocuSign-Authentication";
  
  // instantiate api client, configure environment URL and assign auth data
  apiClient = [[DSApiClient alloc] initWithBaseURL:[[NSURL alloc] initWithString:host]];
  
  //callback(@[[NSNull null], @true]);
}


RCT_EXPORT_METHOD(authenticationApi_login:(NSDictionary *)param_loginOptions:(RCTResponseSenderBlock)callback)
{
  
  DSAuthenticationApi *authApi = [[DSAuthenticationApi alloc] initWithApiClient:apiClient];
  [authApi addHeader:DS_AUTH forKey:DS_AUTH_HEADER];
  
  NSError *error;
  DSAuthenticationApi_LoginOptions* loginOptions = [[DSAuthenticationApi_LoginOptions alloc] initWithDictionary:param_loginOptions error:&error];
  
  // Login to get the account for the user (if you have the accountId then skip this part)
  [authApi loginWithCompletionBlock:loginOptions completionHandler:^(DSLoginInformation *output, NSError *error) {
    if (error) {
      NSLog(@"got error %@", error);
      callback(@[error]);
      return;
    }
    if (!output) {
      NSLog(@"response can't be nil");
    }
    
    callback(@[[NSNull null], [output toDictionary]]);
    
    
  }]; // end login completion block
  
}

RCT_EXPORT_METHOD(foldersApi_listFolders:(NSString *)accountId:(RCTResponseSenderBlock)callback)
{
  
  DSFoldersApi *foldersApi = [[DSFoldersApi alloc] initWithApiClient:apiClient];
  [foldersApi addHeader:DS_AUTH forKey:DS_AUTH_HEADER];
  
  [foldersApi listWithCompletionBlock:accountId completionHandler:^(DSFoldersResponse *output, NSError *error) {
    
    if (error) {
      NSLog(@"got error %@", error);
      callback(@[error]);
      return;
    }
    
    callback(@[[NSNull null], [output toDictionary]]);
    
  }]; // end createEnvelopeWithAccountId
  
  
}


RCT_EXPORT_METHOD(foldersApi_listFolderItems:(NSString *)accountId:(NSString *)folderId:(RCTResponseSenderBlock)callback)
{
  
  DSFoldersApi *foldersApi = [[DSFoldersApi alloc] initWithApiClient:apiClient];
  [foldersApi addHeader:DS_AUTH forKey:DS_AUTH_HEADER];
  
  [foldersApi listItemsWithCompletionBlock:accountId folderId:folderId completionHandler:^(DSFolderItemsResponse *output, NSError *error) {
    
    if (error) {
      NSLog(@"got error %@", error);
      callback(@[error]);
      return;
    }
    
    callback(@[[NSNull null], [output toDictionary]]);
    
  }]; // end createEnvelopeWithAccountId
  
  
}


RCT_EXPORT_METHOD(envelopeApi_createEnvelope:(NSString *)accountId:(NSDictionary *)envelopeInfo:(NSDictionary *)params:(RCTResponseSenderBlock)callback)
{
  
  DSEnvelopesApi *envelopesApi = [[DSEnvelopesApi alloc] initWithApiClient:apiClient];
  [envelopesApi addHeader:DS_AUTH forKey:DS_AUTH_HEADER];
  
  // Create document from passed-in information
  // - TODO
  DSEnvelopeDefinition* envelopeDefinition = [[DSEnvelopeDefinition alloc] init];
  
  DSEnvelopesApi_CreateEnvelopeOptions* createEnvelopeOptions = [[DSEnvelopesApi_CreateEnvelopeOptions alloc] init];
  createEnvelopeOptions.cdseMode = @"false"; // [RCTConvert NSString:params[@"cdseMode"]];
  createEnvelopeOptions.mergeRolesOnDraft = @"false";
  
  [envelopesApi createEnvelopeWithCompletionBlock:accountId envelopeDefinition:envelopeDefinition options:createEnvelopeOptions completionHandler:^(DSEnvelopeSummary *output, NSError *error) {
    
    callback(@[[NSNull null], [output toDictionary]]);
    
  }]; // end createEnvelopeWithAccountId
  
  
}


//NSArray* jsonString = [DSObject arrayOfDictionariesFromModels: output.loginAccounts];
//callback(@[[NSNull null], jsonString]);

//DSLoginAccount *loginAccount = [output.loginAccounts objectAtIndex: 0];
//accountId = loginAccount.accountId;


//NSArray *events = [NSString stringWithFormat:accountId];
//NSArray *events = [loginAccount];
//callback(@[[NSNull null], events]);

// convert to dictionary
//NSDictionary *dict = [pm toDictionary];

// convert to json
//NSString *string = [loginAccount toJSONString];
//NSData *jsonData = [NSJSONSerialization dataWithJSONObject:output.loginAccounts options:NSJSONWritingPrettyPrinted error:&error];


#pragma mark - Private methods


@end
