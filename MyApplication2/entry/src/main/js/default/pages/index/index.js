import prompt from '@system.prompt';
//    定义标识符
const ACTION_MESSAGE_CODE_OUT = 1001;   // 文件写入
const ACTION_MESSAGE_CODE_IN = 1002;   // 读取文件

export default {
    data: {
        fileMessage:"文件消息显示区域"
    },
    onInit() {

    },

//    调用PA执行文件写入操作
    onOutput:async function() {
//        弹出提示框
        prompt.showToast({
            message: "调用PA开始执行文件写入操作",
            duration: 1000
        });

        var action = {};
        action.bundleName = 'com.example.distributedfiles';
        // 在JAVA中创建的PA
        action.abilityName = 'com.example.distributedfiles.DistributedFiles';
        action.messageCode = ACTION_MESSAGE_CODE_OUT;
//        将"hello word"写入文件中
        action.data = "hello word"
        action.abilityType = 1;
        action.syncOption = 0;

        // 调用PA
        await FeatureAbility.callAbility(action);
    },

//    调用PA执行读取文件操作
    onInput:async function() {
//        弹出提示框
        prompt.showToast({
            message: "调用PA开始执行文件读取操作",
            duration: 1000
        });

        var action = {};
        action.bundleName = 'com.example.distributedfiles'
        action.abilityName = 'com.example.distributedfiles.DistributedFiles';
        action.messageCode = ACTION_MESSAGE_CODE_IN;
        action.abilityType = 1;
        action.syncOption = 0;

        // 调用PA
        var resultData = await FeatureAbility.callAbility(action);
        // 解析返回的结果
        var result = JSON.parse(resultData);

//        弹出提示框
        prompt.showToast({
            message: result.result,
            duration: 1000
        });

//        将PA返回的结果显示出来
        this.fileMessage = result.result;
    }
}
