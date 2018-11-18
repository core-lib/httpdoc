//
//  NSObject+RSJSON.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSObject (RSJSON)

- (NSString *)toJSONString;

- (id)toJSONObject;

@end
