/*     */ package org.apache.commons.net.util;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class SubnetUtils
/*     */ {
/*     */   private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
/*     */   private static final String SLASH_FORMAT = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})";
/*  31 */   private static final Pattern addressPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
/*  32 */   private static final Pattern cidrPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})");
/*     */   
/*     */   private static final int NBITS = 32;
/*  35 */   private int netmask = 0;
/*  36 */   private int address = 0;
/*  37 */   private int network = 0;
/*  38 */   private int broadcast = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inclusiveHostCount = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubnetUtils(String cidrNotation) {
/*  51 */     calculate(cidrNotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubnetUtils(String address, String mask) {
/*  62 */     calculate(toCidrNotation(address, mask));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInclusiveHostCount() {
/*  73 */     return this.inclusiveHostCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInclusiveHostCount(boolean inclusiveHostCount) {
/*  83 */     this.inclusiveHostCount = inclusiveHostCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final class SubnetInfo
/*     */   {
/*     */     private static final long UNSIGNED_INT_MASK = 4294967295L;
/*     */ 
/*     */ 
/*     */     
/*     */     private SubnetInfo() {}
/*     */ 
/*     */     
/*     */     private int netmask() {
/*  98 */       return SubnetUtils.this.netmask;
/*  99 */     } private int network() { return SubnetUtils.this.network; }
/* 100 */     private int address() { return SubnetUtils.this.address; } private int broadcast() {
/* 101 */       return SubnetUtils.this.broadcast;
/*     */     }
/*     */     
/* 104 */     private long networkLong() { return SubnetUtils.this.network & 0xFFFFFFFFL; } private long broadcastLong() {
/* 105 */       return SubnetUtils.this.broadcast & 0xFFFFFFFFL;
/*     */     }
/*     */     private int low() {
/* 108 */       return SubnetUtils.this.isInclusiveHostCount() ? network() : ((broadcastLong() - networkLong() > 1L) ? (network() + 1) : 0);
/*     */     }
/*     */ 
/*     */     
/*     */     private int high() {
/* 113 */       return SubnetUtils.this.isInclusiveHostCount() ? broadcast() : ((broadcastLong() - networkLong() > 1L) ? (broadcast() - 1) : 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isInRange(String address) {
/* 125 */       return isInRange(SubnetUtils.this.toInteger(address));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isInRange(int address) {
/* 135 */       long addLong = address & 0xFFFFFFFFL;
/* 136 */       long lowLong = low() & 0xFFFFFFFFL;
/* 137 */       long highLong = high() & 0xFFFFFFFFL;
/* 138 */       return (addLong >= lowLong && addLong <= highLong);
/*     */     }
/*     */     
/*     */     public String getBroadcastAddress() {
/* 142 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(broadcast()));
/*     */     }
/*     */     
/*     */     public String getNetworkAddress() {
/* 146 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(network()));
/*     */     }
/*     */     
/*     */     public String getNetmask() {
/* 150 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(netmask()));
/*     */     }
/*     */     
/*     */     public String getAddress() {
/* 154 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(address()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLowAddress() {
/* 164 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(low()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getHighAddress() {
/* 174 */       return SubnetUtils.this.format(SubnetUtils.this.toArray(high()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int getAddressCount() {
/* 186 */       long countLong = getAddressCountLong();
/* 187 */       if (countLong > 2147483647L) {
/* 188 */         throw new RuntimeException("Count is larger than an integer: " + countLong);
/*     */       }
/*     */       
/* 191 */       return (int)countLong;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getAddressCountLong() {
/* 201 */       long b = broadcastLong();
/* 202 */       long n = networkLong();
/* 203 */       long count = b - n + (SubnetUtils.this.isInclusiveHostCount() ? 1L : -1L);
/* 204 */       return (count < 0L) ? 0L : count;
/*     */     }
/*     */     
/*     */     public int asInteger(String address) {
/* 208 */       return SubnetUtils.this.toInteger(address);
/*     */     }
/*     */     
/*     */     public String getCidrSignature() {
/* 212 */       return SubnetUtils.this.toCidrNotation(SubnetUtils.this.format(SubnetUtils.this.toArray(address())), SubnetUtils.this.format(SubnetUtils.this.toArray(netmask())));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getAllAddresses() {
/* 219 */       int ct = getAddressCount();
/* 220 */       String[] addresses = new String[ct];
/* 221 */       if (ct == 0) {
/* 222 */         return addresses;
/*     */       }
/* 224 */       for (int add = low(), j = 0; add <= high(); add++, j++) {
/* 225 */         addresses[j] = SubnetUtils.this.format(SubnetUtils.this.toArray(add));
/*     */       }
/* 227 */       return addresses;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 236 */       StringBuilder buf = new StringBuilder();
/* 237 */       buf.append("CIDR Signature:\t[").append(getCidrSignature()).append("]").append(" Netmask: [").append(getNetmask()).append("]\n").append("Network:\t[").append(getNetworkAddress()).append("]\n").append("Broadcast:\t[").append(getBroadcastAddress()).append("]\n").append("First Address:\t[").append(getLowAddress()).append("]\n").append("Last Address:\t[").append(getHighAddress()).append("]\n").append("# Addresses:\t[").append(getAddressCount()).append("]\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 244 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SubnetInfo getInfo() {
/* 252 */     return new SubnetInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculate(String mask) {
/* 258 */     Matcher matcher = cidrPattern.matcher(mask);
/*     */     
/* 260 */     if (matcher.matches()) {
/* 261 */       this.address = matchAddress(matcher);
/*     */ 
/*     */       
/* 264 */       int cidrPart = rangeCheck(Integer.parseInt(matcher.group(5)), 0, 32);
/* 265 */       for (int j = 0; j < cidrPart; j++) {
/* 266 */         this.netmask |= 1 << 31 - j;
/*     */       }
/*     */ 
/*     */       
/* 270 */       this.network = this.address & this.netmask;
/*     */ 
/*     */       
/* 273 */       this.broadcast = this.network | this.netmask ^ 0xFFFFFFFF;
/*     */     } else {
/* 275 */       throw new IllegalArgumentException("Could not parse [" + mask + "]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int toInteger(String address) {
/* 283 */     Matcher matcher = addressPattern.matcher(address);
/* 284 */     if (matcher.matches()) {
/* 285 */       return matchAddress(matcher);
/*     */     }
/* 287 */     throw new IllegalArgumentException("Could not parse [" + address + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int matchAddress(Matcher matcher) {
/* 296 */     int addr = 0;
/* 297 */     for (int i = 1; i <= 4; i++) {
/* 298 */       int n = rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255);
/* 299 */       addr |= (n & 0xFF) << 8 * (4 - i);
/*     */     } 
/* 301 */     return addr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] toArray(int val) {
/* 308 */     int[] ret = new int[4];
/* 309 */     for (int j = 3; j >= 0; j--) {
/* 310 */       ret[j] = ret[j] | val >>> 8 * (3 - j) & 0xFF;
/*     */     }
/* 312 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String format(int[] octets) {
/* 319 */     StringBuilder str = new StringBuilder();
/* 320 */     for (int i = 0; i < octets.length; i++) {
/* 321 */       str.append(octets[i]);
/* 322 */       if (i != octets.length - 1) {
/* 323 */         str.append(".");
/*     */       }
/*     */     } 
/* 326 */     return str.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int rangeCheck(int value, int begin, int end) {
/* 335 */     if (value >= begin && value <= end) {
/* 336 */       return value;
/*     */     }
/*     */     
/* 339 */     throw new IllegalArgumentException("Value [" + value + "] not in range [" + begin + "," + end + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int pop(int x) {
/* 347 */     x -= x >>> 1 & 0x55555555;
/* 348 */     x = (x & 0x33333333) + (x >>> 2 & 0x33333333);
/* 349 */     x = x + (x >>> 4) & 0xF0F0F0F;
/* 350 */     x += x >>> 8;
/* 351 */     x += x >>> 16;
/* 352 */     return x & 0x3F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toCidrNotation(String addr, String mask) {
/* 360 */     return addr + "/" + pop(toInteger(mask));
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/org/apache/commons/net/util/SubnetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */