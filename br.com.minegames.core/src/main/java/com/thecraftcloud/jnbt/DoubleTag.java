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
/*    */ public final class DoubleTag
/*    */   extends Tag
/*    */ {
/*    */   private final double value;
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
/*    */   public DoubleTag(String name, double value)
/*    */   {
/* 54 */     super(name);
/* 55 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Double getValue()
/*    */   {
/* 60 */     return Double.valueOf(this.value);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     String name = getName();
/* 66 */     String append = "";
/* 67 */     if ((name != null) && (!name.equals(""))) {
/* 68 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 70 */     return "TAG_Double" + append + ": " + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\voigo\Downloads\jnbt-1.1\jnbt-1.1.jar!\org\jnbt\DoubleTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */