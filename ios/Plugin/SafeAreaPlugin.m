#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(SafeAreaPlugin, "SafeArea",
           CAP_PLUGIN_METHOD(enable, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(disable, CAPPluginReturnNone);
)
