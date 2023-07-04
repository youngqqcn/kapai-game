package com.service.kapai.utils;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.*;
import java.util.Arrays;

/**
 * Md5加密方法
 * 
 * @author agm
 */
public class Ed25519Utils
{

    /**
     * ed25519签名
     * @param private_key_hex_str  ed25519私钥, 十六进制字符串格式的, 不带0x开头
     * @param msg  签名内容
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     */
    public static String Ed25519SignMsg( String private_key_hex_str, String msg ) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        byte[] seed = Utils.hexToBytes(private_key_hex_str);
        // 取前32位字节私钥
        byte[] privSeed = Arrays.copyOfRange(seed, 0, 32);
        // ed25519 签名
        EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        Signature signature = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(privSeed, spec);
        PrivateKey sKey = new EdDSAPrivateKey(privKey);
        // 私钥 签名
        signature.initSign(sKey);
        signature.update(msg.getBytes());

        // 签名转换为hex
        String sign = Utils.bytesToHex(signature.sign());
        return sign;
    }

    /**
     * 验签函数
     * @param public_key_hex_str ed25519公钥, 十六进制字符串格式的, 不带0x开头
     * @param msg 签名时的内容(字符串格式,而不是bytes)
     * @return
     */
    public static boolean Ed25519VerifySig( String public_key_hex_str, String msg, String msg_signature_hex_str ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 公钥
        byte[] pubSeed = Utils.hexToBytes(public_key_hex_str);

        // 用公钥验证签名
        EdDSANamedCurveSpec spec2 = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        Signature signature2 = new EdDSAEngine(MessageDigest.getInstance(spec2.getHashAlgorithm()));
        EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(pubSeed, spec2);
        PublicKey pKey = new EdDSAPublicKey(pubKeySpec);
        // 公钥 验证返回的sign
        signature2.initVerify(pKey);
        // 对返回的data加密SHA256 验证
        signature2.update(msg.getBytes());
        boolean verify = signature2.verify(Utils.hexToBytes(msg_signature_hex_str));
        return verify;
    }


//    public static void main(String[] args)  {
//        String private_key = "96c32d75efdaf8d3507681f2c9521e19958b068db3c15d15dbbc8e359b880c484f1ad087cd0868208767be52eb3946054918e77c30a94e07c33090e1fa732221";
//        String public_key = "4f1ad087cd0868208767be52eb3946054918e77c30a94e07c33090e1fa732221";
//
//        // 第1步： 组装消息
//        String msg = "TRC20USDT" + "20230403234214234245" + "TTBXQZLDRNvTD6i5aUffqJnVTH1FNZzzZZ" +
//                "TU7bPGkSWmvvpw4Wfteh1bW1hDWYfXbB9R" + "123.123456" + "1680525303";
//        msg = msg.toLowerCase();
//
//        System.out.printf("签名消息:%s\n", msg);
//
//        // 第2步： 签名
//        try {
//            String sig = Ed25519Utils.Ed25519SignMsg( private_key, msg );
//            System.out.printf("签名:%s\n", sig);
//
//
//            // 第3步： 验签
//            if( Ed25519Utils.Ed25519VerifySig( public_key, msg, sig  ) ) {
//                System.out.printf("验签成功");
//            }else {
//                System.out.printf("验签失败");
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
