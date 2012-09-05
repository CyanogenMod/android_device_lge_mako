#!/system/bin/sh
setprop persist.radio.kickstart off

for f in $(ls /data/qcks/); do
    (ls -l /data/qcks/$f | grep root) && rm /data/qcks/$f;
done

setprop persist.radio.kickstart on
