package com.qa.util;

import org.jboss.aerogear.security.otp.Totp;

public class GoogleAuth {

    public static String getTwoFactorCode() {
        //Replace with your security key copied from step 12
        //Totp totp = new Totp("Q23Y7CCPXEBSQSEWFMUGLPUWGBN5Q7725IXWEGJDBKBWJIFPORQA"); // 2FA secret key
        Totp totp = new Totp("BXI2SQJMIOGJVKHHT6GMWRHNIWRPBQIZLYPPIYITD7EDY2V5HADA"); //admin
        String twoFactorCode = totp.now(); //Generated 2FA code here
        return twoFactorCode;
    }

    public static void main(String[] args) {
        System.out.println(getTwoFactorCode());
    }
}
