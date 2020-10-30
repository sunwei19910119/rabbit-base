package cn.sunwei1991.rabbit.producer.constant;

/**
 * @Title BrokerMessageStatus
 * @Description 消息发送状态
 * @Author SunWei
 * @Create 2020/10/21 4:52 下午
 */
public enum BrokerMessageStatus {

    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2"),
    SEND_FAIL_A_MOMENT("3"),
    ;

    private String code;

    BrokerMessageStatus(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
