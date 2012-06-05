#
# Copyright 2012 The Android Open Source Project
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

# HACK: inherit from the emulator - TODO: remove this
$(call inherit-product, $(SRC_TARGET_DIR)/board/generic/device.mk)

# The list of hardware-specific modules that are available in AOSP
PRODUCT_PACKAGES :=

# The list of hardware-specific files that are available in AOSP
PRODUCT_COPY_FILES :=

# The list of hardware-specific properties
PRODUCT_PROPERTY_OVERRIDES :=

# Finally, the kernel, which is special-cased so that it can be
# overridden with an environment variable.
# Temporarily disabled until we have a kernel - TODO: enable back
ifeq ($(TARGET_PREBUILT_KERNEL),)
#PRODUCT_COPY_FILES += \
#    device/lge/mako-kernel/kernel:kernel
else
#PRODUCT_COPY_FILES += \
#    $(TARGET_PREBUILT_KERNEL):kernel
endif

# Inherit from the non-open-source side, if present
$(call inherit-product-if-exists, vendor/lge/mako/device-vendor.mk)

# The Open Source overlay comes after the proprietary one,
# so that the proprietary one can take precedence if necessary.
DEVICE_PACKAGE_OVERLAYS += \
    device/lge/mako/overlay
