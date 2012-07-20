/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <errno.h>
#include <fcntl.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <getopt.h>

#define LOG_TAG "bdAddrLoader"

#include <cutils/log.h>
#include <cutils/properties.h>

#define FILE_PATH_MAX   100
#define BD_ADDR_LEN  6
#define BD_ADDR_STR_LEN 18

#define FTM_PATH_COMMON "/persist"
#define FTM_PATH_BT_ADDR "bluetooth/.bdaddr"
#define FTM_PATH_WIFI_ADDR "wifi/.wlanaddr"

#define DEFAULT_BD_ADDR_PROP "persist.service.bt.bdaddr"

int hexa_to_ascii(const unsigned char* hexa, char* ascii, int nHexLen)
{
    int i, j;
    char hex_table[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

    for (i = 0, j = 0; i <nHexLen; i++, j += 2) {
        ascii[j] = hex_table[hexa[i] >> 4];
        ascii[j + 1] = hex_table[hexa[i] & 0x0F];
    }

    ascii[nHexLen*2] = '\0';

    return 0;
}

int readBDAddrData(const char* szFilePath, unsigned char* addrData, int nDataLen)
{
    int nFd, nRdCnt;

    nFd = open(szFilePath, O_RDONLY);

    if(nFd < 0){
        ALOGW("There is no Address File in FTM area : %s\n", szFilePath);
        return 1;
    }

    nRdCnt = read(nFd, addrData, nDataLen);
    if(nRdCnt != nDataLen){
        ALOGE("Fail to read Address data from FTM area\n");
        close(nFd);
        return 1;
    }
    return 0;
}

void formattingBdAddr(char *szBDAddr, const char cSep)
{
    int i=1, j=0;
    int pos=0;
    for(i=1; i<BD_ADDR_LEN; i++){
       pos = strlen(szBDAddr);
       for(j=0; j<(BD_ADDR_LEN*2)-i*2; j++){
          szBDAddr[pos-j] = szBDAddr[pos-j-1];
       }
       szBDAddr[pos-j]=cSep;
    }
}

int main(int argc, char *argv[])
{
    int nFd, nRdCnt;
    char szFilePath[FILE_PATH_MAX] = {0,};
    unsigned char addrData[BD_ADDR_LEN] = {0,};
    char szBDAddr[BD_ADDR_STR_LEN] = {0,};
    char *szBDAddrPath = NULL;
    char *szProperty = NULL;
    char bStdOut = 0;
    int c;

    while((c=getopt(argc, argv, ":i:o:p")) != -1){
        switch(c){
            case 'i': // input file path
                szBDAddrPath = optarg;
                break;
            case 'o': // output : property name
                szProperty = optarg;
                break;
            case 'p':
                bStdOut = 1;
                break;
            default:
                ALOGW("Unknown option : %c", c);
                break;
        }
    }

    if(szBDAddrPath == NULL){
        //set default bd address path
        sprintf(szFilePath, "%s/%s", FTM_PATH_COMMON, FTM_PATH_BT_ADDR);
        szBDAddrPath = szFilePath;
    }

    if(!readBDAddrData(szBDAddrPath, addrData, BD_ADDR_LEN)){
        if(!hexa_to_ascii(addrData, szBDAddr, BD_ADDR_LEN)){
            formattingBdAddr(szBDAddr, '.');
            // write loaded bdaddr to property
            if(szProperty != NULL) property_set(szProperty, szBDAddr);
            // print out szBDAddr
            if(bStdOut) printf("%s",szBDAddr);
            return 0;
        }
        else{
            ALOGE("Fail to convert data from hex to ascii");
        }
    }
    return 1;
}
