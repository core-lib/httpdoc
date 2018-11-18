//
//  NSString+URLEncoder.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "NSString+RSURLEncoder.h"

@implementation NSString (RSURLEncoder)

- (NSString *)URLEncoded
{
    return [self stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLPathAllowedCharacterSet]];
}

@end
