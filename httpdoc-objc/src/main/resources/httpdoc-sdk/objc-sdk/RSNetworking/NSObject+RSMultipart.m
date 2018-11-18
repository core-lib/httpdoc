//
//  NSObject+RSMultipart.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/30.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "NSObject+RSMultipart.h"

@implementation NSObject (RSMultipart)

- (RSPart *)toMultipartForName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename
{
    id value = self;
    if ([value isKindOfClass:[NSURL class]]) {
        NSURL *file = value;
        RSPart *part = [[RSPart alloc] initWithFile:file
                                            forName:name
                                           mimeType:mimeType
                                           filename:filename ? filename : [file lastPathComponent]];
        return part;
    } else if ([value isKindOfClass:[RSPart class]]) {
        RSPart *part = value;
        return part;
    } else if ([value isKindOfClass:[NSData class]]) {
        RSPart *part = [[RSPart alloc] initWithData:value
                                            forName:name
                                           mimeType:mimeType
                                           filename:filename];
        return part;
    } else {
        return nil;
    }
}

- (bool)isMultipart
{
    return [self isMultipartFile]
    || [self isMultipartArray]
    || [self isMultipartDictionary];
}

- (bool)isMultipartFile
{
    id value = self;
    return [value isKindOfClass:[NSURL class]]
    || [value isKindOfClass:[NSData class]]
    || [value isKindOfClass:[RSPart class]];
}

- (bool)isMultipartArray
{
    id value = self;
    if (!value || ![value isKindOfClass:[NSArray class]]) return NO;
    NSArray *array = value;
    if (array.count == 0) return NO;
    bool multipart = YES;
    for (id val in array) {
        multipart &= [val isMultipartFile];
    }
    return multipart;
}

- (bool)isMultipartDictionary
{
    id value = self;
    if (!value || ![value isKindOfClass:[NSDictionary class]]) return NO;
    NSDictionary *dictionary = value;
    if (dictionary.count == 0) return NO;
    bool multipart = YES;
    for (id key in dictionary) {
        id val = [dictionary objectForKey:key];
        multipart &= [key isKindOfClass:[NSString class]] && ([val isMultipartFile] || [val isMultipartArray]);
    }
    return multipart;
}

@end
