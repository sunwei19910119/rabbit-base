package cn.sunwei1991.rabbit.api;

/**
 * @Title SendCallback
 * @Description
 * @Author SunWei
 * @Create 2020/10/21 10:29 上午
 */
public interface SendCallback {

    void onSuccess();

    void onFailure();
}
