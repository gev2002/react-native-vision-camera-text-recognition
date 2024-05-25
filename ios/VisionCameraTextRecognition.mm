#import <Foundation/Foundation.h>
#import <VisionCamera/FrameProcessorPlugin.h>
#import <VisionCamera/FrameProcessorPluginRegistry.h>
#import <VisionCamera/Frame.h>


#if __has_include("VisionCameraTextRecognition/VisionCameraTextRecognition-Swift.h")
#import "VisionCameraTextRecognition/VisionCameraTextRecognition-Swift.h"
#else
#import "VisionCameraTextRecognition-Swift.h"
#endif

@interface VisionCameraTextRecognition (FrameProcessorPluginLoader)
@end

@implementation VisionCameraTextRecognition (FrameProcessorPluginLoader)
+ (void) load {
  [FrameProcessorPluginRegistry addFrameProcessorPlugin:@"scanText"
    withInitializer:^FrameProcessorPlugin*(VisionCameraProxyHolder* proxy, NSDictionary* options) {
    return [[VisionCameraTextRecognition alloc] initWithProxy:proxy withOptions:options];
  }];
}
@end
