//
//  NSObject+RSJSON.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "NSObject+RSJSON.h"
#import "MJExtension.h"

@implementation NSObject (RSJSON)

- (NSString *)toJSONString
{
    Class type = [self class];
    if ([type isSubclassOfClass:[NSNull class]]) {
        return @"null";
    } else if ([type isSubclassOfClass:[NSString class]]) {
        return [NSString stringWithFormat:@"\"%@\"", self];
    } else if ([type isSubclassOfClass:[NSNumber class]]) {
        return [self description];
    } else if ([type isSubclassOfClass:[NSDate class]]) {
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
        NSString * value = [dateFormatter stringFromDate:(NSDate *)self];
        return [value toJSONString];
    } else if ([type isSubclassOfClass:[NSArray class]]) {
        NSMutableString *json = [NSMutableString string];
        [json appendString:@"["];
        NSArray<NSObject *> *array = (NSArray<NSObject *> *)self;
        NSUInteger i = 0;
        for (NSObject *value in array) {
            if (i++ > 0) [json appendString:@", "];
            [json appendString:[value toJSONString]];
        }
        [json appendString:@"]"];
        return [NSString stringWithString:json];
    } else if ([type isSubclassOfClass:[NSDictionary class]]) {
        NSMutableString *json = [NSMutableString string];
        [json appendString:@"{"];
        NSDictionary<NSString *, NSObject *> *dictionary = (NSDictionary<NSString *, NSObject *> *)self;
        NSUInteger i = 0;
        for (NSString *key in dictionary) {
            if (i++ > 0) [json appendString:@", "];
            NSObject *value = [dictionary objectForKey:key];
            [json appendFormat:@"%@: %@", [key toJSONString], [value toJSONString]];
        }
        [json appendString:@"}"];
        return [NSString stringWithString:json];
    } else {
        return [[self mj_keyValues] toJSONString];
    }
}

- (id)toJSONObject
{
    Class type = [self class];
    if ([type isSubclassOfClass:[NSNull class]]) {
        return [NSNull null];
    } else if ([type isSubclassOfClass:[NSString class]]) {
        return [NSString stringWithString:(NSString *)self];
    } else if ([type isSubclassOfClass:[NSNumber class]]) {
        return self;
    } else if ([type isSubclassOfClass:[NSDate class]]) {
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
        NSString * value = [dateFormatter stringFromDate:(NSDate *)self];
        return [value toJSONObject];
    } else if ([type isSubclassOfClass:[NSArray class]]) {
        NSMutableArray<NSObject *> *json = [NSMutableArray<NSObject *> array];
        NSArray<NSObject *> *array = (NSArray<NSObject *> *)self;
        for (NSObject *value in array) {
            [json addObject:[value toJSONObject]];
        }
        return [NSArray arrayWithArray:json];
    } else if ([type isSubclassOfClass:[NSDictionary class]]) {
        NSMutableDictionary<NSString *, NSObject *> *json = [NSMutableDictionary dictionary];
        NSDictionary<NSString *, NSObject *> *dictionary = (NSDictionary<NSString *, NSObject *> *)self;
        [dictionary enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSObject * _Nonnull obj, BOOL * _Nonnull stop) {
            [json setObject:[obj toJSONObject] forKey:key];
        }];
        return [NSDictionary dictionaryWithDictionary:json];
    } else {
        return [[self mj_keyValues] toJSONObject];
    }
}

@end
