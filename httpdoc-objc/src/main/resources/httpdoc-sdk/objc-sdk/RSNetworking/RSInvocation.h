//
//  RSInvocation.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol RSInterceptor;

typedef NSString * RSMethod;

FOUNDATION_EXPORT RSMethod const RSMethodHEAD;
FOUNDATION_EXPORT RSMethod const RSMethodGET;
FOUNDATION_EXPORT RSMethod const RSMethodPOST;
FOUNDATION_EXPORT RSMethod const RSMethodPUT;
FOUNDATION_EXPORT RSMethod const RSMethodDELETE;
FOUNDATION_EXPORT RSMethod const RSMethodOPTIONS;

typedef NS_ENUM(NSUInteger, RSScope) {
    RSScopePATH,
    RSScopeMATRIX,
    RSScopeQUERY,
    RSScopeHEADER,
    RSScopeCOOKIE,
    RSScopeBODY
};

@interface RSParameter : NSObject<NSCopying>

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *path;
@property (nonatomic, assign) RSScope scope;
@property (nonatomic, strong) id value;
@property (nonatomic, copy) NSString *mimeType;
@property (nonatomic, copy) NSString *filename;

@end



@interface RSPart : NSObject

@property (nonatomic, strong) NSMutableDictionary<NSString *, NSString *> *headers;
@property (nonatomic, copy) NSData *data;

- (instancetype)initWithData:(NSData *)data forName:(NSString *)name mimeType:(NSString *)mimeType;

- (instancetype)initWithData:(NSData *)data forName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename;

- (instancetype)initWithData:(NSData *)data forHeaders:(NSDictionary<NSString *, NSString *> *)headers;

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name;

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name mimeType:(NSString *)mimeType;

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename;

@end



@interface RSInvocation : NSObject<NSCopying>

@property (nonatomic, copy) NSString *url;
@property (nonatomic, copy) RSMethod method;
@property (nonatomic, strong) NSMutableArray<RSParameter *> *parameters;
@property (nonatomic, strong) Class resultType;

@property (nonatomic, strong) NSMutableDictionary<NSString *, NSString *> *headers;
@property (nonatomic, strong) NSMutableArray<RSPart *> *parts;
@property (nonatomic, strong) id body;
@property (nonatomic, assign) BOOL multipart;

+ (instancetype)HEAD:(NSString *)url;

+ (instancetype)GET:(NSString *)url;

+ (instancetype)POST:(NSString *)url;

+ (instancetype)PUT:(NSString *)url;

+ (instancetype)DELETE:(NSString *)url;

+ (instancetype)OPTIONS:(NSString *)url;

+ (instancetype)HTTP:(NSString *)method URL:(NSString *)url;

- (instancetype)initWithMethod:(NSString *)method url:(NSString *)url;

- (void)addPathValue:(id)value ofName:(NSString *)name;

- (void)addMatrixValue:(id)value ofName:(NSString *)name;

- (void)addMatrixValue:(id)value ofName:(NSString *)name forPath:(NSString *)path;

- (void)addQueryValue:(id)value ofName:(NSString *)name;

- (void)addHeaderValue:(id)value ofName:(NSString *)name;

- (void)addCookieValue:(id)value ofName:(NSString *)name;

- (void)addBodyValue:(id)value;

- (void)addBodyValue:(id)value ofName:(NSString *)name;

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name;

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name mimeType:(NSString *)mimeType;

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename;

- (void)addPartData:(NSData *)data ofName:(NSString *)name mimeType:(NSString *)mimeType;

- (void)addPartData:(NSData *)data ofName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename;

- (void)addValue:(id)value ofName:(NSString *)name toScope:(RSScope)scope;

- (void)addValue:(id)value ofName:(NSString *)name forPath:(NSString *)path toScope:(RSScope)scope;

- (void)addParameter:(RSParameter *)parameter;

@end

@interface RSInterception : NSObject

- (instancetype)initWithInterceptors:(NSArray<id<RSInterceptor>> *)interceptors;

- (void)doNext:(RSInvocation *)invocation callback:(void (^)(BOOL success, id result, NSError *error))callback;

@end

@protocol RSInterceptor

- (void)intercept:(RSInvocation *)invocation interception:(RSInterception *)interception callback:(void (^)(BOOL success, id result, NSError *error))callback;

@end
