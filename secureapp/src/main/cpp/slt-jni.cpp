#include <sys/system_properties.h>
#include <android/log.h>
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <dlfcn.h>

extern "C" {

char cmd_res_line[256];
char total_cmd_res[25600];

char *exec_get_out(char *cmd) {

    FILE *pipe = popen(cmd, "r");

    if (!pipe)
        return NULL;

    total_cmd_res[0] = 0;

    while (!feof(pipe)) {
        if (fgets(cmd_res_line, 256, pipe) != NULL) {
            strcat(total_cmd_res, cmd_res_line);
        }
    }
    pclose(pipe);
    return total_cmd_res;
}

void imei(char *g_imei) {
    char *imei_start = new char[PROP_VALUE_MAX];
    int ir = __system_property_get("ro.gsm.imei", imei_start);

    if (ir > 0) {
        imei_start[15] = 0;//strz end
        printf("method1 got imei %s len %d\r\n", imei_start, strlen(imei_start));
        strcpy(g_imei, imei_start);
    } else {
        printf("method1 imei failed - trying method2\r\n");
        char *res = exec_get_out("dumpsys iphonesubinfo");
        const char *imei_start_match = "ID = ";
        int imei_start_match_len = strlen(imei_start_match);
        imei_start = strstr(res, imei_start_match);
        if (imei_start && strlen(imei_start) >= 15 + imei_start_match_len) {
            imei_start += imei_start_match_len;
            imei_start[15] = 0;
            printf("method2 IMEI [%s] len %d\r\n", imei_start, strlen(imei_start));
            strcpy(g_imei, imei_start);
        }
    }
}


JNIEXPORT jstring
JNICALL Java_com_applexis_secureapp_DeviceBasedSalt_imei(JNIEnv *env, jobject obj) {
    char *g_imei = new char[32];
    imei(g_imei);
    return env->NewStringUTF(g_imei);
}

}