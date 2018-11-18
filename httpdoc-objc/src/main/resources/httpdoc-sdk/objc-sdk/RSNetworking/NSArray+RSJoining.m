//
//  NSArray+RSJoining.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/20.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "NSArray+RSJoining.h"

@implementation NSArray (RSJoining)

- (NSString *)join
{
    return [self joinWithDelimiter:@","];
}

- (NSString *)joinWithDelimiter:(NSString *)delimiter
{
    NSMutableString *joined = [NSMutableString string];
    NSArray<NSObject *> *array = (NSArray<NSObject *> *)self;
    for (NSUInteger i = 0; i < array.count; i++) {
        if (i > 0) [joined appendString:delimiter];
        NSObject *item = [array objectAtIndex:i];
        [joined appendString:[item description]];
    }
    return [NSString stringWithString:joined];
}

@end
