/*     */ package org.videolan.libvlc.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
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
/*     */ public class HWDecoderUtil
/*     */ {
/*     */   public enum Decoder
/*     */   {
/*  32 */     UNKNOWN, NONE, OMX, MEDIACODEC, ALL;
/*     */   }
/*     */   
/*     */   public enum AudioOutput {
/*  36 */     OPENSLES, AUDIOTRACK, ALL; }
/*     */   
/*     */   private static class DecoderBySOC {
/*     */     public final String key;
/*     */     public final String value;
/*     */     public final Decoder dec;
/*     */     
/*     */     public DecoderBySOC(String key, String value, Decoder dec) {
/*  44 */       this.key = key;
/*  45 */       this.value = value;
/*  46 */       this.dec = dec;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AudioOutputBySOC {
/*     */     public final String key;
/*     */     public final String value;
/*     */     public final AudioOutput aout;
/*     */     
/*     */     public AudioOutputBySOC(String key, String value, AudioOutput aout) {
/*  56 */       this.key = key;
/*  57 */       this.value = value;
/*  58 */       this.aout = aout;
/*     */     }
/*     */   }
/*     */   
/*  62 */   private static final DecoderBySOC[] sBlacklistedDecoderBySOCList = new DecoderBySOC[] { new DecoderBySOC("ro.product.board", "MSM8225", Decoder.NONE), new DecoderBySOC("ro.product.board", "hawaii", Decoder.NONE) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static final DecoderBySOC[] sDecoderBySOCList = new DecoderBySOC[] { new DecoderBySOC("ro.product.brand", "SEMC", Decoder.NONE), new DecoderBySOC("ro.board.platform", "msm7627", Decoder.NONE), new DecoderBySOC("ro.product.brand", "Amazon", Decoder.MEDIACODEC), new DecoderBySOC("ro.board.platform", "omap3", Decoder.OMX), new DecoderBySOC("ro.board.platform", "rockchip", Decoder.OMX), new DecoderBySOC("ro.board.platform", "rk29", Decoder.OMX), new DecoderBySOC("ro.board.platform", "msm7630", Decoder.OMX), new DecoderBySOC("ro.board.platform", "s5pc", Decoder.OMX), new DecoderBySOC("ro.board.platform", "montblanc", Decoder.OMX), new DecoderBySOC("ro.board.platform", "exdroid", Decoder.OMX), new DecoderBySOC("ro.board.platform", "sun6i", Decoder.OMX), new DecoderBySOC("ro.board.platform", "exynos4", Decoder.MEDIACODEC), new DecoderBySOC("ro.board.platform", "omap4", Decoder.ALL), new DecoderBySOC("ro.board.platform", "tegra", Decoder.ALL), new DecoderBySOC("ro.board.platform", "tegra3", Decoder.ALL), new DecoderBySOC("ro.board.platform", "msm8660", Decoder.ALL), new DecoderBySOC("ro.board.platform", "exynos5", Decoder.ALL), new DecoderBySOC("ro.board.platform", "rk30", Decoder.ALL), new DecoderBySOC("ro.board.platform", "rk31", Decoder.ALL), new DecoderBySOC("ro.board.platform", "mv88de3100", Decoder.ALL), new DecoderBySOC("ro.hardware", "mt83", Decoder.ALL) };
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
/* 115 */   private static final AudioOutputBySOC[] sAudioOutputBySOCList = new AudioOutputBySOC[] { new AudioOutputBySOC("ro.product.brand", "Amazon", AudioOutput.OPENSLES), new AudioOutputBySOC("ro.product.manufacturer", "Amazon", AudioOutput.OPENSLES) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private static final HashMap<String, String> sSystemPropertyMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Decoder getDecoderFromDevice() {
/* 132 */     for (DecoderBySOC decBySOC : sBlacklistedDecoderBySOCList) {
/* 133 */       String prop = getSystemPropertyCached(decBySOC.key);
/* 134 */       if (prop != null && 
/* 135 */         prop.contains(decBySOC.value)) {
/* 136 */         return decBySOC.dec;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     if (AndroidUtil.isJellyBeanMR2OrLater) {
/* 145 */       return Decoder.ALL;
/*     */     }
/* 147 */     for (DecoderBySOC decBySOC : sDecoderBySOCList) {
/* 148 */       String prop = getSystemPropertyCached(decBySOC.key);
/* 149 */       if (prop != null && 
/* 150 */         prop.contains(decBySOC.value)) {
/* 151 */         return decBySOC.dec;
/*     */       }
/*     */     } 
/*     */     
/* 155 */     return Decoder.UNKNOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AudioOutput getAudioOutputFromDevice() {
/* 163 */     for (AudioOutputBySOC aoutBySOC : sAudioOutputBySOCList) {
/* 164 */       String prop = getSystemPropertyCached(aoutBySOC.key);
/* 165 */       if (prop != null && 
/* 166 */         prop.contains(aoutBySOC.value)) {
/* 167 */         return aoutBySOC.aout;
/*     */       }
/*     */     } 
/* 170 */     return AudioOutput.ALL;
/*     */   }
/*     */   
/*     */   private static String getSystemPropertyCached(String key) {
/* 174 */     String prop = sSystemPropertyMap.get(key);
/* 175 */     if (prop == null) {
/* 176 */       prop = getSystemProperty(key, "none");
/* 177 */       sSystemPropertyMap.put(key, prop);
/*     */     } 
/* 179 */     return prop;
/*     */   }
/*     */   
/*     */   private static String getSystemProperty(String key, String def) {
/*     */     try {
/* 184 */       ClassLoader cl = ClassLoader.getSystemClassLoader();
/* 185 */       Class<?> SystemProperties = cl.loadClass("android.os.SystemProperties");
/* 186 */       Class<?>[] paramTypes = new Class[] { String.class, String.class };
/* 187 */       Method get = SystemProperties.getMethod("get", paramTypes);
/* 188 */       Object[] params = { key, def };
/* 189 */       return (String)get.invoke(SystemProperties, params);
/* 190 */     } catch (Exception e) {
/* 191 */       return def;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvl\\util\HWDecoderUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */