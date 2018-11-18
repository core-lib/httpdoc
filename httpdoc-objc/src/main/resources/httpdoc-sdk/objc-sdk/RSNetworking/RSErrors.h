//
//  RSErrors.h
//  RSNetworking
//
//  Created by 杨昌沛 on 2018/7/21.
//  Copyright © 2018年 杨昌沛. All rights reserved.
//

#import <Foundation/Foundation.h>

FOUNDATION_EXPORT NSErrorDomain const RSNetworkingErrorDomain;

FOUNDATION_EXPORT NSErrorUserInfoKey const RSErrorUserInfoNameKey;
FOUNDATION_EXPORT NSErrorUserInfoKey const RSErrorUserInfoReasonKey;

typedef NS_ENUM(NSInteger, RSErrorCode) {
    RSErrorCodeUnknownFailure = 1,
    RSErrorCodeInvalidArgument
};
