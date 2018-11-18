//
//  RSCookieHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSCookieHandler.h"
#import "RSConversion.h"
#import "NSString+RSURLEncoder.h"

@interface RSCookieHandler ()

@property (nonatomic, strong) id<RSConverter> converter;

@end

@implementation RSCookieHandler

- (instancetype)init
{
    self = [super init];
    if (self) {
        _converter = [RSDefaultConverter converter];
    }
    return self;
}

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    NSString *cookie = [invocation.headers objectForKey:@"Cookie"];
    NSMutableString *builder = cookie ? [NSMutableString stringWithString:cookie] : [NSMutableString string];
    for (RSParameter *parameter in invocation.parameters) {
        if (parameter.scope != RSScopeCOOKIE || parameter.value == nil) continue;
        NSString *name = parameter.name;
        id value = parameter.value;
        NSDictionary<NSString *, NSArray<NSString *> *> *dictionary = [_converter convertValue:value forName:name];
        [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSArray<NSString *> * _Nonnull obj, BOOL * _Nonnull stop) {
            NSArray *values = obj;
            for (NSString *value in values) {
                if (builder.length > 0) [builder appendString:@"; "];
                [builder appendString:[key URLEncoded]];
                [builder appendString:@"="];
                [builder appendString:[value URLEncoded]];
            }
        }];
    }
    if (builder != nil && builder.length > 0) {
        [invocation.headers setObject:builder forKey:@"Cookie"];
    }
    [interception doNext:invocation callback:callback];
}

@end
