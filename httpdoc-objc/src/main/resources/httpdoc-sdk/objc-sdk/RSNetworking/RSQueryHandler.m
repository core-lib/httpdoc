//
//  RSQueryHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSQueryHandler.h"
#import "RSConversion.h"
#import "NSString+RSURLEncoder.h"

@interface RSQueryHandler ()

@property (nonatomic, strong) id<RSConverter> converter;

@end

@implementation RSQueryHandler

- (instancetype)init
{
    self = [super init];
    if (self) {
        _converter = [RSDefaultConverter converter];
    }
    return self;
}

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    NSString *url = invocation.url;
    __block NSMutableString *query = [NSMutableString string];
    for (RSParameter *parameter in invocation.parameters) {
        if (parameter.scope != RSScopeQUERY || parameter.value == nil) continue;
        NSString *name = parameter.name;
        id value = parameter.value;
        NSDictionary<NSString *, NSArray<NSString *> *> *dictionary = [_converter convertValue:value forName:name];
        [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSArray<NSString *> * _Nonnull obj, BOOL * _Nonnull stop) {
            NSArray<NSString *> *values = obj;
            for (NSString *value in values) {
                if (query.length > 0) [query appendString:@"&"];
                [query appendString:[key URLEncoded]];
                [query appendString:@"="];
                [query appendString:[value URLEncoded]];
            }
        }];
    }
    if (query.length > 0) {
        if ([url containsString:@"?"]) {
            url = [NSString stringWithFormat:@"%@&%@", url, query];
        } else {
            url = [NSString stringWithFormat:@"%@?%@", url, query];
        }
    }
    invocation.url = url;
    [interception doNext:invocation callback:callback];
}

@end
