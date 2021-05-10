/*     */ package org.videolan.libvlc;
/*     */ 
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import androidx.annotation.MainThread;
/*     */ import org.videolan.libvlc.interfaces.ILibVLC;
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
/*     */ 
/*     */ public abstract class Dialog
/*     */ {
/*     */   public static final int TYPE_ERROR = 0;
/*     */   public static final int TYPE_LOGIN = 1;
/*     */   public static final int TYPE_QUESTION = 2;
/*     */   public static final int TYPE_PROGRESS = 3;
/*     */   protected final int mType;
/*     */   private final String mTitle;
/*     */   protected String mText;
/*     */   private Object mContext;
/* 106 */   private static Handler sHandler = null;
/* 107 */   private static Callbacks sCallbacks = null;
/*     */   
/*     */   protected Dialog(int type, String title, String text) {
/* 110 */     this.mType = type;
/* 111 */     this.mTitle = title;
/* 112 */     this.mText = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public int getType() {
/* 124 */     return this.mType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public String getTitle() {
/* 132 */     return this.mTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public String getText() {
/* 140 */     return this.mText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void setContext(Object context) {
/* 148 */     this.mContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public Object getContext() {
/* 156 */     return this.mContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public void dismiss() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MainThread
/*     */   public static void setCallbacks(ILibVLC iLibVLC, Callbacks callbacks) {
/* 174 */     if (callbacks != null && sHandler == null)
/* 175 */       sHandler = new Handler(Looper.getMainLooper()); 
/* 176 */     sCallbacks = callbacks;
/* 177 */     nativeSetCallbacks(iLibVLC, (callbacks != null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ErrorMessage
/*     */     extends Dialog
/*     */   {
/*     */     private ErrorMessage(String title, String text) {
/* 188 */       super(0, title, text);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static abstract class IdDialog extends Dialog {
/*     */     protected long mId;
/*     */     
/*     */     protected IdDialog(long id, int type, String title, String text) {
/* 196 */       super(type, title, text);
/* 197 */       this.mId = id;
/*     */     }
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public void dismiss() {
/* 203 */       if (this.mId != 0L) {
/* 204 */         nativeDismiss(this.mId);
/* 205 */         this.mId = 0L;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private native void nativeDismiss(long param1Long);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class LoginDialog
/*     */     extends IdDialog
/*     */   {
/*     */     private final String mDefaultUsername;
/*     */     private final boolean mAskStore;
/*     */     
/*     */     private LoginDialog(long id, String title, String text, String defaultUsername, boolean askStore) {
/* 221 */       super(id, 1, title, text);
/* 222 */       this.mDefaultUsername = defaultUsername;
/* 223 */       this.mAskStore = askStore;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public String getDefaultUsername() {
/* 231 */       return this.mDefaultUsername;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public boolean asksStore() {
/* 241 */       return this.mAskStore;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public void postLogin(String username, String password, boolean store) {
/* 253 */       if (this.mId != 0L) {
/* 254 */         nativePostLogin(this.mId, username, password, store);
/* 255 */         this.mId = 0L;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private native void nativePostLogin(long param1Long, String param1String1, String param1String2, boolean param1Boolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class QuestionDialog
/*     */     extends IdDialog
/*     */   {
/*     */     public static final int TYPE_NORMAL = 0;
/*     */     
/*     */     public static final int TYPE_WARNING = 1;
/*     */     
/*     */     public static final int TYPE_ERROR = 2;
/*     */     
/*     */     private final int mQuestionType;
/*     */     private final String mCancelText;
/*     */     private final String mAction1Text;
/*     */     private final String mAction2Text;
/*     */     
/*     */     private QuestionDialog(long id, String title, String text, int type, String cancelText, String action1Text, String action2Text) {
/* 279 */       super(id, 2, title, text);
/* 280 */       this.mQuestionType = type;
/* 281 */       this.mCancelText = cancelText;
/* 282 */       this.mAction1Text = action1Text;
/* 283 */       this.mAction2Text = action2Text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public int getQuestionType() {
/* 294 */       return this.mQuestionType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public String getCancelText() {
/* 302 */       return this.mCancelText;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public String getAction1Text() {
/* 310 */       return this.mAction1Text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public String getAction2Text() {
/* 318 */       return this.mAction2Text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public void postAction(int action) {
/* 328 */       if (this.mId != 0L) {
/* 329 */         nativePostAction(this.mId, action);
/* 330 */         this.mId = 0L;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private native void nativePostAction(long param1Long, int param1Int);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ProgressDialog
/*     */     extends IdDialog
/*     */   {
/*     */     private final boolean mIndeterminate;
/*     */     
/*     */     private float mPosition;
/*     */     private final String mCancelText;
/*     */     
/*     */     private ProgressDialog(long id, String title, String text, boolean indeterminate, float position, String cancelText) {
/* 348 */       super(id, 3, title, text);
/* 349 */       this.mIndeterminate = indeterminate;
/* 350 */       this.mPosition = position;
/* 351 */       this.mCancelText = cancelText;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public boolean isIndeterminate() {
/* 359 */       return this.mIndeterminate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public boolean isCancelable() {
/* 367 */       return (this.mCancelText != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public float getPosition() {
/* 376 */       return this.mPosition;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @MainThread
/*     */     public String getCancelText() {
/* 384 */       return this.mCancelText;
/*     */     }
/*     */     
/*     */     private void update(float position, String text) {
/* 388 */       this.mPosition = position;
/* 389 */       this.mText = text;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void displayErrorFromNative(String title, String text) {
/* 396 */     final ErrorMessage dialog = new ErrorMessage(title, text);
/* 397 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 400 */             if (Dialog.sCallbacks != null) {
/* 401 */               Dialog.sCallbacks.onDisplay(dialog);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dialog displayLoginFromNative(long id, String title, String text, String defaultUsername, boolean askStore) {
/* 410 */     final LoginDialog dialog = new LoginDialog(id, title, text, defaultUsername, askStore);
/* 411 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 414 */             if (Dialog.sCallbacks != null)
/* 415 */               Dialog.sCallbacks.onDisplay(dialog); 
/*     */           }
/*     */         });
/* 418 */     return dialog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dialog displayQuestionFromNative(long id, String title, String text, int type, String cancelText, String action1Text, String action2Text) {
/* 425 */     final QuestionDialog dialog = new QuestionDialog(id, title, text, type, cancelText, action1Text, action2Text);
/*     */     
/* 427 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 430 */             if (Dialog.sCallbacks != null)
/* 431 */               Dialog.sCallbacks.onDisplay(dialog); 
/*     */           }
/*     */         });
/* 434 */     return dialog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dialog displayProgressFromNative(long id, String title, String text, boolean indeterminate, float position, String cancelText) {
/* 441 */     final ProgressDialog dialog = new ProgressDialog(id, title, text, indeterminate, position, cancelText);
/* 442 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 445 */             if (Dialog.sCallbacks != null)
/* 446 */               Dialog.sCallbacks.onDisplay(dialog); 
/*     */           }
/*     */         });
/* 449 */     return dialog;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void cancelFromNative(final Dialog dialog) {
/* 454 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 457 */             if (dialog instanceof IdDialog)
/* 458 */               ((IdDialog)dialog).dismiss();
/* 459 */             if (Dialog.sCallbacks != null && dialog != null) {
/* 460 */               Dialog.sCallbacks.onCanceled(dialog);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void updateProgressFromNative(final Dialog dialog, final float position, final String text) {
/* 468 */     sHandler.post(new Runnable()
/*     */         {
/*     */           public void run() {
/* 471 */             if (dialog.getType() != 3)
/* 472 */               throw new IllegalArgumentException("dialog is not a progress dialog"); 
/* 473 */             ProgressDialog progressDialog = (ProgressDialog)dialog;
/* 474 */             progressDialog.update(position, text);
/* 475 */             if (Dialog.sCallbacks != null)
/* 476 */               Dialog.sCallbacks.onProgressUpdate(progressDialog); 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static native void nativeSetCallbacks(ILibVLC paramILibVLC, boolean paramBoolean);
/*     */   
/*     */   public static interface Callbacks {
/*     */     @MainThread
/*     */     void onDisplay(ErrorMessage param1ErrorMessage);
/*     */     
/*     */     @MainThread
/*     */     void onDisplay(LoginDialog param1LoginDialog);
/*     */     
/*     */     @MainThread
/*     */     void onDisplay(QuestionDialog param1QuestionDialog);
/*     */     
/*     */     @MainThread
/*     */     void onDisplay(ProgressDialog param1ProgressDialog);
/*     */     
/*     */     @MainThread
/*     */     void onCanceled(Dialog param1Dialog);
/*     */     
/*     */     @MainThread
/*     */     void onProgressUpdate(ProgressDialog param1ProgressDialog);
/*     */   }
/*     */ }


/* Location:              C:\Users\gy\Desktop\test0429\libvlc-all-3.3.0-eap17\classes.jar!\org\videolan\libvlc\Dialog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */