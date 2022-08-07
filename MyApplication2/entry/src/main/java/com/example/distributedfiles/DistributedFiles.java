package com.example.distributedfiles;

import ohos.ace.ability.AceInternalAbility;
import ohos.agp.components.LayoutScatter;
import ohos.app.AbilityContext;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DistributedFiles extends AceInternalAbility implements AceInternalAbility.AceInternalAbilityHandler{
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0, "app_tag");
    private static final String BUNDLE_NAME = "com.example.distributedfiles";
    private static final String ABILITY_NAME = "com.example.distributedfiles.DistributedFiles";

    //    标识符
    private static final int MESSAGE_CODE_OUT = 1001;
    private static final int MESSAGE_CODE_IN = 1002;

    private static DistributedFiles mInstance;
    private AbilityContext mContext;

    public DistributedFiles() {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

    public static void register(AbilityContext context){
        mInstance = new DistributedFiles();
        mInstance.mContext = context;
        mInstance.setInternalAbilityHandler(mInstance);
    }

    public static void unregister() {
        if (mInstance == null) {
            return;
        }
        mInstance.mContext = null;
        mInstance.setInternalAbilityHandler(null);
    }

    @Override
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel1, MessageOption messageOption) throws RemoteException {
        HiLog.info(LABEL, "成功调用PA" );

//        文件字符缓冲流初始化
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

//        获取属于自己的分布式目录
        File distDir = mContext.getDistributedDir();

        String filePath = distDir + File.separator + "hello.txt";
        File file = new File(filePath);
        try {
//            如果不存在该文件，则创建该文件
            if(file.createNewFile());

            switch (i) {

//                文件写入操作
                case MESSAGE_CODE_OUT:
                    String hello = messageParcel.readString();
                    HiLog.info(LABEL,"准备写入文件的内容：" + hello);
//                    将"hello word"写入文件
                    bufferedWriter = new BufferedWriter(new FileWriter(filePath));
                    bufferedWriter.write(hello);
                    break;

//                    文件读取操作
                case MESSAGE_CODE_IN:
                    HiLog.info(LABEL,"准备读取文件的内容");
                    File dist = mContext.getDistributedDir();
                    String path = dist + File.separator + "hello.txt";
                    String text;
                    StringBuffer buffer = new StringBuffer();
                    InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "GBK");
                    bufferedReader = new BufferedReader(isr);
                    while((text = bufferedReader.readLine()) != null) {
                        HiLog.info(LABEL,"text: " + text);
                        buffer.append(text);
                    }

//                  将读取的内容返回FA
                    Map<String, Object> result = new HashMap<>();
                    HiLog.info(LABEL,buffer.toString());
                    result.put("result",buffer.toString());
                    messageParcel1.writeString(ZSONObject.toZSONString(result));
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            完成流的操作，关闭流
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
