package net.danielpark.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by namgyu.park on 2017. 10. 28..
 */

public final class KeyHash {

    private KeyHash(){
        throw new UnsupportedOperationException();
    }

    public enum HashType {
        SHA("SHA"), SHA256("SHA-256");

        String code;

        HashType(String code){
            this.code = code;
        }

        String getCode(){
            return this.code;
        }
    }

    /**
     * Get key hash like e.g) MoOnjObCBRe$nfa42kdoeMdie4=
     * <p>
     *     this method is only to see key hash. Don't use for real process
     *     </p>
     * @param context   android context
     * @param hashType {@link HashType}
     * @return          app key hash
     */
    public static String getAppKeyHash(Context context, HashType hashType) {
        PackageInfo packageInfo;
        String keyHash = null;
        try{
            // Getting application package name
            String packageName = AppUtil.getAppPackageName(context);

            // Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature: packageInfo.signatures){
                MessageDigest md = MessageDigest.getInstance(hashType.getCode());
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));

                return keyHash;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
