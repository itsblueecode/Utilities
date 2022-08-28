package com.yadahs.utilities

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat

/**
 * @Author Dushane Coram
 * @Date 28, August, 2022
 * @Project Utilities
 * @Company Yadahs LLC,
 * @Location Atlanta, GA, United States of America
 * @Copyright (c) 2022 Yadahs LLC. All rights reserved.
 * @Description Provide utility methods to check and cache the current app version
 */
object AppVersionUtils {

    private const val LAST_APP_VERSION = "last_app_version"
    private const val APP_PREFS = "app_prefs"


    // GETTERS


    /**
     * @return int value represent the current app version. Return -1 if there was an error
     */
    private fun getCurrentAppVersion(context: Context): Int {
        return try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                val longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
                longVersionCode.toInt()
            }
        } catch (e: Exception) {
            Log.e(
                AppVersionUtils::class.java.canonicalName,
                "getCurrentAppVersion: error = " + e.message
            )
            -1
        }
    }

    /**
     * @return int value represent the last app version. Return -1 if this is the first time the app is being run or there was an error
     */
    private fun getLastAppVersion(context: Context): Int {
        return try {
            val prefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
            prefs.getInt(LAST_APP_VERSION, -1)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }


    // ACTION METHODS


    /**
     * check app versions.
     * @param context ..
     * @return whether the current app version is newer than the stored app version.
     */
    fun isNewAppVersion(context: Context): Boolean {
        return try {
            val lastAppVersion = getLastAppVersion(context)
            lastAppVersion == -1 || lastAppVersion < getCurrentAppVersion(context)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // SETTERS


    /**
     * @param context ...
     */
    fun setLastAppVersion(context: Context) {
        try {
            val prefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt(LAST_APP_VERSION, getCurrentAppVersion(context))
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}