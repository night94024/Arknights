package top.strelitzia.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.angelinaBot.dao.ActivityMapper;
import top.angelinaBot.model.ReplayInfo;
import top.angelinaBot.model.TextLine;
import top.angelinaBot.util.MiraiFrameUtil;
import top.angelinaBot.util.SendMessageUtil;
import top.strelitzia.dao.UserFoundMapper;

import java.io.File;
import java.util.Date;

/**
 * @author wangzy
 * @Date 2020/12/8 15:53
 **/

@Component
@Slf4j
public class CleanDayCountJob {

    @Autowired
    private UserFoundMapper userFoundMapper;


    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private SendMessageUtil sendMessageUtil;

    //每天凌晨四点重置抽卡次数
    @Scheduled(cron = "${scheduled.cleanJob}")
    @Async
    public void cleanDayCountJob() {
        userFoundMapper.cleanTodayCount();
        log.info("{}每日抽卡数清空", new Date());
    }

    @Scheduled(cron = "${scheduled.exterminateJob}")
    @Async
    public void exterminateJob() {
        ReplayInfo replayInfo = new ReplayInfo();
        TextLine textLine = new TextLine();
        textLine.addCenterStringLine("剿灭提醒");
        textLine.addString("我是本群剿灭小助手，今天是本周最后一天，博士不要忘记打剿灭哦❤");
        textLine.addCenterStringLine("道路千万条，剿灭第一条");
        textLine.addCenterStringLine("剿灭忘记打，博士两行泪");
        textLine.addString("洁哥主页：https://www.angelina-bot.top/");
        replayInfo.setReplayImg(textLine.drawImage());
        for (Long groupId : MiraiFrameUtil.messageIdMap.keySet()) {
            replayInfo.setGroupId(groupId);
            replayInfo.setLoginQQ(MiraiFrameUtil.messageIdMap.get(groupId));
            sendMessageUtil.sendGroupMsg(replayInfo);
        }
    }

    @Scheduled(cron = "${scheduled.edejob}")
    @Async
    public void edejob() {
        ReplayInfo replayInfo = new ReplayInfo();
        String Hu = "runFile/petpet/Hu.gif";
        replayInfo.setReplayImg(new File(Hu));
        replayInfo.setReplayMessage("清理智的时间到了");
        for (Long groupId : MiraiFrameUtil.messageIdMap.keySet()) {
            replayInfo.setGroupId(groupId);
            replayInfo.setLoginQQ(MiraiFrameUtil.messageIdMap.get(groupId));
            sendMessageUtil.sendGroupMsg(replayInfo);
        }
    }
    @Scheduled(cron = "${scheduled.Zerojob}")
    @Async
    public void Zerojob() {
        ReplayInfo replayInfo = new ReplayInfo();
        TextLine textLine = new TextLine();
        textLine.addCenterStringLine("每日一醒");
        textLine.addString("我是本群小助手，浊心斯卡蒂，各位博士一天已经过去了，现在是凌晨零点，看看博士有那些忘记了的呢");
        textLine.addCenterStringLine("今日的理智清完没");
        textLine.addCenterStringLine("今天的基建换人没");
        textLine.addCenterStringLine("今日的任务做完没");
        textLine.addString("如果各位博士遇到什么困难，都可以吩咐蒂蒂的，蒂蒂会帮博士解答难题，需要的请喊蒂蒂就行");
        replayInfo.setReplayImg(textLine.drawImage());
        for (Long groupId : MiraiFrameUtil.messageIdMap.keySet()) {
            replayInfo.setGroupId(groupId);
            replayInfo.setLoginQQ(MiraiFrameUtil.messageIdMap.get(groupId));
            sendMessageUtil.sendGroupMsg(replayInfo);
        }
    }

}
