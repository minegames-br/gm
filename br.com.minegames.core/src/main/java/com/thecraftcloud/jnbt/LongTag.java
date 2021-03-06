/*    */ package com.thecraftcloud.jnbt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LongTag
/*    */   extends Tag
/*    */ {
/*    */   private final long value;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LongTag(String name, long value)
/*    */   {
/* 54 */     super(name);
/* 55 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Long getValue()
/*    */   {
/* 60 */     return Long.valueOf(this.value);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     String name = getName();
/* 66 */     String append = "";
/* 67 */     if ((name != null) && (!name.equals(""))) {
/* 68 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 70 */     return "TAG_Long" + append + ": " + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\voigo\Downloads\jnbt-1.1\jnbt-1.1.jar!\org\jnbt\LongTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */