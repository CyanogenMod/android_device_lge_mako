#
# Copyright (C) 2011 The Android Open-Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This file includes all definitions that apply to ALL mako devices, and
# are also specific to mako devices
#
# Everything in this directory will become public

DEVICE_PACKAGE_OVERLAYS := device/lge/mako/overlay

# This device is xhdpi.  However the platform doesn't
# currently contain all of the bitmaps at xhdpi density so
# we do this little trick to fall back to the hdpi version
# if the xhdpi doesn't exist.
PRODUCT_AAPT_CONFIG := normal hdpi xhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

PRODUCT_PACKAGES := \
	lights.msm8960

# Live Wallpapers
PRODUCT_PACKAGES += \
        LiveWallpapers \
        LiveWallpapersPicker \
        VisualizationWallpapers \
        librs_jni

LOCAL_KERNEL := device/lge/mako-kernel/kernel

PRODUCT_COPY_FILES := \
	$(LOCAL_KERNEL):kernel

PRODUCT_COPY_FILES += \
	device/lge/mako-kernel/prima_wlan.ko:system/lib/modules/wlan.ko \
	device/lge/mako/WCNSS_cfg.dat:system/etc/firmware/wlan/prima/WCNSS_cfg.dat \
	device/lge/mako/WCNSS_qcom_cfg.ini:system/etc/firmware/wlan/prima/WCNSS_qcom_cfg.ini \
	device/lge/mako/WCNSS_qcom_wlan_nv.bin:system/etc/firmware/wlan/prima/WCNSS_qcom_wlan_nv.bin

PRODUCT_COPY_FILES += \
	device/lge/mako/init.mako.rc:root/init.mako.rc \
	device/lge/mako/init.mako.usb.rc:root/init.mako.usb.rc \
	device/lge/mako/ueventd.mako.rc:root/ueventd.mako.rc \
	device/lge/mako/vold.fstab:system/etc/vold.fstab \
	device/lge/mako/media_profiles.xml:system/etc/media_profiles.xml

# Prebuilt kl and kcm keymaps
PRODUCT_COPY_FILES += \
	device/lge/mako/apq8064-tabla-snd-card_Button_Jack.kl:system/usr/keylayout/apq8064-tabla-snd-card_Button_Jack.kl \
	device/lge/mako/hs_detect.kl:system/usr/keylayout/hs_detect.kl \
	device/lge/mako/mako-keypad.kl:system/usr/keylayout/mako-keypad.kl \
	device/lge/mako/apq8064-tabla-snd-card_Button_Jack.kcm:system/usr/keychars/apq8064-tabla-snd-card_Button_Jack.kcm \
	device/lge/mako/hs_detect.kcm:system/usr/keychars/hs_detect.kcm \
	device/lge/mako/mako-keypad.kcm:system/usr/keychars/mako-keypad.kcm

# Prebuilt input device calibration files
PRODUCT_COPY_FILES += \
	device/lge/mako/touch_dev.idc:system/usr/idc/touch_dev.idc \
	device/lge/mako/keypad_8064.idc:system/usr/idc/keypad_8064.idc

# These are the hardware-specific features
PRODUCT_COPY_FILES += \
	frameworks/native/data/etc/handheld_core_hardware.xml:system/etc/permissions/handheld_core_hardware.xml \
	frameworks/native/data/etc/android.hardware.camera.flash-autofocus.xml:system/etc/permissions/android.hardware.camera.flash-autofocus.xml \
	frameworks/native/data/etc/android.hardware.camera.front.xml:system/etc/permissions/android.hardware.camera.front.xml \
	frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
	frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
	frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
	frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
	frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
	frameworks/native/data/etc/android.hardware.sensor.gyroscope.xml:system/etc/permissions/android.hardware.sensor.gyroscope.xml \
	frameworks/native/data/etc/android.hardware.sensor.barometer.xml:system/etc/permissions/android.hardware.sensor.barometer.xml \
	frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
	frameworks/native/data/etc/android.hardware.nfc.xml:system/etc/permissions/android.hardware.nfc.xml \
	frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml \
	frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
	frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
	packages/wallpapers/LivePicker/android.software.live_wallpaper.xml:system/etc/permissions/android.software.live_wallpaper.xml

PRODUCT_PROPERTY_OVERRIDES += \
	ro.opengles.version=131072

PRODUCT_PROPERTY_OVERRIDES += \
	ro.sf.lcd_density=320

PRODUCT_CHARACTERISTICS := nosdcard

PRODUCT_TAGS += dalvik.gc.type-precise

PRODUCT_PACKAGES += \
	librs_jni \
	com.android.future.usb.accessory

# Filesystem management tools
PRODUCT_PACKAGES += \
	make_ext4fs \
	setup_fs

# HWComposer
#PRODUCT_PACKAGES += \
#	hwcomposer.default
PRODUCT_PACKAGES += \
	libgenlock \
	liboverlay \
	hwcomposer.msm8960 \
	gralloc.msm8960 \
	copybit.msm8960

#Qcom spec graphics
MM_GRAPHICS := libgsl
MM_GRAPHICS += libsc-adreno200.a
MM_GRAPHICS += libEGL_adreno200
MM_GRAPHICS += eglsubAndroid
MM_GRAPHICS += libGLESv2_adreno200
MM_GRAPHICS += libGLESv2S3D_adreno200
MM_GRAPHICS += libGLESv1_CM_adreno200
MM_GRAPHICS += libq3dtools_adreno200
MM_GRAPHICS += libOpenVG
MM_GRAPHICS += libC2D2
MM_GRAPHICS += libc2d2_a3xx
MM_GRAPHICS += libc2d2_z180
MM_GRAPHICS += libllvm-a3xx
MM_GRAPHICS += libOpenCL
MM_GRAPHICS += libsc-a2xx
MM_GRAPHICS += libsc-a3xx
MM_GRAPHICS += yamato_pfp.fw
MM_GRAPHICS += yamato_pm4.fw
MM_GRAPHICS += leia_pfp_470.fw
MM_GRAPHICS += leia_pm4_470.fw
MM_GRAPHICS += a225_pfp.fw
MM_GRAPHICS += a225_pm4.fw
MM_GRAPHICS += a225p5_pm4.fw
MM_GRAPHICS += a300_pfp.fw
MM_GRAPHICS += a300_pm4.fw
MM_GRAPHICS += a330_pfp.fw
MM_GRAPHICS += a330_pm4.fw
MM_GRAPHICS += gpu_dcvsd


PRODUCT_PACKAGES += $(MM_GRAPHICS)


PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
	persist.sys.usb.config=pc_suite

# for bugmailer
PRODUCT_PACKAGES += send_bug
PRODUCT_COPY_FILES += \
	system/extras/bugmailer/bugmailer.sh:system/bin/bugmailer.sh \
	system/extras/bugmailer/send_bug:system/bin/send_bug

$(call inherit-product, frameworks/native/build/phone-xhdpi-1024-dalvik-heap.mk)
