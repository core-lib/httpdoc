//
//  RSClient.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RSInvocation.h"
#import "AFNetworking.h"

@protocol RSCall

- (void)resume;

- (void)resume:(void (^)(BOOL success, id result, NSError *error))callback;

@end

@protocol RSClient

- (id<RSCall>)invoke:(RSInvocation *)invocation callback:(void (^)(BOOL success, id result, NSError *error))callback;

- (void)addForeInterceptor:(id<RSInterceptor>)interceptor;

- (void)addBackInterceptor:(id<RSInterceptor>)interceptor;

@end

@interface RSAFNetworkingCall : NSObject<RSCall>

@end

@interface RSAFNetworkingClient : NSObject<RSClient>

- (instancetype)initWithSessionManager:(AFHTTPSessionManager *)sessionManager;

@end
