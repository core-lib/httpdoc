//
//  RSConversion.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>

// 转换器
@protocol RSConverter

- (NSDateFormatter *)getDateFormatter;

- (void)setDateFormatter:(NSDateFormatter *)dateFormatter;

- (BOOL)atomic:(Class)type;

- (NSDictionary<NSString *, NSArray<NSString *> *> *)convertValue:(id)value forName:(NSString *)name;

@end

// 规约器
@protocol RSReducer

- (BOOL)atomic;

- (BOOL)supports:(Class)type;

- (NSDictionary<NSString *, NSArray<NSString *> *> *)reduceValue:(id)value forName:(NSString *)name byConverter:(id<RSConverter>) converter;

@end

@interface RSDefaultConverter : NSObject<RSConverter>

+ (instancetype)converter;

@end

@interface RSStringReducer : NSObject<RSReducer>

@end;

@interface RSNumberReducer : NSObject<RSReducer>

@end;

@interface RSDateReducer : NSObject<RSReducer>

@end;

@interface RSArrayReducer : NSObject<RSReducer>

@end;

@interface RSSetReducer : NSObject<RSReducer>

@end

@interface RSDictionaryReducer : NSObject<RSReducer>

@end;
