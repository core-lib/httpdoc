//
//  RSPathHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSPathHandler.h"
#import "RSConversion.h"
#import "NSString+RSURLEncoder.h"
#import "NSArray+RSURLEncoder.h"
#import "NSArray+RSJoining.h"
#import "RSErrors.h"

@interface RSPathHandler ()

@property (nonatomic, strong) id<RSConverter> converter;

@end

@implementation RSPathHandler

- (instancetype)init
{
    self = [super init];
    if (self) {
        _converter = [RSDefaultConverter converter];
    }
    return self;
}

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    __block NSString *url = invocation.url;
    for (RSParameter *parameter in invocation.parameters) {
        if (parameter.scope != RSScopePATH || parameter.value == nil) continue;
        NSString *name = parameter.name;
        id value = parameter.value;
        NSDictionary<NSString *, NSArray<NSString *> *> *dictionary = [_converter convertValue:value forName:name];
        NSArray<NSString *> *array = [dictionary objectForKey:name];
        if (array == nil || array.count == 0) {
            NSString *reason = [NSString stringWithFormat:@"invalid argument in path variable: \"%@\"", name];
            NSDictionary *userInfo = [NSDictionary dictionaryWithObject:reason forKey:RSErrorUserInfoReasonKey];
            @throw [NSError errorWithDomain:RSNetworkingErrorDomain code:RSErrorCodeInvalidArgument userInfo:userInfo];
        }
        NSString *val = [array firstObject];
        val = [val URLEncoded];
        
        // 矩阵参数
        NSString *matrices = [self matrices:invocation parameter:parameter];
        if (matrices != nil && matrices.length > 0) {
            val = [NSString stringWithFormat:@"%@;%@", val, matrices];
        }
        
        // 替换路径上的占位符
        NSString *placeholder = [NSString stringWithFormat:@"{%@}", name];
        if (![url containsString:placeholder]) {
            NSString *reason = [NSString stringWithFormat:@"unknown path variable named: \"%@\"", name];
            NSDictionary *userInfo = [NSDictionary dictionaryWithObject:reason forKey:RSErrorUserInfoReasonKey];
            @throw [NSError errorWithDomain:RSNetworkingErrorDomain code:RSErrorCodeInvalidArgument userInfo:userInfo];
        }
        url = [url stringByReplacingOccurrencesOfString:placeholder withString:val];
    }
    invocation.url = url;
    [interception doNext:invocation callback:callback];
}

- (NSString *)matrices:(RSInvocation *)invocation parameter:(RSParameter *)parameter
{
    NSString *name = parameter.name;
    NSUInteger index = -1;
    for (RSParameter *param in invocation.parameters) {
        if (param.scope != RSScopePATH) continue;
        index++;
        if (param == parameter) break;
    }
    
    NSMutableString *matrices = [NSMutableString string];
    
    for (RSParameter *matrix in invocation.parameters) {
        if (matrix.scope != RSScopeMATRIX || matrix.value == nil) continue;
        NSString *path = matrix.path;
        // 矩阵变量的指定path和参数名称相同或没有指定path而且该参数是第一个路径参数
        if ([name isEqualToString:path] || ((path == nil || path.length == 0) && index == 0)) {
            NSDictionary<NSString *, NSArray<NSString *> *> *dictionary = [_converter convertValue:matrix.value forName:matrix.name];
            [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSArray<NSString *> * _Nonnull obj, BOOL * _Nonnull stop) {
                NSArray<NSString *> *values = obj;
                if (matrices.length > 0) [matrices appendString:@";"];
                [matrices appendString:[key URLEncoded]];
                [matrices appendString:@"="];
                [matrices appendString:[[values URLEncodeds] join]];
            }];
        }
    }
    
    return [NSString stringWithString:matrices];
}

@end
