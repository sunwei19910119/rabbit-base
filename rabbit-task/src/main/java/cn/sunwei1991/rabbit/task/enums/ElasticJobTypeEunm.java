package cn.sunwei1991.rabbit.task.enums;


/**
 * @Title ElasticJobTypeEunm
 * @Description
 * @Author SunWei
 * @Create 2020/10/26 4:23 下午
 */
public enum  ElasticJobTypeEunm {

    SIMPLE("SimpleJob","简单类型job"),
    DATAFLOW("DataflowJob","流式类型job"),
    SCRIPT("ScriptJob","脚本类型类型job");

    private String type;

    private String desc;

    private ElasticJobTypeEunm(String type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
