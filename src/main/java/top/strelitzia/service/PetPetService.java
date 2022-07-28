package top.strelitzia.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaEvent;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.EventEnum;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.angelinaBot.model.TextLine;
import top.strelitzia.dao.IntegralMapper;
import top.strelitzia.model.IntegralInfo;
import top.strelitzia.util.PetPetUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class PetPetService {

    @Autowired
    private PetPetUtil petPetUtil;

    @Autowired
    private IntegralMapper integralMapper;

    @AngelinaGroup(keyWords = {"摸头", "摸我", "摸摸"}, description = "发送头像的摸头动图")
    public ReplayInfo PetPet(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        BufferedImage userImage = null;
        try {
            userImage = ImageIO.read(new URL("http://q.qlogo.cn/headimg_dl?dst_uin=" + messageInfo.getQq() + "&spec=100"));
            String path = "runFile/petpet/frame.gif";
            petPetUtil.getGif(path, userImage);
            replayInfo.setReplayImg(new File(path));
            return replayInfo;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @AngelinaEvent(event = EventEnum.GroupRecall, description = "撤回事件回复")
    public ReplayInfo GroupWallPaper(MessageInfo messageInfo) {
        String path = "runFile/petpet/hei.gif";
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayImg(new File(path));
        replayInfo.setReplayMessage("撤回？不打算给我看看吗？");
        return replayInfo;
    }


    @AngelinaGroup(keyWords = {"口我", "透透"}, description = "禁言功能")
    public ReplayInfo MuteSomeOne(MessageInfo messageInfo) {
        String path = "runFile/petpet/love/mouth.gif";
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayImg(new File(path));
        replayInfo.setReplayMessage("这是一个不好的习惯哦，博士~");
        replayInfo.setMuted((new Random().nextInt(5) + 10) * 60);
        return replayInfo;
    }

    @AngelinaEvent(event = EventEnum.MemberJoinEvent, description = "入群欢迎")
    public ReplayInfo memberJoin(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayMessage("欢迎" + messageInfo.getName()
                + "博士入群，你可以通过【蒂蒂菜单】了解蒂蒂的功能，或者你可以叫我的名字蒂蒂，我随时都在。"
                + "\n洁哥源码：https://github.com/Strelizia02/AngelinaBot"
                + "\n详细菜单请阅：https://github.com/Strelizia02/AngelinaBot/wiki");
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"签到"}, description = "每日签到")
    public ReplayInfo checkIn(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        int special =this.integralMapper.selectBySwitch();//特殊奖励查询
        //查询当天是否已签到
        Integer today = this.integralMapper.selectDayCountByQQ(messageInfo.getQq());
        if(today == null) today = 0 ;
        if (today < 1) {
            Integer integral = this.integralMapper.selectByQQ(messageInfo.getQq());
            if(integral == null) integral = 0 ;
            int add = new Random().nextInt(3) + 1;//随机加1~3分
            if(special!=0) add = special;//有特殊奖励时，加分改为特殊分
            integral = integral + add;
            this.integralMapper.integralByGroupId(messageInfo.getGroupId(),messageInfo.getName(),messageInfo.getQq(),integral);
            replayInfo.setReplayMessage("每日签到成功，获得积分："+add+"分");
            this.integralMapper.updateByQQ(messageInfo.getQq(), messageInfo.getName());//签到状态更新
        }else {
            replayInfo.setReplayMessage("您今日已签到，不可重复签到");
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"查询积分榜"}, description = "积分榜查询")
    public ReplayInfo inquireIntegral(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        if(messageInfo.getArgs().size()>1){
            //以结果判定是不是数字，如果是数字，以QQ查询，如果不是，以名字查询
            boolean result = messageInfo.getArgs().get(1).matches("[0-9]+");
            if (!result){
                String name = messageInfo.getArgs().get(1);
                List<Integer> Integral = this.integralMapper.selectByName(name);
                //判定信息是否为空
                if(Integral.size()==0){
                    replayInfo.setReplayMessage("积分榜里没有您要查询的信息呢，您看看您是不是写错了");
                }else {
                    String remind = "";
                    if(Integral.size()>1){
                        remind = "\n由于名字出现重复，可能存在查询不准确情况，请使用QQ查询";
                    }
                    replayInfo.setReplayMessage(name + "的积分为" + Integral + remind);
                }
            }else {
                Long QQ = Long.valueOf(messageInfo.getArgs().get(1));
                Integer Integral = this.integralMapper.selectByQQ(QQ);
                //判定信息是否为空，空信息可能是因为名字为数字
                if(Integral==null){
                    //将QQ转换为名字进行二次查询，仍然查不到即没有
                    String name = String.valueOf(QQ);
                    List<Integer> nameIntegral = this.integralMapper.selectByName(name);
                    if(nameIntegral.size()==0){
                        replayInfo.setReplayMessage("积分榜里没有您要查询的信息呢，您看看您是不是写错了");
                    }else{
                        String remind = "";
                        if(nameIntegral.size()>1){
                            remind = "\n由于名字出现重复，可能存在查询不准确情况，请使用QQ查询";
                        }
                        replayInfo.setReplayMessage(name + "的积分为" + nameIntegral + remind);
                    }
                }else {
                    replayInfo.setReplayMessage(QQ+"的积分为"+Integral);
                }
            }
        }else {
            StringBuilder s = new StringBuilder();
            List<IntegralInfo> integralInfoList = this.integralMapper.selectFiveByName();
            int i = 0;
            for(IntegralInfo integralInfo : integralInfoList){
                i = i + 1;
                s.append("第").append(i).append("名为：").append(integralInfo.getName());
                s.append("   他的积分为").append(integralInfo.getIntegral()).append("\n");
            }
            replayInfo.setReplayMessage(s.toString());
        }
        return replayInfo;
    }
}
