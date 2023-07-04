package com.service.kapai.jna;

import com.service.kapai.jna.go.GoString;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface LibWeb3 extends Library {
    LibWeb3 LIB_WEB_3 = Native.load(System.getProperty("user.dir") + "/libweb3.so", LibWeb3.class);

    void InitConfig(GoString.ByValue jsonStr);

    boolean VerifyingSignedMessage(GoString.ByValue wallet, GoString.ByValue message, GoString.ByValue sign);

    String GetMoldEncodeData(int id, long no, boolean useEP);

    String GetERC20Balance(GoString.ByValue contract, GoString.ByValue account);

    String SendERC20Transfer(GoString.ByValue hexKey, GoString.ByValue contract, GoString.ByValue to, GoString.ByValue value);

    String GetERC20TransferLog(GoString.ByValue hash);

    String GetMoldLog(GoString.ByValue hash);

    String GetPancakeSwapTokenV2Price(GoString.ByValue tokenIn, GoString.ByValue tokenOut);

    String GetBuyNodeEncodeData(long value, GoString.ByValue recommender, long no);

    String GetByNode(GoString.ByValue contract, long value);

    String GetBuyNodeLog(GoString.ByValue hash);

    String Release(GoString.ByValue hexKey, GoString.ByValue contract);
}
