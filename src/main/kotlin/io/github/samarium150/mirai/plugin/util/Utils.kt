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
package io.github.samarium150.mirai.plugin.util

import io.github.samarium150.mirai.plugin.command.Lolicon
import io.github.samarium150.mirai.plugin.command.Lolicon.set
import io.github.samarium150.mirai.plugin.config.PluginConfig
import io.github.samarium150.mirai.plugin.data.PluginData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.*
import org.jetbrains.annotations.Nullable
import java.net.Proxy

/**
 * 工具类
 *
 * @constructor 实例化工具类
 */
object Utils {

    /**
     * 检查用户是否是Bot所有者
     *
     * @param user 目标用户
     * @return 检查结果
     */
    fun checkMaster(@Nullable user: User?): Boolean {
        return user == null || user.id == PluginConfig.master
    }

    /**
     * 检查用户是否受信任
     *
     * @param user 目标用户
     * @return 检查结果
     */
    fun checkUserPerm(@Nullable user: User?): Boolean {
        return user == null || PluginData.trustedUsers.contains(user.id)
    }

    /**
     * 检查用户在群里的权限
     *
     * @param user 目标用户
     * @return 检查结果
     */
    fun checkMemberPerm(@Nullable user: User?): Boolean {
        return (user as Member).permission != MemberPermission.MEMBER
    }

    /**
     * 将字符串转为整数值
     *
     * @param value 输入的字符串
     * @param type 需要转化的类别
     * @return 转换后的值
     * @throws NumberFormatException 数值非法时抛出
     * @see Lolicon.set
     */
    @Throws(NumberFormatException::class)
    fun convertValue(value: String, type: String): Int {
        val setting = value.toInt()
        when (type) {
            "r18" -> if (setting != 0 && setting != 1 && setting != 2) throw NumberFormatException(value)
            "recall" -> if (setting < 0 || setting >= 120) throw NumberFormatException(value)
            "cooldown" -> if (setting < 0) throw NumberFormatException(value)
        }
        return setting
    }

    /**
     * 根据输入值返回代理类型
     *
     * @param value 输入的字符串
     * @return 代理的类型
     * @throws IllegalArgumentException 数值非法时抛出
     * @see Proxy.Type
     */
    @Throws(IllegalArgumentException::class)
    fun getProxyType(value: String): Proxy.Type {
        return when (value) {
            "DIRECT" -> Proxy.Type.DIRECT
            "HTTP" -> Proxy.Type.HTTP
            "SOCKS" -> Proxy.Type.SOCKS
            else -> throw IllegalArgumentException(value)
        }
    }

    /**
     * 将字符串处理为标签列表
     *
     * @param str 输入的字符串
     * @return 标签列表
     */
    fun processTags(str: String): List<List<String>> {
        val result: MutableList<List<String>> = listOf<List<String>>().toMutableList()
        val and = str.split("&")
        for (s in and) result.add(s.split("|"))
        return result.toList()
    }

    /**
     * 根据黑白名单检查标签是否合法
     *
     * @param tag 标签
     * @return 检查结果
     */
    private fun isTagAllowed(tag: String): Boolean {
        return when (PluginConfig.tagFilterMode) {
            "none" -> true
            "whitelist" -> {
                for (filter in PluginConfig.tagFilter) {
                    if (filter.toRegex(setOf(RegexOption.IGNORE_CASE)).matches(tag)) return true
                }
                false
            }
            "blacklist" -> {
                for (filter in PluginConfig.tagFilter) {
                    if (filter.toRegex(setOf(RegexOption.IGNORE_CASE)).matches(tag)) return false
                }
                true
            }
            else -> true
        }
    }

    /**
     * 根据黑白名单检查标签列表中的标签是否合法
     *
     * @param tags 标签列表
     * @return 检查结果
     */
    fun areTagsAllowed(tags: List<String>): Boolean {
        return when (PluginConfig.tagFilterMode) {
            "none" -> true
            "whitelist" -> {
                var flag = false
                for (tag in tags) {
                    if (isTagAllowed(tag)) {
                        flag = true
                        break
                    }
                }
                flag
            }
            "blacklist" -> {
                var flag = true
                for (tag in tags) {
                    if (!isTagAllowed(tag)) {
                        flag = false
                        break
                    }
                }
                flag
            }
            else -> true
        }
    }

    private val sizeMap: Map<String, Int> = mapOf(
        "original" to 0,
        "regular" to 1,
        "small" to 2,
        "thumb" to 3,
        "mini" to 4
    )

    fun getUrl(urls: Map<String, String>): String? {
        return urls[urls.keys.sortedBy { sizeMap[it] }[0]]
    }

    /**
     * 检查联系对象是否能执行命令
     *
     * @param subject 联系对象
     * @param user 命令发起者
     * @return 检查结果
     * @see CommandSender.subject
     * @see CommandSender.user
     */
    fun isPermitted(subject: Contact?, user: User?): Boolean {
        return when (PluginConfig.mode) {
            "whitelist" -> {
                when {
                    subject == null -> true
                    subject is User && PluginData.userSet.contains(subject.id) -> true
                    subject is Group &&
                        PluginData.groupSet.contains(subject.id) &&
                        PluginData.userSet.contains(user?.id) -> true
                    else -> false
                }
            }
            "blacklist" -> {
                when {
                    subject == null -> true
                    subject is User && !PluginData.userSet.contains(subject.id) -> true
                    subject is Group &&
                        !PluginData.groupSet.contains(subject.id) &&
                        !PluginData.userSet.contains(user?.id) -> true
                    else -> false
                }
            }
            else -> true
        }
    }
}
