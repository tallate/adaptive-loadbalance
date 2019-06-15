package com.aliware.tianchi.jmxmonitor;

/**
 * @author hgc
 * @date 3/9/19
 */
public class OSType {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMacOS() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && !OS.contains("x");
    }

    public static boolean isMacOSX() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && OS.contains("x");
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isOS2() {
        return OS.contains("os/2");
    }

    public static boolean isSolaris() {
        return OS.contains("solaris");
    }

    public static boolean isSunOS() {
        return OS.contains("sunos");
    }

    public static boolean isMPEiX() {
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX() {
        return OS.contains("hp-ux");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isOS390() {
        return OS.contains("os/390");
    }

    public static boolean isFreeBSD() {
        return OS.contains("freebsd");
    }

    public static boolean isIrix() {
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix() {
        return OS.contains("digital") && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.contains("netware");
    }

    public static boolean isOSF1() {
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS() {
        return OS.contains("openvms");
    }

    @SuppressWarnings("all")
    public enum OSTypeEnum {
        Any("any"),
        Linux("Linux"),
        Mac_OS("Mac OS"),
        Mac_OS_X("Mac OS X"),
        Windows("Windows"),
        OS2("OS/2"),
        Solaris("Solaris"),
        SunOS("SunOS"),
        MPEiX("MPE/iX"),
        HP_UX("HP-UX"),
        AIX("AIX"),
        OS390("OS/390"),
        FreeBSD("FreeBSD"),
        Irix("Irix"),
        Digital_Unix("Digital Unix"),
        NetWare_411("NetWare"),
        OSF1("OSF1"),
        OpenVMS("OpenVMS"),
        Others("Others");

        private OSTypeEnum(String desc) {
            this.description = desc;
        }

        @Override
        public String toString() {
            return description;
        }

        private String description;
    }


    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static OSTypeEnum getOSType() {
        if (isAix()) {
            return OSTypeEnum.AIX;
        } else if (isDigitalUnix()) {
            return OSTypeEnum.Digital_Unix;
        } else if (isFreeBSD()) {
            return OSTypeEnum.FreeBSD;
        } else if (isHPUX()) {
            return OSTypeEnum.HP_UX;
        } else if (isIrix()) {
            return OSTypeEnum.Irix;
        } else if (isLinux()) {
            return OSTypeEnum.Linux;
        } else if (isMacOS()) {
            return OSTypeEnum.Mac_OS;
        } else if (isMacOSX()) {
            return OSTypeEnum.Mac_OS_X;
        } else if (isMPEiX()) {
            return OSTypeEnum.MPEiX;
        } else if (isNetWare()) {
            return OSTypeEnum.NetWare_411;
        } else if (isOpenVMS()) {
            return OSTypeEnum.OpenVMS;
        } else if (isOS2()) {
            return OSTypeEnum.OS2;
        } else if (isOS390()) {
            return OSTypeEnum.OS390;
        } else if (isOSF1()) {
            return OSTypeEnum.OSF1;
        } else if (isSolaris()) {
            return OSTypeEnum.Solaris;
        } else if (isSunOS()) {
            return OSTypeEnum.SunOS;
        } else if (isWindows()) {
            return OSTypeEnum.Windows;
        } else {
            return OSTypeEnum.Others;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getOSType());
    }
}
