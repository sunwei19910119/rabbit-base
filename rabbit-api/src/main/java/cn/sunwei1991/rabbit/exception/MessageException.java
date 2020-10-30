package cn.sunwei1991.rabbit.exception;

/**
 * @Title MessageException
 * @Description Message异常
 * @Author SunWei
 * @Create 2020/10/21 9:59 上午
 */
public class MessageException extends Exception {
    public MessageException(){
        super();
    }

    public MessageException(String message){
        super(message);
    }

    public MessageException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageException(Throwable cause){
        super(cause);
    }
}
