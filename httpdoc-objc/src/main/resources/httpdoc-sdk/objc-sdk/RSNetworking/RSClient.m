//
//  RSClient.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSClient.h"
#import "RSPathHandler.h"
#import "RSQueryHandler.h"
#import "RSHeaderHandler.h"
#import "RSCookieHandler.h"
#import "RSBodyHandler.h"
#import "RSResultHandler.h"
#import "RSErrors.h"

@interface RSAFNetworkingCall ()

@property (nonatomic, strong) RSInvocation *invocation;
@property (nonatomic, strong) RSAFNetworkingClient *client;
@property (nonatomic, strong) void (^callback)(BOOL, id, NSError *);

- (instancetype)initWithInvocation:(RSInvocation *)invocation
                            client:(RSAFNetworkingClient *)client
                          callback:(void (^)(BOOL, id, NSError *))callback;

@end

@implementation RSAFNetworkingCall

- (instancetype)initWithInvocation:(RSInvocation *)invocation
                            client:(RSAFNetworkingClient *)client
                          callback:(void (^)(BOOL, id, NSError *))callback
{
    self = [super init];
    if (self) {
        _invocation = invocation;
        _client = client;
        _callback = callback;
    }
    return self;
}

- (void)resume
{
    [self resume:_callback];
}

- (void)resume:(void (^)(BOOL, id, NSError *))callback
{
    [_client invoke:_invocation callback:callback];
}

@end

@interface RSAFNetworkingClient ()<RSInterceptor>

@property (nonatomic, strong) AFHTTPSessionManager *sessionManager;
@property (nonatomic, strong) NSMutableArray<id<RSInterceptor>> *interceptors;

@end

@implementation RSAFNetworkingClient

- (instancetype)init
{
    self = [super init];
    if (self) {
        NSArray<id<RSInterceptor>> *plugins = @[
                                [[RSPathHandler alloc] init],
                                [[RSQueryHandler alloc] init],
                                [[RSHeaderHandler alloc] init],
                                [[RSCookieHandler alloc] init],
                                [[RSBodyHandler alloc] init],
                                [[RSResultHandler alloc] init]
                            ];
        _interceptors = [NSMutableArray arrayWithArray:plugins];
    }
    return self;
}

- (instancetype)initWithSessionManager:(AFHTTPSessionManager *)sessionManager
{
    self = [self init];
    if (self) {
        _sessionManager = sessionManager;
    }
    return self;
}

- (id<RSCall>)invoke:(RSInvocation *)invocation callback:(void (^)(BOOL, id, NSError *))callback
{
    @try {
        RSInvocation *copied = [invocation copy];
        NSMutableArray<id<RSInterceptor>> *interceptors = [NSMutableArray arrayWithArray:_interceptors];
        [interceptors addObject:self];
        RSInterception *interception = [[RSInterception alloc] initWithInterceptors:interceptors];
        [interception doNext:copied callback:callback];
        return [[RSAFNetworkingCall alloc] initWithInvocation:invocation client:self callback:callback];
    } @catch(NSError *error) {
        dispatch_async(_sessionManager.completionQueue ?: dispatch_get_main_queue(), ^{
            callback(NO, nil, error);
        });
        return nil;
    } @catch(NSException *exception) {
        dispatch_async(_sessionManager.completionQueue ?: dispatch_get_main_queue(), ^{
            NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
            if (exception.name) [userInfo setObject:exception.name forKey:RSErrorUserInfoNameKey];
            if (exception.reason) [userInfo setObject:exception.reason forKey:RSErrorUserInfoReasonKey];
            if (exception.userInfo) [userInfo addEntriesFromDictionary:exception.userInfo];
            NSError *error = [NSError errorWithDomain:RSNetworkingErrorDomain code:RSErrorCodeUnknownFailure userInfo:userInfo];
            callback(NO, nil, error);
        });
        return nil;
    }
}

- (void)addForeInterceptor:(id<RSInterceptor>)interceptor
{
    [_interceptors setObject:interceptor atIndexedSubscript:0];
}

- (void)addBackInterceptor:(id<RSInterceptor>)interceptor
{
    [_interceptors addObject:interceptor];
}

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback
{
    if (invocation.multipart) {
        [self dataTaskWithHTTPMethod:invocation.method
                           URLString:invocation.url
                             headers:invocation.headers
                          parameters:invocation.body
                               parts:invocation.parts
                             success:^(NSURLSessionDataTask *task, id responseObject) {
                                 callback(true, responseObject, nil);
                             }
                             failure:^(NSURLSessionDataTask *task, NSError *error) {
                                 callback(false, nil, error);
                             }];
    } else {
        [self dataTaskWithHTTPMethod:invocation.method
                           URLString:invocation.url
                             headers:invocation.headers
                          parameters:invocation.body
                             success:^(NSURLSessionDataTask *task, id responseObject) {
                                 callback(true, responseObject, nil);
                             }
                             failure:^(NSURLSessionDataTask *task, NSError *error) {
                                 callback(false, nil, error);
                             }];
    }
}

- (NSURLSessionDataTask *)dataTaskWithHTTPMethod:(NSString *)method
                                       URLString:(NSString *)URLString
                                         headers:(NSDictionary<NSString *, NSString *> *)headers
                                      parameters:(id)parameters
                                         success:(void (^)(NSURLSessionDataTask * task, id responseObject))success
                                         failure:(void (^)(NSURLSessionDataTask * task, NSError *error))failure
{
    NSError *serializationError = nil;
    AFHTTPRequestSerializer<AFURLRequestSerialization> * serializer = _sessionManager.requestSerializer;
    NSMutableURLRequest *request = [serializer requestWithMethod:method
                                                       URLString:[[NSURL URLWithString:URLString relativeToURL:_sessionManager.baseURL] absoluteString]
                                                      parameters:parameters
                                                           error:&serializationError];
    
    if (serializationError) {
        if (failure) {
            dispatch_async(_sessionManager.completionQueue ?: dispatch_get_main_queue(), ^{
                failure(nil, serializationError);
            });
        }
        
        return nil;
    }
    
    for (NSString *key in headers) {
        [request setValue:[headers valueForKey:key] forHTTPHeaderField:key];
    }
    
    __block NSURLSessionDataTask *dataTask = nil;
    dataTask = [_sessionManager dataTaskWithRequest:request
                                     uploadProgress:nil
                                   downloadProgress:nil
                                  completionHandler:^(NSURLResponse * __unused response, id responseObject, NSError *error) {
                                      if (error) {
                                          if (failure) {
                                              failure(dataTask, error);
                                          }
                                      } else {
                                          if (success) {
                                              success(dataTask, responseObject);
                                          }
                                      }
                                  }];
    
    [dataTask resume];
    
    return dataTask;
}


- (NSURLSessionDataTask *)dataTaskWithHTTPMethod:(NSString *)method
                                       URLString:(NSString *)URLString
                                         headers:(NSDictionary<NSString *, NSString *> *)headers
                                      parameters:(id)parameters
                                           parts:(NSArray<RSPart *> *)parts
                                         success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                                         failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure
{
    NSError *serializationError = nil;
    AFHTTPRequestSerializer<AFURLRequestSerialization> * serializer = _sessionManager.requestSerializer;
    NSMutableURLRequest *request = [serializer multipartFormRequestWithMethod:method
                                                                    URLString:[[NSURL URLWithString:URLString relativeToURL:_sessionManager.baseURL] absoluteString]
                                                                   parameters:parameters
                                                    constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
                                                        for (RSPart *part in parts) {
                                                            [formData appendPartWithHeaders:part.headers body:part.data];
                                                        }
                                                    }
                                                                        error:&serializationError];
    if (serializationError) {
        if (failure) {
            dispatch_async(_sessionManager.completionQueue ?: dispatch_get_main_queue(), ^{
                failure(nil, serializationError);
            });
        }
        
        return nil;
    }
    
    for (NSString *key in headers) {
        [request setValue:[headers valueForKey:key] forHTTPHeaderField:key];
    }
    
    __block NSURLSessionDataTask *task = [_sessionManager uploadTaskWithStreamedRequest:request
                                                                               progress:nil
                                                                      completionHandler:^(NSURLResponse * __unused response, id responseObject, NSError *error){
                                                                          if (error) {
                                                                              if (failure) {
                                                                                  failure(task, error);
                                                                              }
                                                                          } else {
                                                                              if (success) {
                                                                                  success(task, responseObject);
                                                                              }
                                                                          }
                                                                      }];
    
    [task resume];
    
    return task;
}

@end
