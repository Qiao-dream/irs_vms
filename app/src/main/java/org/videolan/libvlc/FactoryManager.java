/*    */ package org.videolan.libvlc;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.videolan.libvlc.interfaces.IComponentFactory;
/*    */ 
/*    */ public class FactoryManager
/*    */ {
/*  9 */   private static Map<String, IComponentFactory> factories = new HashMap<>();
/*    */   
/*    */   public static void registerFactory(String factoryId, IComponentFactory factory) {
/* 12 */     factories.put(factoryId, factory);
/*    */   }
/*    */   
/*    */   public static IComponentFactory getFactory(String factoryId) {
/* 16 */     return factories.get(factoryId);
/*    */   }
/*    */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\FactoryManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */