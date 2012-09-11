# Android fstab file.
#<src>                                         <mnt_point>  <type>  <mnt_flags and options>  <fs_mgr_flags>
# The filesystem that contains the filesystem checker binary (typically /system) cannot
# specify MF_CHECK, and must come before any filesystems that do specify MF_CHECK

/dev/block/platform/msm_sdcc.1/by-name/system       /system         ext4    ro,barrier=1                                                    wait
/dev/block/platform/msm_sdcc.1/by-name/cache        /cache          ext4    noatime,nosuid,nodev,barrier=1,data=ordered                     wait,check
/dev/block/platform/msm_sdcc.1/by-name/userdata     /data           ext4    noatime,nosuid,nodev,barrier=1,data=ordered,noauto_da_alloc     wait,check,encryptable=/dev/block/platform/msm_sdcc.1/by-name/metadata
/dev/block/platform/msm_sdcc.1/by-name/persist      /persist        ext4    nosuid,nodev,barrier=1,data=ordered,nodelalloc                  wait
/dev/block/platform/msm_sdcc.1/by-name/modem        /firmware       vfat    ro,uid=1000,gid=1000,dmask=227,fmask=337                        wait
