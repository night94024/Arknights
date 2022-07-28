package top.strelitzia.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaEvent;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.EventEnum;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.strelitzia.util.PetPetUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@Service
@Slf4j
public class SuperloveService {

    @Autowired
    private PetPetUtil petPetUtil;

    @AngelinaGroup(keyWords = {""}, description = "呼叫")
    public ReplayInfo GroupWallPaper(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        String Moth = "runFile/petpet/love/find.gif";
        replayInfo.setReplayImg(new File(Moth));
        replayInfo.setReplayMessage("博士这是在叫我吗？");
        return replayInfo;
    }

    @AngelinaEvent(event = EventEnum.NudgeEvent, description = "发送头像的摸头动图")
    public ReplayInfo Group(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        String Poke = "runFile/petpet/love/poke.gif";
        replayInfo.setReplayImg(new File(Poke));
        replayInfo.setReplayMessage("不可以戳蒂蒂的头,蒂蒂的帽子会掉的");
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"有看见大姐头吗"})
    public ReplayInfo Groups(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        String Ice = "runFile/petpet/love/ice.gif";
        replayInfo.setReplayImg(new File(Ice));
        replayInfo.setReplayMessage("博士，在这个桌子下面");
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"明日方舟"})
    public ReplayInfo GroupRecall(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        String Arknights = "runFile/petpet/sound/Arknights.mp3";
        replayInfo.setMp3(new File(Arknights));
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"爱你"}, description = "抽取一张蒂蒂的图片发送到群里")
    public ReplayInfo GroupPicture(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayMessage("我也爱你，Mr." + messageInfo.getName());
        String folderPath = "runFile/DIDI";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (folder.isDirectory()) {
            int picNum = files.length;
            int selectPicIndex = (int) (Math.random() * (double) picNum);
            File selectFile = files[selectPicIndex];
            String oriFileName = selectFile.getAbsolutePath();
            replayInfo.setReplayImg(new File(oriFileName));
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"喜欢"}, description = "发送一张图片")
    public ReplayInfo GroupsN(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayImg(("https://iw233.cn/api/Random.php"));
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"大佐"})
    public ReplayInfo MuteSomeOne(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        String ba = "runFile/petpet/sound/da.mp3";
        replayInfo.setMp3(new File(ba));
        return replayInfo;
    }
}
