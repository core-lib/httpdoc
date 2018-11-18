//
//  RSHeaderHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSHeaderHandler.h"
#import "RSConversion.h"
#import "NSString+RSURLEncoder.h"

@interface RSHeaderHandler ()

@property (nonatomic, strong) id<RSConverter> converter;

@end

@implementation RSHeaderHandler

- (instancetype)init
{
    self = [super init];
    if (self) {
        _converter = [RSDefaultConverter converter];
    }
    return self;
}

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    for (RSParameter *parameter in invocation.parameters) {
        if (parameter.scope != RSScopeHEADER || parameter.value == nil) continue;
        NSString *name = parameter.name;
        id value = parameter.value;
        NSDictionary<NSString *, NSArray<NSString *> *> *dictionary = [_converter convertValue:value forName:name];
        [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSArray<NSString *> * _Nonnull obj, BOOL * _Nonnull stop) {
            NSArray<NSString *> *values = obj;
            for (NSString *value in values) {
                [invocation.headers setObject:[value URLEncoded] forKey:[key URLEncoded]];
            }
        }];
    }
    [interception doNext:invocation callback:callback];
}

@end
