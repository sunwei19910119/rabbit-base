package cn.sunwei1991.rabbit.producer.broker;

import cn.sunwei1991.rabbit.api.Message;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Title MessageHolder
 * @Description 用于批量发送消息的消息存储
 * @Author SunWei
 * @Create 2020/10/22 9:11 上午
 */
public class MessageHolder {

    private List<Message> messages = Lists.newArrayList();

    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal<MessageHolder>(){
        @Override
        protected MessageHolder initialValue(){
            return new MessageHolder();
        }
    };


    public static void add(Message message){
        holder.get().messages.add(message);
    }

    public static List<Message> clear(){
        List<Message> tmp = Lists.newArrayList(holder.get().messages);
        holder.remove();
        return tmp;
    }

}
