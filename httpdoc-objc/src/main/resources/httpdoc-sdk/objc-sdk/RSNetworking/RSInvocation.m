//
//  RSInvocation.m
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/19.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import "RSInvocation.h"
#import "NSString+RSURLEncoder.h"

RSMethod const RSMethodHEAD = @"HEAD";
RSMethod const RSMethodGET = @"GET";
RSMethod const RSMethodPOST = @"POST";
RSMethod const RSMethodPUT = @"PUT";
RSMethod const RSMethodDELETE = @"DELETE";
RSMethod const RSMethodOPTIONS = @"OPTIONS";

@implementation RSParameter

- (instancetype)copyWithZone:(NSZone *)zone
{
    RSParameter *parameter = [[self class] allocWithZone:zone];
    parameter.name = _name;
    parameter.path = _path;
    parameter.scope = _scope;
    parameter.value = _value;
    return parameter;
}

@end

@implementation RSPart

- (instancetype)init
{
    self = [super init];
    if (self) {
        _headers = [NSMutableDictionary dictionary];
    }
    return self;
}

- (instancetype)initWithData:(NSData *)data forName:(NSString *)name mimeType:(NSString *)mimeType
{
    return [self initWithData:data forName:name mimeType:mimeType filename:nil];
}

- (instancetype)initWithData:(NSData *)data forName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename
{
    NSMutableDictionary<NSString *, NSString *> *headers = [NSMutableDictionary dictionary];
    NSString *disposition = @"form-data";
    if (name) {
        disposition = [NSString stringWithFormat:@"%@; name=\"%@\"", disposition, [name URLEncoded]];
    }
    if (filename) {
        disposition = [NSString stringWithFormat:@"%@; filename=\"%@\"", disposition, [filename URLEncoded]];
    }
    [headers setValue:disposition forKey:@"Content-Disposition"];
    [headers setValue:mimeType ? mimeType : @"application/octet-stream" forKey:@"Content-Type"];
    return [self initWithData:data forHeaders:headers];
}

- (instancetype)initWithData:(NSData *)data forHeaders:(NSDictionary<NSString *, NSString *> *)headers
{
    self = [self init];
    if (self) {
        _data = data;
        [_headers addEntriesFromDictionary:headers];
    }
    return self;
}

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name
{
    return [self initWithFile:file forName:name mimeType:nil];
}

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name mimeType:(NSString *)mimeType
{
    NSString *filename = [file lastPathComponent];
    return [self initWithFile:file forName:name mimeType:mimeType filename:filename];
}

- (instancetype)initWithFile:(NSURL *)file forName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename
{
    NSData *data = [[NSData alloc] initWithContentsOfURL:file];
    return [self initWithData:data forName:name mimeType:mimeType filename:filename];
}

@end

@implementation RSInvocation

+ (instancetype)HEAD:(NSString *)url
{
    return [self HTTP:RSMethodHEAD URL:url];
}

+ (instancetype)GET:(NSString *)url
{
    return [self HTTP:RSMethodGET URL:url];
}

+ (instancetype)POST:(NSString *)url
{
    return [self HTTP:RSMethodPOST URL:url];
}

+ (instancetype)PUT:(NSString *)url
{
    return [self HTTP:RSMethodPUT URL:url];
}

+ (instancetype)DELETE:(NSString *)url
{
    return [self HTTP:RSMethodDELETE URL:url];
}

+ (instancetype)OPTIONS:(NSString *)url
{
    return [self HTTP:RSMethodOPTIONS URL:url];
}

+ (instancetype)HTTP:(NSString *)method URL:(NSString *)url
{
    return [[[self class] alloc] initWithMethod:method url:url];
}

- (instancetype)initWithMethod:(NSString *)method url:(NSString *)url
{
    self = [self init];
    if (self) {
        _method = method;
        _url = url;
    }
    return self;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        _parameters = [NSMutableArray array];
        _headers = [NSMutableDictionary dictionary];
        _parts = [NSMutableArray array];
    }
    return self;
}

- (void)addPathValue:(id)value ofName:(NSString *)name
{
    [self addValue:value ofName:name toScope:RSScopePATH];
}

- (void)addMatrixValue:(id)value ofName:(NSString *)name
{
    [self addMatrixValue:value ofName:name forPath:@""];
}

- (void)addMatrixValue:(id)value ofName:(NSString *)name forPath:(NSString *)path
{
    [self addValue:value ofName:name forPath:path toScope:RSScopeMATRIX];
}

- (void)addQueryValue:(id)value ofName:(NSString *)name
{
    [self addValue:value ofName:name toScope:RSScopeQUERY];
}

- (void)addHeaderValue:(id)value ofName:(NSString *)name
{
    [self addValue:value ofName:name toScope:RSScopeHEADER];
}

- (void)addCookieValue:(id)value ofName:(NSString *)name
{
    [self addValue:value ofName:name toScope:RSScopeCOOKIE];
}

- (void)addBodyValue:(id)value
{
    [self addBodyValue:value ofName:@""];
}

- (void)addBodyValue:(id)value ofName:(NSString *)name
{
    [self addValue:value ofName:name toScope:RSScopeBODY];
}

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name
{
    [self addPartFile:file ofName:name mimeType:nil];
}

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name mimeType:(NSString *)mimeType
{
    NSString *filename = [file lastPathComponent];
    [self addPartFile:file ofName:name mimeType:mimeType filename:filename];
}

- (void)addPartFile:(NSURL *)file ofName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename
{
    NSData *data = [[NSData alloc] initWithContentsOfURL:file];
    [self addPartData:data ofName:name mimeType:mimeType filename:filename];
}

- (void)addPartData:(NSData *)data ofName:(NSString *)name mimeType:(NSString *)mimeType
{
    [self addPartData:data ofName:name mimeType:mimeType filename:nil];
}

- (void)addPartData:(NSData *)data ofName:(NSString *)name mimeType:(NSString *)mimeType filename:(NSString *)filename
{
    RSParameter *parameter = [[RSParameter alloc] init];
    parameter.value = data;
    parameter.name = name;
    parameter.scope = RSScopeBODY;
    parameter.mimeType = mimeType;
    parameter.filename = filename;
    [self addParameter:parameter];
}

- (void)addValue:(id)value ofName:(NSString *)name toScope:(RSScope)scope
{
    [self addValue:value ofName:name forPath:@"" toScope:scope];
}

- (void)addValue:(id)value ofName:(NSString *)name forPath:(NSString *)path toScope:(RSScope)scope
{
    RSParameter *parameter = [[RSParameter alloc] init];
    parameter.value = value;
    parameter.name = name;
    parameter.path = path;
    parameter.scope = scope;
    [self addParameter:parameter];
}

- (void)addParameter:(RSParameter *)parameter
{
    [_parameters addObject:parameter];
}

- (instancetype)copyWithZone:(NSZone *)zone
{
    RSInvocation *invocation = [[[self class] allocWithZone:zone] initWithMethod:_method url:_url];
    NSMutableArray<RSParameter *> *parameters = [NSMutableArray array];
    for (RSParameter *parameter in _parameters) [parameters addObject:[parameter copy]];
    invocation.parameters = parameters;
    invocation.resultType = _resultType;
    return invocation;
}

@end


@interface RSInterception ()

@property (nonatomic, copy) NSArray<id<RSInterceptor>> *interceptors;
@property (nonatomic, assign) NSUInteger index;

@end

@implementation RSInterception

- (instancetype)initWithInterceptors:(NSArray<id<RSInterceptor>> *)interceptors
{
    self = [super init];
    if (self) {
        _interceptors = interceptors;
        _index = 0;
    }
    return self;
}

- (void)doNext:(RSInvocation *)invocation callback:(void (^)(BOOL, id, NSError *))callback
{
    if (_index >= _interceptors.count) {
        callback(false, nil, nil);
        return;
    }
    id<RSInterceptor> interceptor = [_interceptors objectAtIndex:_index++];
    [interceptor intercept:invocation interception:self callback:callback];
}

@end
