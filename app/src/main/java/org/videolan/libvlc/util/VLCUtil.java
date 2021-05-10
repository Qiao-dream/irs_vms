/*     */
package org.videolan.libvlc.util;
/*     */
/*     */

import android.annotation.TargetApi;
/*     */ import android.content.Context;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.net.Uri;
/*     */ import android.os.Build;
/*     */ import android.util.Log;
/*     */ import androidx.annotation.NonNull;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Locale;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class VLCUtil
        /*     */ {
    /*     */   public static final String TAG = "VLC/LibVLC/Util";
    /*  48 */   private static String errorMsg = null;
    /*     */   private static boolean isCompatible = false;

    /*     */
    /*     */
    public static String getErrorMsg() {
        /*  52 */
        return errorMsg;
        /*     */
    }

    /*     */
    /*     */
    @TargetApi(21)
    /*     */ public static String[] getABIList21() {
        /*  57 */
        String[] abis = Build.SUPPORTED_ABIS;
        /*  58 */
        if (abis == null || abis.length == 0)
            /*  59 */ return getABIList();
        /*  60 */
        return abis;
        /*     */
    }

    /*     */
    /*     */
    /*     */
    public static String[] getABIList() {
        /*  65 */
        String[] abis = new String[2];
        /*  66 */
        abis[0] = Build.CPU_ABI;
        /*  67 */
        abis[1] = Build.CPU_ABI2;
        /*  68 */
        return abis;
        /*     */
    }

    /*     */
    /*     */
//    public static boolean hasCompatibleCPU(Context context) {
//        /*     */
//        String[] abis;
//        /*  73 */
//        if (errorMsg != null || isCompatible) return isCompatible;
//        /*     */
//        /*  75 */
//        boolean hasNeon = false, hasFpu = false, hasArmV6 = false, hasPlaceHolder = false;
//        /*  76 */
//        boolean hasArmV7 = false, hasMips = false, hasX86 = false, is64bits = false, isIntel = false;
//        /*  77 */
//        float bogoMIPS = -1.0F;
//        /*  78 */
//        int processors = 0;
//        /*     */
//        /*     */
//        /*     */
//        /*  82 */
//        if (Build.VERSION.SDK_INT >= 21) {
//            /*  83 */
//            abis = getABIList21();
//            /*     */
//        } else {
//            /*  85 */
//            abis = getABIList();
//            /*     */
//        }
//        /*  87 */
//        for (String abi : abis) {
//            /*  88 */
//            if (abi.equals("x86")) {
//                /*  89 */
//                hasX86 = true;
//                /*  90 */
//            } else if (abi.equals("x86_64")) {
//                /*  91 */
//                hasX86 = true;
//                /*  92 */
//                is64bits = true;
//                /*  93 */
//            } else if (abi.equals("armeabi-v7a")) {
//                /*  94 */
//                hasArmV7 = true;
//                /*  95 */
//                hasArmV6 = true;
//                /*  96 */
//            } else if (abi.equals("armeabi")) {
//                /*  97 */
//                hasArmV6 = true;
//                /*  98 */
//            } else if (abi.equals("arm64-v8a")) {
//                /*  99 */
//                hasNeon = true;
//                /* 100 */
//                hasArmV6 = true;
//                /* 101 */
//                hasArmV7 = true;
//                /* 102 */
//                is64bits = true;
//                /*     */
//            }
//            /*     */
//        }
//        /*     */
//        /*     */
//        /* 107 */
//        ElfData elf = null;
//        /* 108 */
//        boolean elfHasX86 = false;
//        /* 109 */
//        boolean elfHasArm = false;
//        /* 110 */
//        boolean elfHasMips = false;
//        /* 111 */
//        boolean elfIs64bits = false;
//        /* 112 */
//        File lib = searchLibrary(context.getApplicationInfo());
//        /* 113 */
//        if (lib != null && (elf = readLib(lib)) != null) {
//            /* 114 */
//            elfHasX86 = (elf.e_machine == 3 || elf.e_machine == 62);
//            /* 115 */
//            elfHasArm = (elf.e_machine == 40 || elf.e_machine == 183);
//            /* 116 */
//            elfHasMips = (elf.e_machine == 8);
//            /* 117 */
//            elfIs64bits = elf.is64bits;
//            /*     */
//            /* 119 */
//            Log.i("VLC/LibVLC/Util", "ELF ABI = " + (elfHasArm ? "arm" : (elfHasX86 ? "x86" : "mips")) + ", " + (elfIs64bits ? "64bits" : "32bits"));
//            /*     */
//            /* 121 */
//            Log.i("VLC/LibVLC/Util", "ELF arch = " + elf.att_arch);
//            /* 122 */
//            Log.i("VLC/LibVLC/Util", "ELF fpu = " + elf.att_fpu);
//            /*     */
//        } else {
//            /* 124 */
//            Log.w("VLC/LibVLC/Util", "WARNING: Unable to read libvlcjni.so; cannot check device ABI!");
//            /*     */
//        }
//        /*     */
//        /*     */
//        /* 128 */
//        FileReader fileReader = null;
//        /* 129 */
//        BufferedReader br = null;
//        /*     */
//        /* 131 */
//        try {
//            fileReader = new FileReader("/proc/cpuinfo");
//            /* 132 */
//            br = new BufferedReader(fileReader);
//            /*     */
//            String str;
//            /* 134 */
//            while ((str = br.readLine()) != null) {
//                /* 135 */
//                if (str.contains("AArch64")) {
//                    /* 136 */
//                    hasArmV7 = true;
//                    /* 137 */
//                    hasArmV6 = true;
//                    /* 138 */
//                } else if (str.contains("ARMv7")) {
//                    /* 139 */
//                    hasArmV7 = true;
//                    /* 140 */
//                    hasArmV6 = true;
//                    /* 141 */
//                } else if (str.contains("ARMv6")) {
//                    /* 142 */
//                    hasArmV6 = true;
//                    /*     */
//                    /*     */
//                }
//                /* 145 */
//                else if (str.contains("clflush size")) {
//                    /* 146 */
//                    hasX86 = true;
//                    /* 147 */
//                } else if (str.contains("GenuineIntel")) {
//                    /* 148 */
//                    hasX86 = true;
//                    /* 149 */
//                } else if (str.contains("placeholder")) {
//                    /* 150 */
//                    hasPlaceHolder = true;
//                    /* 151 */
//                } else if (str.contains("CPU implementer") && str.contains("0x69")) {
//                    /* 152 */
//                    isIntel = true;
//                    /*     */
//                    /*     */
//                }
//                /* 155 */
//                else if (str.contains("microsecond timers")) {
//                    /* 156 */
//                    hasMips = true;
//                    /* 157 */
//                }
//                if (str.contains("neon") || str.contains("asimd"))
//                    /* 158 */ hasNeon = true;
//                /* 159 */
//                if (str.contains("vfp") || (str.contains("Features") && str.contains("fp")))
//                    /* 160 */ hasFpu = true;
//                /* 161 */
//                if (str.startsWith("processor"))
//                    /* 162 */ processors++;
//                /* 163 */
//                if (bogoMIPS < 0.0F && str.toLowerCase(Locale.ENGLISH).contains("bogomips")) {
//                    /* 164 */
//                    String[] bogo_parts = str.split(":");
//                    /*     */
//                    try {
//                        /* 166 */
//                        bogoMIPS = Float.parseFloat(bogo_parts[1].trim());
//                        /* 167 */
//                    } catch (NumberFormatException e) {
//                        /* 168 */
//                        bogoMIPS = -1.0F;
//                        /*     */
//                    }
//                    /*     */
//                }
//                /*     */
//            }
//        }
//        /* 172 */ catch (IOException iOException) {
//        }
//        /*     */ finally
//            /* 174 */ {
//            close(br);
//            /* 175 */
//            close(fileReader);
//        }
//        /*     */
//        /* 177 */
//        if (processors == 0) {
//            /* 178 */
//            processors = 1;
//            /*     */
//        }
//        /* 180 */
//        isCompatible = true;
//        /*     */
//        /* 182 */
//        if (elf != null) {
//            /*     */
//            /* 184 */
//            if (elfHasX86 && !hasX86) {
//                /*     */
//                /*     */
//                /* 187 */
//                if (hasPlaceHolder && isIntel) {
//                    /* 188 */
//                    Log.d("VLC/LibVLC/Util", "Emulated armv7 detected, trying to launch x86 libraries");
//                    /*     */
//                } else {
//                    /* 190 */
//                    errorMsg = "x86 build on non-x86 device";
//                    /* 191 */
//                    isCompatible = false;
//                    /*     */
//                }
//                /* 193 */
//            } else if (elfHasArm && !hasArmV6) {
//                /* 194 */
//                errorMsg = "ARM build on non ARM device";
//                /* 195 */
//                isCompatible = false;
//                /*     */
//            }
//            /*     */
//            /* 198 */
//            if (elfHasMips && !hasMips) {
//                /* 199 */
//                errorMsg = "MIPS build on non-MIPS device";
//                /* 200 */
//                isCompatible = false;
//                /* 201 */
//            } else if (elfHasArm && hasMips) {
//                /* 202 */
//                errorMsg = "ARM build on MIPS device";
//                /* 203 */
//                isCompatible = false;
//                /*     */
//            }
//            /*     */
//            /* 206 */
//            if (elf.e_machine == 40 && elf.att_arch.startsWith("v7") && !hasArmV7) {
//                /* 207 */
//                errorMsg = "ARMv7 build on non-ARMv7 device";
//                /* 208 */
//                isCompatible = false;
//                /*     */
//            }
//            /* 210 */
//            if (elf.e_machine == 40) {
//                /* 211 */
//                if (elf.att_arch.startsWith("v6") && !hasArmV6) {
//                    /* 212 */
//                    errorMsg = "ARMv6 build on non-ARMv6 device";
//                    /* 213 */
//                    isCompatible = false;
//                    /* 214 */
//                } else if (elf.att_fpu && !hasFpu) {
//                    /* 215 */
//                    errorMsg = "FPU-enabled build on non-FPU device";
//                    /* 216 */
//                    isCompatible = false;
//                    /*     */
//                }
//                /*     */
//            }
//            /* 219 */
//            if (elfIs64bits && !is64bits) {
//                /* 220 */
//                errorMsg = "64bits build on 32bits device";
//                /* 221 */
//                isCompatible = false;
//                /*     */
//            }
//            /*     */
//        }
//        /*     */
//        /* 225 */
//        float frequency = -1.0F;
//        /* 226 */
//        fileReader = null;
//        /* 227 */
//        br = null;
//        /* 228 */
//        String line = "";
//        /*     */
//        try {
//            /* 230 */
//            fileReader = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
//            /* 231 */
//            br = new BufferedReader(fileReader);
//            /* 232 */
//            line = br.readLine();
//            /* 233 */
//            if (line != null)
//                /* 234 */ frequency = Float.parseFloat(line) / 1000.0F;
//            /* 235 */
//        } catch (IOException ex) {
//            /* 236 */
//            Log.w("VLC/LibVLC/Util", "Could not find maximum CPU frequency!");
//            /* 237 */
//        } catch (NumberFormatException e) {
//            /* 238 */
//            Log.w("VLC/LibVLC/Util", "Could not parse maximum CPU frequency!");
//            /* 239 */
//            Log.w("VLC/LibVLC/Util", "Failed to parse: " + line);
//            /*     */
//        } finally {
//            /* 241 */
//            close(br);
//            /* 242 */
//            close(fileReader);
//            /*     */
//        }
//        /*     */
//        /*     */
//        /* 246 */
//        machineSpecs = new MachineSpecs();
//        /* 247 */
//        Log.d("VLC/LibVLC/Util", "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
//        /*     */
//        /* 249 */
//        machineSpecs.hasArmV6 = hasArmV6;
//        /* 250 */
//        machineSpecs.hasArmV7 = hasArmV7;
//        /* 251 */
//        machineSpecs.hasFpu = hasFpu;
//        /* 252 */
//        machineSpecs.hasMips = hasMips;
//        /* 253 */
//        machineSpecs.hasNeon = hasNeon;
//        /* 254 */
//        machineSpecs.hasX86 = hasX86;
//        /* 255 */
//        machineSpecs.is64bits = is64bits;
//        /* 256 */
//        machineSpecs.bogoMIPS = bogoMIPS;
//        /* 257 */
//        machineSpecs.processors = processors;
//        /* 258 */
//        machineSpecs.frequency = frequency;
//        /* 259 */
//        return isCompatible;
//        /*     */
//    }

    /*     */
    /*     */
    public static MachineSpecs getMachineSpecs() {
        /* 263 */
        return machineSpecs;
        /*     */
    }

    /*     */
    /* 266 */   private static MachineSpecs machineSpecs = null;
    /*     */   private static final int EM_386 = 3;
    /*     */   private static final int EM_MIPS = 8;
    /*     */   private static final int EM_ARM = 40;
    /*     */   private static final int EM_X86_64 = 62;
    /*     */   private static final int EM_AARCH64 = 183;
    /*     */   private static final int ELF_HEADER_SIZE = 52;
    /*     */   private static final int SECTION_HEADER_SIZE = 40;
    /*     */   private static final int SHT_ARM_ATTRIBUTES = 1879048195;

    /*     */
    /*     */   public static class MachineSpecs {
        /*     */     public boolean hasNeon;
        /*     */     public boolean hasFpu;
        /*     */     public boolean hasArmV6;
        /*     */     public boolean hasArmV7;
        /*     */     public boolean hasMips;
        /*     */     public boolean hasX86;
        /*     */     public boolean is64bits;
        /*     */     public float bogoMIPS;
        /*     */     public int processors;
        /*     */     public float frequency;
        /*     */
    }

    /*     */
    /*     */   private static class ElfData {
        /*     */ ByteOrder order;
        /*     */ boolean is64bits;
        /*     */ int e_machine;
        /*     */ int e_shoff;
        /*     */ int e_shnum;
        /*     */ int sh_offset;
        /*     */ int sh_size;
        /*     */ String att_arch;
        /*     */ boolean att_fpu;

        /*     */
        /*     */
        private ElfData() {
        }
        /*     */
    }

    /*     */
    /*     */
    @TargetApi(9)
    /*     */ private static File searchLibrary(ApplicationInfo applicationInfo) {
        /*     */
        String[] libraryPaths;
        /* 306 */
        if ((applicationInfo.flags & 0x1) != 0) {
            /* 307 */
            String property = System.getProperty("java.library.path");
            /* 308 */
            libraryPaths = property.split(":");
            /*     */
        } else {
            /* 310 */
            libraryPaths = new String[1];
            /* 311 */
            libraryPaths[0] = applicationInfo.nativeLibraryDir;
            /*     */
        }
        /* 313 */
        if (libraryPaths[0] == null) {
            /* 314 */
            Log.e("VLC/LibVLC/Util", "can't find library path");
            /* 315 */
            return null;
            /*     */
        }
        /*     */
        /*     */
        /*     */
        /* 320 */
        for (String libraryPath : libraryPaths) {
            /* 321 */
            File lib = new File(libraryPath, "libvlcjni.so");
            /* 322 */
            if (lib.exists() && lib.canRead())
                /* 323 */ return lib;
            /*     */
        }
        /* 325 */
        Log.e("VLC/LibVLC/Util", "WARNING: Can't find shared library");
        /* 326 */
        return null;
        /*     */
    }

    /*     */
    /*     */
    /* 330 */   private static final String[] CPU_archs = new String[]{"*Pre-v4", "*v4", "*v4T", "v5T", "v5TE", "v5TEJ", "v6", "v6KZ", "v6T2", "v6K", "v7", "*v6-M", "*v6S-M", "*v7E-M", "*v8"};
    /*     */
    /*     */   private static final String URI_AUTHORIZED_CHARS = "'()*";

    /*     */
    /*     */
    /*     */
//    private static ElfData readLib(File file) {
//        /* 336 */
//        RandomAccessFile in = null;
//        /*     */
//        try {
//            /* 338 */
//            in = new RandomAccessFile(file, "r");
//            /*     */
//            /* 340 */
//            ElfData elf = new ElfData();
//            /* 341 */
//            if (!readHeader(in, elf)) {
//                /* 342 */
//                return null;
//                /*     */
//            }
//            /* 344 */
//            switch (elf.e_machine) {
//                /*     */
//                case 3:
//                    /*     */
//                case 8:
//                    /*     */
//                case 62:
//                    /*     */
//                case 183:
//                    /* 349 */
//                    elfData1 = elf;
//                    return elfData1;
//                /*     */
//                case 40:
//                    /* 351 */
//                    in.close();
//                    /* 352 */
//                    in = new RandomAccessFile(file, "r");
//                    /* 353 */
//                    if (!readSection(in, elf)) {
//                        /* 354 */
//                        elfData1 = null;
//                        return elfData1;
//                        /* 355 */
//                    }
//                    in.close();
//                    /* 356 */
//                    in = new RandomAccessFile(file, "r");
//                    /* 357 */
//                    if (!readArmAttributes(in, elf)) {
//                        /* 358 */
//                        elfData1 = null;
//                        return elfData1;
//                        /*     */
//                    }
//                    break;
//                /*     */
//                default:
//                    /* 361 */
//                    elfData1 = null;
//                    return elfData1;
//                /*     */
//            }
//            /* 363 */
//            ElfData elfData1 = elf;
//            return elfData1;
//            /* 364 */
//        } catch (IOException e) {
//            /* 365 */
//            e.printStackTrace();
//            /*     */
//        } finally {
//            /* 367 */
//            close(in);
//            /*     */
//        }
//        /* 369 */
//        return null;
//        /*     */
//    }

    /*     */
    /*     */
    /*     */
    private static boolean readHeader(RandomAccessFile in, ElfData elf) throws IOException {
        /* 374 */
        byte[] bytes = new byte[52];
        /* 375 */
        in.readFully(bytes);
        /* 376 */
        if (bytes[0] != Byte.MAX_VALUE || bytes[1] != 69 || bytes[2] != 76 || bytes[3] != 70 || (bytes[4] != 1 && bytes[4] != 2)) {
            /*     */
            /*     */
            /*     */
            /*     */
            /* 381 */
            Log.e("VLC/LibVLC/Util", "ELF header invalid");
            /* 382 */
            return false;
            /*     */
        }
        /*     */
        /* 385 */
        elf.is64bits = (bytes[4] == 2);
        /* 386 */
        elf.order = (bytes[5] == 1) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
        /*     */
        /*     */
        /*     */
        /*     */
        /* 391 */
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        /* 392 */
        buffer.order(elf.order);
        /*     */
        /* 394 */
        elf.e_machine = buffer.getShort(18);
        /* 395 */
        elf.e_shoff = buffer.getInt(32);
        /* 396 */
        elf.e_shnum = buffer.getShort(48);
        /* 397 */
        return true;
        /*     */
    }

    /*     */
    /*     */
    private static boolean readSection(RandomAccessFile in, ElfData elf) throws IOException {
        /* 401 */
        byte[] bytes = new byte[40];
        /* 402 */
        in.seek(elf.e_shoff);
        /*     */
        /* 404 */
        for (int i = 0; i < elf.e_shnum; ) {
            /* 405 */
            in.readFully(bytes);
            /*     */
            /*     */
            /* 408 */
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            /* 409 */
            buffer.order(elf.order);
            /*     */
            /* 411 */
            int sh_type = buffer.getInt(4);
            /* 412 */
            if (sh_type != 1879048195) {
                /*     */
                i++;
                continue;
                /*     */
            }
            /* 415 */
            elf.sh_offset = buffer.getInt(16);
            /* 416 */
            elf.sh_size = buffer.getInt(20);
            /* 417 */
            return true;
            /*     */
        }
        /*     */
        /* 420 */
        return false;
        /*     */
    }

    /*     */
    /*     */
    private static boolean readArmAttributes(RandomAccessFile in, ElfData elf) throws IOException {
        /* 424 */
        byte[] bytes = new byte[elf.sh_size];
        /* 425 */
        in.seek(elf.sh_offset);
        /* 426 */
        in.readFully(bytes);
        /*     */
        /*     */
        /* 429 */
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        /* 430 */
        buffer.order(elf.order);
        /*     */
        /*     */
        /*     */
        /* 434 */
        if (buffer.get() != 65) {
            /* 435 */
            return false;
            /*     */
        }
        /*     */
        /* 438 */
        while (buffer.remaining() > 0) {
            /* 439 */
            int start_section = buffer.position();
            /* 440 */
            int length = buffer.getInt();
            /* 441 */
            String vendor = getString(buffer);
            /* 442 */
            if (vendor.equals("aeabi")) {
                /*     */
                /* 444 */
                while (buffer.position() < start_section + length) {
                    /* 445 */
                    int start = buffer.position();
                    /* 446 */
                    int tag = buffer.get();
                    /* 447 */
                    int size = buffer.getInt();
                    /*     */
                    /* 449 */
                    if (tag != 1) {
                        /* 450 */
                        buffer.position(start + size);
                        /*     */
                        /*     */
                        continue;
                        /*     */
                    }
                    /*     */
                    /* 455 */
                    while (buffer.position() < start + size) {
                        /* 456 */
                        tag = getUleb128(buffer);
                        /* 457 */
                        if (tag == 6) {
                            /* 458 */
                            int arch = getUleb128(buffer);
                            /* 459 */
                            elf.att_arch = CPU_archs[arch];
                            continue;
                            /* 460 */
                        }
                        if (tag == 27) {
                            /* 461 */
                            getUleb128(buffer);
                            /* 462 */
                            elf.att_fpu = true;
                            /*     */
                            /*     */
                            continue;
                            /*     */
                        }
                        /*     */
                        /* 467 */
                        tag %= 128;
                        /* 468 */
                        if (tag == 4 || tag == 5 || tag == 32 || (tag > 32 && (tag & 0x1) != 0)) {
                            /* 469 */
                            getString(buffer);
                            continue;
                            /*     */
                        }
                        /* 471 */
                        getUleb128(buffer);
                        /*     */
                    }
                    /*     */
                }
                /*     */
                /*     */
                break;
                /*     */
            }
            /*     */
        }
        /* 478 */
        return true;
        /*     */
    }

    /*     */
    /*     */
    private static String getString(ByteBuffer buffer) {
        /* 482 */
        StringBuilder sb = new StringBuilder(buffer.limit());
        /* 483 */
        while (buffer.remaining() > 0) {
            /* 484 */
            char c = (char) buffer.get();
            /* 485 */
            if (c == '\000')
                /*     */ break;
            /* 487 */
            sb.append(c);
            /*     */
        }
        /* 489 */
        return sb.toString();
        /*     */
    }

    /*     */
    /*     */
    private static int getUleb128(ByteBuffer buffer) {
        /* 493 */
        int c, ret = 0;
        /*     */
        /*     */
        do {
            /* 496 */
            ret <<= 7;
            /* 497 */
            c = buffer.get();
            /* 498 */
            ret |= c & 0x7F;
            /* 499 */
        } while ((c & 0x80) > 0);
        /*     */
        /* 501 */
        return ret;
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    public static Uri UriFromMrl(String mrl) {
        /* 511 */
        char[] array = mrl.toCharArray();
        /* 512 */
        StringBuilder sb = new StringBuilder(array.length * 2);
        /* 513 */
        for (int i = 0; i < array.length; i++) {
            /* 514 */
            char c = array[i];
            /* 515 */
            if (c == '%' &&
                    /* 516 */         array.length - i >= 3) {
                /*     */
                try {
                    /* 518 */
                    int hex = Integer.parseInt(new String(array, i + 1, 2), 16);
                    /* 519 */
                    if ("'()*".indexOf(hex) != -1) {
                        /* 520 */
                        sb.append((char) hex);
                        /* 521 */
                        i += 2;
                        /*     */
                        continue;
                        /*     */
                    }
                    /* 524 */
                } catch (NumberFormatException numberFormatException) {
                }
                /*     */
            }
            /*     */
            /*     */
            /* 528 */
            sb.append(c);
            /*     */
            continue;
            /*     */
        }
        /* 531 */
        return Uri.parse(sb.toString());
        /*     */
    }

    /*     */
    /*     */
    public static String encodeVLCUri(@NonNull Uri uri) {
        /* 535 */
        return encodeVLCString(uri.toString());
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    public static String encodeVLCString(@NonNull String mrl) {
        /* 543 */
        char[] array = mrl.toCharArray();
        /* 544 */
        StringBuilder sb = new StringBuilder(array.length * 2);
        /*     */
        /* 546 */
        for (char c : array) {
            /* 547 */
            if ("'()*".indexOf(c) != -1) {
                /* 548 */
                sb.append("%").append(Integer.toHexString(c));
                /*     */
            } else {
                /* 550 */
                sb.append(c);
                /*     */
            }
            /* 552 */
        }
        return sb.toString();
        /*     */
    }

    /*     */
    /*     */
    /*     */
    private static void close(Closeable closeable) {
        /* 557 */
        if (closeable != null)
            /*     */ try {
            /* 559 */
            closeable.close();
            /* 560 */
        } catch (IOException iOException) {
        }
        /*     */
    }
    /*     */
}


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\VLCUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */