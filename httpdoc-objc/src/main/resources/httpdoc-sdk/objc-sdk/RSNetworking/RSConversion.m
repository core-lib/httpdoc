//
//  RSConversion.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSConversion.h"
#import "MJExtension.h"

@interface RSDefaultConverter ()

@property (nonatomic, strong) NSDateFormatter *dateFormatter;
@property (nonatomic, strong) NSArray<id<RSReducer>> *reducers;

@end

@implementation RSDefaultConverter

+ (instancetype)converter
{
    static RSDefaultConverter *converter;
    if (converter) return converter;
    return [[[self class] alloc] init];
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        _dateFormatter = [[NSDateFormatter alloc] init];
        _dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
        _reducers = @[
                      [[RSStringReducer alloc] init],
                      [[RSNumberReducer alloc] init],
                      [[RSDateReducer alloc] init],
                      [[RSArrayReducer alloc] init],
                      [[RSSetReducer alloc] init],
                      [[RSDictionaryReducer alloc] init]
                    ];
    }
    return self;
}

- (NSDateFormatter *)getDateFormatter
{
    return _dateFormatter;
}

- (void)setDateFormatter:(NSDateFormatter *)dateFormatter
{
    _dateFormatter = dateFormatter;
}

- (BOOL)atomic:(Class)type
{
    for (id<RSReducer> reducer in _reducers) {
        if ([reducer supports:type]) {
            return [reducer atomic];
        }
    }
    return NO;
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)convertValue:(id)value forName:(NSString *)name
{
    if (value == nil) return [NSDictionary dictionary];
    if (name == nil) name = @"";
    
    Class type = [value class];
    for (id<RSReducer> reducer in _reducers) {
        if ([reducer supports:type]) {
            return [reducer reduceValue:value forName:name byConverter:self];
        }
    }
    
    id val = [value mj_keyValues];
    return [self convertValue:val forName:name];
}

@end


@implementation RSStringReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSString class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    return [NSDictionary dictionaryWithObject:@[value] forKey:name];
}

- (BOOL)atomic {
    return YES;
}


@end


@implementation RSNumberReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSNumber class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    return [NSDictionary dictionaryWithObject:@[[value description]] forKey:name];
}

- (BOOL)atomic {
    return YES;
}


@end


@implementation RSDateReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSDate class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    NSString *val = [[converter getDateFormatter] stringFromDate:value];
    return [NSDictionary dictionaryWithObject:@[val] forKey:name];
}

- (BOOL)atomic {
    return YES;
}


@end


@implementation RSArrayReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSArray class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    NSArray<NSObject *> *array = value;
    if (array.count == 0) {
        return [NSDictionary dictionary];
    }
    
    BOOL atomic = YES;
    for (NSObject *obj in array) atomic = atomic && [converter atomic:[obj class]];
    
    if (atomic) {
        NSMutableArray<NSString *> *merged = [NSMutableArray array];
        for (NSObject *val in array) {
            NSDictionary<NSString *, NSArray<NSString *> *> *dic = [converter convertValue:val forName:name];
            NSArray<NSString *> *arr = [dic objectForKey:name];
            [merged addObjectsFromArray:arr];
        }
        return [NSDictionary dictionaryWithObject:merged forKey:name];
    } else {
        NSMutableDictionary<NSString *, NSArray<NSString *> *> *dictionary = [NSMutableDictionary dictionary];
        for (NSUInteger i = 0; i < array.count; i ++) {
            NSObject *val = [array objectAtIndex:i];
            NSString *key = [NSString stringWithFormat:@"%@[%lu]", name, i];
            NSDictionary<NSString *, NSArray<NSString *> *> *dic = [converter convertValue:val forName:key];
            [dictionary addEntriesFromDictionary:dic];
        }
        return [NSDictionary dictionaryWithDictionary:dictionary];
    }
}

- (BOOL)atomic {
    return NO;
}


@end


@implementation RSSetReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSSet class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    NSSet<NSObject *> *set = value;
    if (set.count == 0) {
        return [NSDictionary dictionary];
    }
    
    BOOL atomic = YES;
    for (NSObject *obj in set) atomic = atomic && [converter atomic:[obj class]];
    
    if (atomic) {
        NSMutableArray *merged = [NSMutableArray array];
        for (NSObject *val in set) {
            NSDictionary *dic = [converter convertValue:val forName:name];
            NSArray *arr = [dic objectForKey:name];
            [merged addObjectsFromArray:arr];
        }
        return [NSDictionary dictionaryWithObject:merged forKey:name];
    } else {
        NSMutableDictionary<NSString *, NSArray<NSString *> *> *dictionary = [NSMutableDictionary dictionary];
        NSUInteger i = 0;
        for (NSObject *val in set) {
            NSString *key = [NSString stringWithFormat:@"%@[%lu]", name, i++];
            NSDictionary<NSString *, NSArray<NSString *> *> *dic = [converter convertValue:val forName:key];
            [dictionary addEntriesFromDictionary:dic];
        }
        return [NSDictionary dictionaryWithDictionary:dictionary];
    }
}

- (BOOL)atomic {
    return NO;
}


@end


@implementation RSDictionaryReducer

- (BOOL)supports:(Class)type
{
    return [type isSubclassOfClass:[NSDictionary class]];
}

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>)converter
{
    NSMutableDictionary<NSString *, NSArray<NSString *> *> *dictionary = [NSMutableDictionary dictionary];
    NSDictionary<NSString *, NSObject *> *dic = value;
    for (NSString *key in dic) {
        NSObject *v = [dic objectForKey:key];
        NSString *k = [NSString stringWithFormat:@"%@['%@']", name, key];
        NSDictionary<NSString *, NSArray<NSString *> *> *d = [converter convertValue:v forName:k];
        [dictionary addEntriesFromDictionary:d];
    }
    return dictionary;
}

- (BOOL)atomic {
    return NO;
}


@end
