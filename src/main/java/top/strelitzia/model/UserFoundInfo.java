package top.strelitzia.model;

import kotlin.reflect.jvm.internal.impl.util.Check;

import javax.swing.plaf.PanelUI;

/**
 * @author strelitzia
 * @Date 2022/05/03 14:38
 * 用户抽卡垫刀信息
 **/
public class UserFoundInfo {
    private Long qq;
    private Integer foundCount;
    private Integer todayCount;
    private Integer allCount;
    private Integer allSix;
    private Integer allFive;
    private Long todayWife;
    private Long wifeGroup;
    private Long Check;

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public Integer getFoundCount() {
        return foundCount;
    }

    public void setFoundCount(Integer foundCount) {
        this.foundCount = foundCount;
    }

    public Integer getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(Integer todayCount) {
        this.todayCount = todayCount;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getAllSix() {
        return allSix;
    }

    public void setAllSix(Integer allSix) {
        this.allSix = allSix;
    }

    public Integer getAllFive() {
        return allFive;
    }

    public void setAllFive(Integer allFive) {
        this.allFive = allFive;
    }

    public Long getTodayWife() {
        return todayWife;
    }

    public void setTodayWife(Long todayWife) {
        this.todayWife = todayWife;
    }

    public Long getWifeGroup() {
        return wifeGroup;
    }

    public void setWifeGroup(Long wifeGroup) {
        this.wifeGroup = wifeGroup;
    }

    public Long getCheck() {
        return Check;
    }

    public void setCheak(Long Cheak){this.Check = Check;}

}
