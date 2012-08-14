import common
import struct

def FullOTA_InstallEnd(info):
  try:
    bootloader_img = info.input_zip.read("RADIO/bootloader.img")
  except KeyError:
    print "no bootloader.img in target_files; skipping install"
  else:
    WriteBootloader(info, bootloader_img)

def IncrementalOTA_InstallEnd(info):
  try:
    target_bootloader_img = info.target_zip.read("RADIO/bootloader.img")
    try:
      source_bootloader_img = info.source_zip.read("RADIO/bootloader.img")
    except KeyError:
      source_bootloader_img = None

    if source_bootloader_img == target_bootloader_img:
      print "bootloader unchanged; skipping"
    else:
      WriteBootloader(info, target_bootloader_img)
  except KeyError:
    print "no bootloader.img in target target_files; skipping install"



# /* mako bootloader.img format */
#
# #define BOOTLDR_MAGIC "BOOTLDR!"
# #define BOOTLDR_MAGIC_SIZE 8
#
# struct bootloader_images_header {
#         char magic[BOOTLDR_MAGIC_SIZE];
#         unsigned int num_images;
#         unsigned int start_offset;
#         unsigned int bootldr_size;
#         struct {
#                 char name[64];
#                 unsigned int size;
#         } img_info[];
# };

def WriteBootloader(info, bootloader):
  # bootloader.img contains 6 separate images.  We ignore the first
  # one (sbl1) and write the other five, each to their own partition.
  # There are also backup partitions of all 5 that we also write.
  # sbl1 is the lowest level of the bootloader and is not updatable
  # via OTA.

  header_fmt = "<8sIII"
  header_size = struct.calcsize(header_fmt)
  magic, num_images, start_offset, bootloader_size = struct.unpack(
      header_fmt, bootloader[:header_size])
  assert magic == "BOOTLDR!", "bootloader.img bad magic value"

  img_info_fmt = "<64sI"
  img_info_size = struct.calcsize(img_info_fmt)

  imgs = [struct.unpack(img_info_fmt,
                        bootloader[header_size+i*img_info_size:
                                     header_size+(i+1)*img_info_size])
          for i in range(num_images)]

  total = 0
  p = start_offset
  img_dict = {}
  for name, size in imgs:
    img_dict[trunc_to_null(name)] = p, size
    p += size
  assert p - start_offset == bootloader_size, "bootloader.img corrupted"
  imgs = img_dict

  print magic, num_images, start_offset, bootloader_size
  print imgs

  common.ZipWriteStr(info.output_zip, "bootloader-flag.txt",
                     "updating-bootloader" + "\0" * 13)
  common.ZipWriteStr(info.output_zip, "bootloader-flag-clear.txt", "\0" * 32)

  _, misc_device = common.GetTypeAndDevice("/misc", info.info_dict)

  info.script.AppendExtra(
      'package_extract_file("bootloader-flag.txt", "%s");' %
      (misc_device,))

  # Write the five images to separate files in the OTA package
  for i in "sbl2 sbl3 tz rpm aboot".split():
    common.ZipWriteStr(info.output_zip, "bootloader.%s.img" % (i,),
                       bootloader[imgs[i][0]:imgs[i][0]+imgs[i][1]])
    _, device = common.GetTypeAndDevice("/"+i, info.info_dict)
    info.script.AppendExtra('package_extract_file("bootloader.%s.img", "%s");' %
                            (i, device))

  info.script.AppendExtra(
      'package_extract_file("bootloader-flag-clear.txt", "%s");' %
      (misc_device,))

  try:
    for i in "sbl2 sbl3 tz rpm aboot".split():
      _, device = common.GetTypeAndDevice("/"+i+"b", info.info_dict)
      info.script.AppendExtra(
          'package_extract_file("bootloader.%s.img", "%s");' % (i, device))
  except KeyError:
    pass



def trunc_to_null(s):
  if '\0' in s:
    return s[:s.index('\0')]
  else:
    return s
