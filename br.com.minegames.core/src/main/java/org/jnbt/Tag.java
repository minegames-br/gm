/*    */ package org.jnbt;
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
/*    */ public abstract class Tag
/*    */ {
/*    */   private final String name;
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
/*    */   public Tag(String name)
/*    */   {
/* 53 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String getName()
/*    */   {
/* 61 */     return this.name;
/*    */   }
/*    */   
/*    */   public abstract Object getValue();
/*    */ }


/* Location:              C:\Users\voigo\Downloads\jnbt-1.1\jnbt-1.1.jar!\org\jnbt\Tag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */