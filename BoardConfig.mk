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

#
# This file sets variables that control the way modules are built
# throughout the system. Those variables can be read from any Android.mk
# file. Preferably, such variables should be prefixed by "BOARD_".
#
# The variables in this file should be used to control all behaviors
# that affect at least one Open Source Android.mk (i.e. outside of the
# vendor/ folder).
#
# Preferably, variables set in this file should not be used to conditionally
# disable makefiles (the primary mechanism to control what gets included in
# a build is to use PRODUCT_PACKAGES in a product definition file).
#

-include vendor/lge/mako/BoardConfigVendor.mk
