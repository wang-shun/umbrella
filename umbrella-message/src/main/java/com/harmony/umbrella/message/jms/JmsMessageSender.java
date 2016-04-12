/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harmony.umbrella.message.jms;

import com.harmony.umbrella.message.Message;
import com.harmony.umbrella.message.MessageException;
import com.harmony.umbrella.message.MessageSender;

/**
 * jms消费生产扩展
 * 
 * @author wuxii@foxmail.com
 */
public interface JmsMessageSender extends MessageSender {

    /**
     * JMS生产/发送消息
     * 
     * @param message
     *            待发送的消息
     * @param config
     *            jms配置项
     * @return
     * @throws MessageException
     */
    boolean send(Message message, JmsConfig config) throws MessageException;

}
