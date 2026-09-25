package com.getcapacitor.community.keepawake

import android.view.WindowManager
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.PluginThread
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "KeepAwake")
public class KeepAwakePlugin : Plugin() {
    // The window flags belong to the main thread. keepAwake, allowSleep and isKeptAwake run there, in the order of
    // the calls.

    @PluginMethod(thread = PluginThread.MAIN)
    public fun keepAwake(call: PluginCall) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        call.resolve()
    }

    @PluginMethod(thread = PluginThread.MAIN)
    public fun allowSleep(call: PluginCall) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        call.resolve()
    }

    @PluginMethod
    public fun isSupported(call: PluginCall) {
        val ret = JSObject()
        ret.put("isSupported", true)
        call.resolve(ret)
    }

    @PluginMethod(thread = PluginThread.MAIN)
    public fun isKeptAwake(call: PluginCall) {
        // use the "bitwise and" operator to check if FLAG_KEEP_SCREEN_ON is on or off
        // credits: https://stackoverflow.com/a/24214209/9979122
        val flags = activity.window.attributes.flags
        val isKeptAwake = (flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0

        val ret = JSObject()
        ret.put("isKeptAwake", isKeptAwake)
        call.resolve(ret)
    }
}
