//
//  RSResultHandler.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSResultHandler.h"
#import "MJExtension.h"

@implementation RSResultHandler

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL, id, NSError *))callback {
    [interception doNext:invocation callback:^(BOOL success, id result, NSError *error) {
        Class resultType = invocation.resultType;
        if (!success || resultType == nil) {
            callback(success, result, error);
            return;
        }
        if ([result isKindOfClass:[NSArray class]]) {
            id objs = [resultType mj_objectArrayWithKeyValuesArray:result];
            callback(success, objs, error);
        } else {
            id obj = [resultType mj_objectWithKeyValues:result];
            callback(success, obj, error);
        }
    }];
}

@end
