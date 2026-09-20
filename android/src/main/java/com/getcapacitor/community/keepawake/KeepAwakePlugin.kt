package com.getcapacitor.community.keepawake

import android.view.WindowManager
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "KeepAwake")
public class KeepAwakePlugin : Plugin() {
    @PluginMethod
    public fun keepAwake(call: PluginCall) {
        bridge.executeOnMainThread {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            call.resolve()
        }
    }

    @PluginMethod
    public fun allowSleep(call: PluginCall) {
        bridge.executeOnMainThread {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            call.resolve()
        }
    }

    @PluginMethod
    public fun isSupported(call: PluginCall) {
        val ret = JSObject()
        ret.put("isSupported", true)
        call.resolve(ret)
    }

    @PluginMethod
    public fun isKeptAwake(call: PluginCall) {
        bridge.executeOnMainThread {
            // use the "bitwise and" operator to check if FLAG_KEEP_SCREEN_ON is on or off
            // credits: https://stackoverflow.com/a/24214209/9979122
            val flags = activity.window.attributes.flags
            val isKeptAwake = (flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0

            val ret = JSObject()
            ret.put("isKeptAwake", isKeptAwake)
            call.resolve(ret)
        }
    }
}
