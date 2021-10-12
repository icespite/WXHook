package com.icespite.wxhook;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WXHook implements IXposedHookLoadPackage {
    public static final String TAG = "IceSpite-";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        try {
            if (HookConfig.TOAST_SWITCH) {
//                hookToast(loadPackageParam);
                hookSign(loadPackageParam);
            }

        } catch (Exception e) {
            Log.i(TAG, " load exception:" + Log.getStackTraceString(e));
            XposedBridge.log(TAG + "errorinfo: " + e.toString());
        }
    }

    private void hookToast(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.tencent.mm")) {
            XposedBridge.log(TAG + "HookToastReady");
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass("android.widget.Toast", loadPackageParam.classLoader), "makeText", Context.class, CharSequence.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(TAG + "BeforeToastHook: " + param.args[0] + " " + param.args[1] + " " + param.args[2] + " ");
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(TAG + "AfterToastHook: " + param.getResult());
                    XposedBridge.log("Dump Stack: " + "---------------start----------------");
                    Throwable ex = new Throwable();
                    StackTraceElement[] stackElements = ex.getStackTrace();
                    if (stackElements != null) {
                        for (int i = 0; i < stackElements.length; i++) {

                            XposedBridge.log("Dump Stack" + i + ": " + stackElements[i].getClassName()
                                    + "----" + stackElements[i].getFileName()
                                    + "----" + stackElements[i].getLineNumber()
                                    + "----" + stackElements[i].getMethodName());
                        }
                    }
                    XposedBridge.log("Dump Stack: " + "---------------over----------------");
                    Context context = ((Application) param.args[0]).getApplicationContext();
                    Toast.makeText(context, "FinishHookToast", Toast.LENGTH_LONG).show();
                    super.afterHookedMethod(param);
                }
            });
        }
    }

    private void hookSign(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.tencent.mm")) {
            XposedBridge.log(TAG + "HookSignReady");

            Class<?> dumpClass = XposedHelpers.findClass("com.tencent.mm.pluginsdk.model.app.s", loadPackageParam.classLoader);
            Class<?> fclass2 = XposedHelpers.findClass("com.tencent.mm.pluginsdk.model.app.g", loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(dumpClass, "a", Context.class, fclass2, String.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + "BeforeSignHook: " + param.args[0] + " " + param.args[1] + " " + param.args[2] + " ");
                            param.setResult(true);
                        }
                    });
        }
    }

    // 获取指定名称的类声明的类成员变量、类方法、内部类的信息
    public void dumpClass(Class<?> actions) {
        XposedBridge.log("Dump class " + actions.getName());
        XposedBridge.log("Methods");
        // 获取到指定名称类声明的所有方法的信息
        Method[] m = actions.getDeclaredMethods();
        // 打印获取到的所有的类方法的信息
        for (int i = 0; i < m.length; i++) {

            XposedBridge.log(m[i].toString());
        }
        XposedBridge.log("Fields");
        // 获取到指定名称类声明的所有变量的信息
        Field[] f = actions.getDeclaredFields();
        // 打印获取到的所有变量的信息
        for (int j = 0; j < f.length; j++) {
            XposedBridge.log(f[j].toString());
        }
        XposedBridge.log("Classes");
        // 获取到指定名称类中声明的所有内部类的信息
        Class<?>[] c = actions.getDeclaredClasses();
        // 打印获取到的所有内部类的信息
        for (int k = 0; k < c.length; k++) {
            XposedBridge.log(c[k].toString());
        }
    }
}
