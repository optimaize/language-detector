package com.optimaize.langdetect.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class RemoveMinorityScriptsTextFilterTest {

    @Test
    public void testWithCyrillicAndHani() throws Exception {
        RemoveMinorityScriptsTextFilter filter = RemoveMinorityScriptsTextFilter.forThreshold(0.35);
        String result = filter.filter("Hu Jintao (in Chinese 胡錦濤) and Leo Tolstoy (in Russian Лев Николаевич Толстой) are two well known people.");
        assertEquals("Hu Jintao (in Chinese ) and Leo Tolstoy (in Russian   ) are two well known people.", result);
    }

    @Test
    public void testWithChineseAndSomeEnglish() throws Exception {
        String input = "设为首页收藏本站 开启辅助访问 为首页收藏本站 开启辅助访为首页收藏本站 开启辅助访切换到窄版 请 登录 后使用快捷导航 没有帐号 注册 用户名 Email 自动登录  找回密码 密码 登录  注册 快捷导航 论坛BBS 导读Guide 排行榜Ranklist 淘帖Collection 日志Blog 相册Album 分享Share 搜索 搜索 帖子 用户 公告";

        //expect no change, the ratio 0.35 is too low
        RemoveMinorityScriptsTextFilter filter = RemoveMinorityScriptsTextFilter.forThreshold(0.42);
        assertEquals(filter.filter(input), input);

        //expect the English to be removed
        filter = RemoveMinorityScriptsTextFilter.forThreshold(0.43);
        String result = filter.filter(input);
        assertEquals("设为首页收藏本站 开启辅助访问 为首页收藏本站 开启辅助访为首页收藏本站 开启辅助访切换到窄版 请 登录 后使用快捷导航 没有帐号 注册 用户名  自动登录  找回密码 密码 登录  注册 快捷导航 论坛 导读 排行榜 淘帖 日志 相册 分享 搜索 搜索 帖子 用户 公告", result);
    }

    /**
     * Seems obvious, but better test: plain latin text may not be modified.
     */
    @Test
    public void testJustLatin() throws Exception {
        RemoveMinorityScriptsTextFilter filter = RemoveMinorityScriptsTextFilter.forThreshold(0.01);
        String text = "Hu Jintao is a well known person.";
        String result = filter.filter(text);
        assertEquals(text, result);
    }
}
