/*
 * Copyright 2013-2015 wuxii@foxmail.com.
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
package com.harmony.modules.scheduling;

/**
 * 定时任务trigger
 * 
 * @author wuxii@foxmail.com
 *
 */
public interface Trigger {

    /**
     * 年
     */
    public String getYears();

    /**
     * 月
     */
    public String getMonths();

    /**
     * 一个月中的第几天
     */
    public String getDayOfMonth();

    /**
     * 一周中的第几天
     */
    public String getDayOfWeek();

    /**
     * 时间
     */
    public String getHours();

    /**
     * 分钟
     */
    public String getMinutes();

    /**
     * 秒
     */
    public String getSeconds();

    /**
     * 启动延时
     * 
     * @return
     */
    public long getDelay();

}