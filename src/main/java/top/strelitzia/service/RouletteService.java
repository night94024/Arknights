package top.strelitzia.service;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.angelinaBot.util.SendMessageUtil;

import java.io.File;
import java.util.*;

@Service
public class RouletteService {

    @Autowired
    SendMessageUtil sendMessageUtil;

    //轮盘赌map
    private static final Map<Long, List<Integer>> rouletteInfo = new HashMap<>();

    //轮盘赌对决map
    private static final Map<Long,List<Long>> rouletteDuel = new HashMap<>();

    @AngelinaGroup(keyWords = {"给轮盘上子弹","上膛","拔枪吧"}, description = "守护铳轮盘赌，看看谁是天命之子(多颗子弹直接在后面输入数字）")
    public ReplayInfo Roulette(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        int bulletNum ;
        //判断数字
        if (messageInfo.getArgs().size()>1){
            boolean result = messageInfo.getArgs().get(1).matches("[0-9]+");
            if (!result){
                StringBuilder s = new StringBuilder();
                char[] arr=messageInfo.getArgs().get(1).toCharArray();
                for(char c :arr){
                    if (c>=48&&c<=57){
                        s.append(c - '0');
                    }
                }
                if (s.toString().equals("")){
                    replayInfo.setReplayMessage("对不起啊博士，没能理解您的意思，请务必告诉我数字呢");
                    return replayInfo;
                }
                bulletNum = Integer.parseInt(s.toString());
            }else {
                bulletNum = Integer.parseInt(messageInfo.getArgs().get(1));
            }
            if (bulletNum > 6){
                replayInfo.setReplayMessage("博士，您装入的子弹数量太多了");
                return replayInfo;
            }else if(bulletNum == 6) {
                replayInfo.setReplayMessage("博士...您是要自杀吗");
                return replayInfo;
            }
        }else {
            bulletNum = 1;
        }
        int bullet = 0;
        if (bulletNum == 1){
            //只加一个子弹
            for (int j=0;j<6;j++){
                bullet=bullet+new Random().nextInt(2);
            }
            replayInfo.setReplayMessage("（放入了 1 颗子弹）");
            sendMessageUtil.sendGroupMsg(replayInfo);
        }else {
            //加N个子弹,随机选弹仓加入子弹，则触发位置是最小的弹仓号
            List<Integer> list = new ArrayList<>();
            List<Integer> situList = new ArrayList<>(Arrays.asList(0,1,2,3,4,5));
            for(int i=0;i<bulletNum;i++){
                Integer situ = new Random().nextInt(situList.size());
                bullet = situList.get(situ);
                situList.remove(situ);
                list.add(bullet);
            }
            bullet=Collections.min(list);
            replayInfo.setReplayMessage("（放入了 "+ bulletNum +" 颗子弹）");
            sendMessageUtil.sendGroupMsg(replayInfo);
        }
        List<Integer> rouletteInitial = new ArrayList<>(Arrays.asList(bullet,0));
        rouletteInfo.put(messageInfo.getGroupId(),rouletteInitial);
        replayInfo.setReplayMessage("这是一把充满荣耀与死亡的守护铳，不幸者将再也发不出声音。勇士们啊，扣动你们的扳机！感谢Outcast提供的守护铳！");
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"kq"}, description = "进入生死的轮回")
    public ReplayInfo PetPet(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        List<Integer> rouletteNum = rouletteInfo.get(messageInfo.getGroupId());
        //判断是否已经上膛
        if (rouletteNum == null){
            replayInfo.setReplayMessage("您还没上子弹呢");
            return replayInfo;
        }
        //取出子弹位置和开枪次数进行对比
        Integer bullet = rouletteNum.get(0);
        Integer trigger = rouletteNum.get(1);
        if(bullet.equals(trigger)){
            replayInfo.setMuted(60);//轮盘赌禁言时间
            //replayInfo.setMuted((new Random().nextInt(5) + 1) * 60);
            String Ha="runFile/petpet/Ha.gif";
            replayInfo.setReplayImg(new File(Ha));
            replayInfo.setReplayMessage("对不起，Mr."+replayInfo.getName()+"，你将会留在这里，与大海融在一起");
            //清空这一次的轮盘赌
            rouletteInfo.remove(messageInfo.getGroupId());
            return replayInfo;
        }else {
            switch ( trigger) {
                case 0 : replayInfo.setReplayMessage("从此刻开始，你们将在无退路");
                break;
                case 1 : replayInfo.setReplayMessage("英雄们啊，贯彻吾等的信念，无需害怕");
                    break;
                case 2 : replayInfo.setReplayMessage("此时此刻，我们的灵魂已经联系在了一起，无需动摇");
                    break;
                case 3 : replayInfo.setReplayMessage("伊比利亚的灯光已经在前方照耀，无需后退");
                    break;
                case 4 : replayInfo.setReplayMessage("恭喜，第五名幸运者，你活了下来，现在世界又会围绕着你而旋转");
                    break;
            }
            rouletteNum.set(1,trigger+1);
            rouletteInfo.put(messageInfo.getGroupId(),rouletteNum);
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"轮盘转动"}, description = "六人参赛，一人丧命")
    public ReplayInfo RouletteDuel(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        List<Long> QQList = rouletteDuel.get(messageInfo.getGroupId());
        if (QQList == null){
            QQList = new ArrayList<>();
        }else {
            if (QQList.contains(messageInfo.getQq())){
                replayInfo.setReplayMessage("您已经参加了轮盘对决，不要重复参加哦");
                return replayInfo;
            }
        }
        switch (QQList.size()) {
            case 0 : {
                replayInfo.setReplayMessage("这是一把充满荣耀与死亡的守护铳，六个弹槽只有一颗子弹，六位参赛者也将会有一位不幸者将再也发不出声音。\n欢迎第一位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            case 1 : {
                replayInfo.setReplayMessage("欢迎第二位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            case 2 : {
                replayInfo.setReplayMessage("欢迎第三位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            case 3 : {
                replayInfo.setReplayMessage("欢迎第四位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            case 4 : {
                replayInfo.setReplayMessage("欢迎第五位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            case 5 : {
                replayInfo.setReplayMessage("欢迎第六位挑战者" + messageInfo.getName() + "\n愿主保佑你，我的勇士。");
                QQList.add(messageInfo.getQq());
                rouletteDuel.put(messageInfo.getGroupId(),QQList);
                break;
            }
            default : replayInfo.setReplayMessage(messageInfo.getName() + "，参赛人数已满，请等待下一场参赛吧");
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"对决开始"}, description = "轮盘对决的生死抉择开始了")
    public ReplayInfo RouletteDuelBegging(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        List<Long> QQList = rouletteDuel.get(messageInfo.getGroupId());
        //查询次数决定能不能开始
        if(QQList.size() < 6){
            replayInfo.setReplayMessage("参赛人数还不足六人，还不能开始对决呢。");
            return replayInfo;
        }
        //计算子弹位置设立随机数
        int bullet = 0;
        for (int i=0;i<6;i++){
            bullet=bullet+new Random().nextInt(2);
        }
        switch (bullet) {
            case 0 : replayInfo.setQq(QQList.get(0));
                break;
            case 1 : replayInfo.setQq(QQList.get(1));
            break;
            case 2 : replayInfo.setQq(QQList.get(2));
                break;
            case 3 : replayInfo.setQq(QQList.get(3));
                break;
            case 4 : replayInfo.setQq(QQList.get(4));
                break;
            default : replayInfo.setQq(QQList.get(5));
        }
        //把获取到的禁言QQ带入禁言功能并且实现禁言
        replayInfo.setMuted(5 * 60);
        if(new Random().nextInt(100)>98){
            Bot bot = Bot.getInstance(replayInfo.getLoginQQ());
            Group group = bot.getGroupOrFail(replayInfo.getGroupId());
            int muted = 60;
            group.getOrFail(QQList.get(0)).mute(muted);
            group.getOrFail(QQList.get(1)).mute(muted);
            group.getOrFail(QQList.get(2)).mute(muted);
            group.getOrFail(QQList.get(3)).mute(muted);
            group.getOrFail(QQList.get(4)).mute(muted);
            group.getOrFail(QQList.get(5)).mute(muted);
            replayInfo.setReplayMessage("子弹炸膛了！博士！您还好吧？");
        }else {
            replayInfo.setReplayMessage("亲爱的"+replayInfo.getQq()+"，永别了，安息吧......");
        }
        rouletteDuel.remove(messageInfo.getGroupId());
        return replayInfo;
    }

}
