package top.strelitzia.model;

public class IntegralInfo {
    private long groupId;
    private Integer integral;
    private String name;
    private Integer QQ;
    private Integer signCount;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQQ() { return QQ; }

    public void setQQ(Integer QQ) { this.QQ = QQ; }

    public Integer getSignCount() { return signCount; }

    public void setSignCount(Integer signCount) { this.signCount = signCount; }
}
