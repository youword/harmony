package com.example.distributedfiles;
import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;

public class MainAbility extends AceAbility{
    @Override
    public void onStart(Intent intent) {
        DistributedFiles.register(this);
        requestPermissionsFromUser(new String[]{"ohos.permission.DISTRIBUTED_DATASYNC"},0);
        super.onStart(intent);

    }

    @Override
    public void onStop() {
        DistributedFiles.unregister();
        super.onStop();
    }
}
