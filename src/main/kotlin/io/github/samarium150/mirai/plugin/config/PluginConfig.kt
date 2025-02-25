/**
 * Copyright (c) 2020-2021 Samarium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package io.github.samarium150.mirai.plugin.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 插件的配置
 *
 * @constructor 实例化插件配置
 * @see AutoSavePluginConfig
 */
object PluginConfig : AutoSavePluginConfig("Config") {

    /**
     * Bot 所有者账号
     */
    @ValueDescription("Bot所有者账号")
    val master: Long by value()

    /**
     * Get命令和Adv命令的模式
     */
    @ValueDescription("Get命令和Adv命令的模式：none/whitelist/blacklist")
    val mode: String by value("none")

    /**
     * 是否保存图片
     */
    @ValueDescription("是否保存图片")
    val save: Boolean by value(false)

    /**
     * 是否使用已保存的图片作为缓存
     */
    @ValueDescription("是否使用已保存的图片作为缓存")
    val cache: Boolean by value(false)

    /**
     * 是否发送图片信息
     */
    @ValueDescription("是否发送图片信息")
    val verbose: Boolean by value(true)

    /**
     * 是否启用闪照模式
     */
    @ValueDescription("是否启用闪照模式")
    val flash: Boolean by value(false)

    /**
     * 图片大小
     */
    @ValueDescription("original/regular/small/thumb/mini")
    val size: String by value("regular")

    /**
     * 获取Pixiv图片的反向代理
     */
    @ValueDescription("获取Pixiv图片的反向代理")
    val proxy: String by value("https://i.pixiv.re")

    /**
     * 默认的撤回时间(单位: s)
     */
    @ValueDescription("默认的撤回时间(单位：s)")
    var recall: Int by value(30)

    /**
     * 是否撤回图片信息
     */
    @ValueDescription("是否撤回图片信息")
    var recallImgInfo: Boolean by value(false)

    /**
     * 是否撤回图片
     */
    @ValueDescription("是否撤回图片")
    var recallImg: Boolean by value(true)

    /**
     * 默认的冷却时间(单位: s)
     */
    @ValueDescription("默认的冷却时间(单位：s)")
    var cooldown: Int by value(60)

    /**
     * 标签过滤模式
     */
    @ValueDescription("标签过滤模式: none/whitelist/blacklist")
    val tagFilterMode: String by value("none")

    /**
     * 标签过滤器
     */
    @ValueDescription("标签过滤器，白名单模式只发送包含指定标签的图片，黑名单模式只发送不包含指定标签的图片。(仅Get命令，Adv命令中不生效)")
    val tagFilter: MutableSet<String> by value()
}
