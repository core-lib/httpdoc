//
//  NSArray+RSURLEncoder.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "NSArray+RSURLEncoder.h"
#import "NSString+RSURLEncoder.h"

@implementation NSArray (RSURLEncoder)

- (NSArray<NSString *> *)URLEncodeds
{
    NSMutableArray<NSString *> *encodeds = [NSMutableArray arrayWithCapacity:self.count];
    for (id url in self) {
        [encodeds addObject:[[url description] URLEncoded]];
    }
    return [NSArray arrayWithArray:encodeds];
}

@end
