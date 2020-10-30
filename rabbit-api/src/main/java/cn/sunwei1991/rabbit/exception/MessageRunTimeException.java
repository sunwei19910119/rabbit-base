package cn.sunwei1991.rabbit.exception;

/**
 * @Title MessageRunTimeException
 * @Description Message运行时异常
 * @Author SunWei
 * @Create 2020/10/21 10:02 上午
 */
public class MessageRunTimeException extends RuntimeException{

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message){
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause){
        super(cause);
    }
}
