//
//  RSBodyHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSBodyHandler.h"
#import "NSObject+RSJSON.h"
#import "NSString+RSURLEncoder.h"
#import "NSObject+RSMultipart.h"
#import "MJExtension.h"

@implementation RSBodyHandler

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    NSUInteger bodies = 0;
    BOOL upload = NO;
    for (RSParameter *parameter in invocation.parameters) {
        if (parameter.scope != RSScopeBODY) continue;
        bodies++;
        upload = upload || [parameter.value isMultipart];
    }
    BOOL multipart = bodies > 1 || upload;
    invocation.multipart = multipart;
    
    if (multipart) {
        for (RSParameter *parameter in invocation.parameters) {
            if (parameter.scope != RSScopeBODY || parameter.value == nil) continue;
            
            if ([parameter.value isMultipartDictionary]) {
                NSDictionary *dictionary = parameter.value;
                [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString *  _Nonnull key, id  _Nonnull value, BOOL * _Nonnull stop) {
                    NSString *name = parameter.name;
                    name = name && name.length > 0 ? [NSString stringWithFormat:@"%@.%@", name, key] : key;
                    RSPart *part = [value toMultipartForName:name
                                            mimeType:parameter.mimeType
                                            filename:parameter.filename];
                    [invocation.parts addObject:part];
                }];
            } else if ([parameter.value isMultipartArray]) {
                NSArray *array = parameter.value;
                [array enumerateObjectsUsingBlock:^(id  _Nonnull value, NSUInteger idx, BOOL * _Nonnull stop) {
                    RSPart *part = [value toMultipartForName:parameter.name
                                            mimeType:parameter.mimeType
                                            filename:parameter.filename];
                    [invocation.parts addObject:part];
                }];
            } else if ([parameter.value isMultipartFile]) {
                RSPart *part = [parameter.value toMultipartForName:parameter.name
                                        mimeType:parameter.mimeType
                                        filename:parameter.filename];
                [invocation.parts addObject:part];
            } else {
                NSString *json = [parameter.value toJSONString];
                NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
                RSPart *part = [[RSPart alloc] initWithData:data
                                                    forName:parameter.name
                                                   mimeType:@"application/json"];
                [invocation.parts addObject:part];
            }
        }
    } else {
        for (RSParameter *parameter in invocation.parameters) {
            if (parameter.scope != RSScopeBODY || parameter.value == nil) continue;
            invocation.body = [parameter.value toJSONObject];
        }
    }
    
    [interception doNext:invocation callback:callback];
}


@end
