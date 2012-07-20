#!/system/bin/sh

BLUETOOTH_SLEEP_PATH=/proc/bluetooth/sleep/proto
LOG_TAG="mako-bluetooth"
LOG_NAME="${0}:"

loge ()
{
  /system/bin/log -t $LOG_TAG -p e "$LOG_NAME $@"
}

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}

failed ()
{
  loge "$1: exit code $2"
  exit $2
}

# Note that "hci_qcomm_init -e" prints expressions to set the shell variables
# BTS_DEVICE, BTS_TYPE, BTS_BAUD, and BTS_ADDRESS.

POWER_CLASS=`getprop qcom.bt.dev_power_class`
TRANSPORT=`getprop ro.qualcomm.bt.hci_transport`

#find the transport type
logi "Transport : $TRANSPORT"

#load bd addr
BDADDR=`/system/bin/bdAddrLoader -p`

logi "BDADDR: $BDADDR"

case $POWER_CLASS in
  1) PWR_CLASS="-p 0" ;
     logi "Power Class: 1";;
  2) PWR_CLASS="-p 1" ;
     logi "Power Class: 2";;
  3) PWR_CLASS="-p 2" ;
     logi "Power Class: CUSTOM";;
  *) PWR_CLASS="";
     logi "Power Class: Ignored. Default(1) used (1-CLASS1/2-CLASS2/3-CUSTOM)";
     logi "Power Class: To override, Before turning BT ON; setprop qcom.bt.dev_power_class <1 or 2 or 3>";;
esac

if ["$BDADDR" == ""]
then
  logwrapper /system/bin/hci_qcomm_init -e $PWR_CLASS -vv
else
  logwrapper /system/bin/hci_qcomm_init -b $BDADDR -e $PWR_CLASS -vv
fi

case $? in
  0) logi "Bluetooth QSoC firmware download succeeded, $BTS_DEVICE $BTS_TYPE $BTS_BAUD $BTS_ADDRESS";;
  *) failed "Bluetooth QSoC firmware download failed" $exit_code_hci_qcomm_init;
     exit $exit_code_hci_qcomm_init;;
esac

logi "start bluetooth smd transport"

echo 1 > /sys/module/hci_smd/parameters/hcismd_set

exit 0
