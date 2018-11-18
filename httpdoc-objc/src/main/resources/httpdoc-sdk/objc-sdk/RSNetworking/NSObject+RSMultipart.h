//
//  NSObject+RSMultipart.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/30.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RSInvocation.h"

@interface NSObject (RSMultipart)

- (RSPart *)toMultipartForName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename;

- (bool)isMultipart;

- (bool)isMultipartFile;

- (bool)isMultipartArray;

- (bool)isMultipartDictionary;

@end
