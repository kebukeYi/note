package com.java.note.tool;

import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.constant.PermName;
import org.apache.rocketmq.common.help.FAQUrl;
import org.apache.rocketmq.common.protocol.ResponseCode;
import org.apache.rocketmq.common.protocol.header.SendMessageRequestHeader;
import org.apache.rocketmq.common.sysflag.TopicSysFlag;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

/**
 * @ClassName Test
 * @Author kebukeyi
 * @Date 2022/4/19 0:28
 * @Description
 * @Version 1.0.0
 */
public class test {

    protected RemotingCommand msgCheck(final ChannelHandlerContext ctx, final SendMessageRequestHeader requestHeader, final RemotingCommand response) {
        //第一步：检查消息发送是否合理
        //1. 检查Broker是否有写权限
        if (!PermName.isWriteable(this.brokerController.getBrokerConfig().getBrokerPermission()) && this.brokerController.getTopicConfigManager().isOrderTopic(requestHeader.getTopic())) {
            response.setCode(ResponseCode.NO_PERMISSION);
            response.setRemark("the broker[" + this.brokerController.getBrokerConfig().getBrokerIP1() + "] sending message is forbidden");
            return response;
        }
        //2.检查topic是否可以进行消息发送。主要针对默认主题，默认主题不能发送消息，仅供路由查找
        if (!this.brokerController.getTopicConfigManager().isTopicCanSendMessage(requestHeader.getTopic())) {
            String errorMsg = "the topic[" + requestHeader.getTopic() + "] is conflict with system reserved words. ";
            response.setCode(ResponseCode.SYSTEM_ERROR);
            response.setRemark(errorMsg);
            return response;
        }
        //
        TopicConfig topicConfig = this.brokerController.getTopicConfigManager().selectTopicConfig(requestHeader.getTopic());
        if (null == topicConfig) {
            int topicSysFlag = 0;
            if (requestHeader.isUnitMode()) {
                if (requestHeader.getTopic().startsWith(MixAll.RETRY_GROUP_TOPIC_PREFIX)) {
                    topicSysFlag = TopicSysFlag.buildSysFlag(false, true);
                } else {
                    topicSysFlag = TopicSysFlag.buildSysFlag(true, false);
                }
            }
            //在NameServer端存储主题的配置信息
            topicConfig = this.brokerController.getTopicConfigManager().createTopicInSendMessageMethod(
                    requestHeader.getTopic(), requestHeader.getDefaultTopic(),
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel()), requestHeader.getDefaultTopicQueueNums(), topicSysFlag);
            //检查队列，如果队列不合法，则返回错误码
            if (null == topicConfig) {
                if (requestHeader.getTopic().startsWith(MixAll.RETRY_GROUP_TOPIC_PREFIX)) {
                    topicConfig = this.brokerController.getTopicConfigManager().createTopicInSendMessageBackMethod(requestHeader.getTopic(), 1, PermName.PERM_WRITE |
                            PermName.PERM_READ, topicSysFlag);
                }
            }
            //
            if (null == topicConfig) {
                response.setCode(ResponseCode.TOPIC_NOT_EXIST);
                response.setRemark("topic[" + requestHeader.getTopic() + "] not exist, apply first please !" + FAQUrl.suggestTodo(FAQUrl.APPLY_TOPIC_URL));
                return response;
            }
        }
        //
        int queueIdInt = requestHeader.getQueueId();
        int idValid = Math.max(topicConfig.getWriteQueueNums(), topicConfig.getReadQueueNums());
        //
        if (queueIdInt >= idValid) {
            String errorInfo = String.format("request queueId[%d] is illegal, % s Producer: %s ",
                    queueIdInt, topicConfig.toString(),
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel()));
            response.setCode(ResponseCode.SYSTEM_ERROR);
            response.setRemark(errorInfo);
            return response;
        }
        return response;
    }
}
