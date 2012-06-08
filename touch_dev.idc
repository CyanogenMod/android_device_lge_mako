# Copyright (C) 2011 The Android Open Source Project
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
# Input Device Calibration File for the Tuna touch screen.
#

device.internal = 1

# Basic Parameters
touch.deviceType = touchScreen
touch.orientationAware = 1

# Gesture Mode Parameters
touch.gestureMode = spots


# Size
touch.size.calibration = diameter
touch.size.scale = 1
touch.size.bias = 0
touch.size.isSummed = 0

# Pressure
# Driver reports signal strength as pressure.
#
# A normal thumb touch typically registers about 200 signal strength
# units although we don't expect these values to be accurate.
touch.pressure.calibration = physical
touch.pressure.scale = 0.004

# Orientation
touch.orientation.calibration = none

touch.distance.calibration = none
touch.distance.scale = 1

keyboard.layout = mako-keypad
keyboard.characterMap = mako-keypad
