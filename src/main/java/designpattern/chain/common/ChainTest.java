package designpattern.chain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: 责任链普通实现
 * @Author: mingri31164
 * @Date: 2025/6/18 14:58
 **/
public class ChainTest {
    public static void main(String[] args) {

        // 场景：请假三天内，组长请假即可、3-7天部门经理请假、7天以上总经理请假
        LeaveRequest leaveRequest = new LeaveRequest("张三", 7, "身体不适");

        GroupLeader groupLeader = new GroupLeader();
        Manager manager = new Manager();
        GeneralManager generalManager = new GeneralManager();

        groupLeader.setNextHandler(manager);
        manager.setNextHandler(generalManager);
        groupLeader.submit(leaveRequest);
    }

    @Data
    @AllArgsConstructor
    static class LeaveRequest{
        private String name;
        private Integer day;
        private String reason;

        public String desc(){
            return "姓名：" + name + " 请假天数：" + day + "天 原因：" + reason;
        }
    }

    // 抽象处理器
    @Data
    static abstract class Handler{
        Handler nextHandler;
        int start;  // 当前处理器可以处理的最小天数
        protected final static int NUM_ONE = 1;
        protected final static int NUM_THREE = 3;
        protected final static int NUM_SEVEN = 7;

        public Handler(int start) {
            this.start = start;
        }

        public boolean submit(LeaveRequest request){
            if(request.getDay() <= 0)
                throw new IllegalArgumentException("请假天数必须大于0");
            if(start <= request.getDay()){
                return dealLeave(request) && nextHandler != null ? nextHandler.submit(request) : true;
            }
            return true;
        }

        public abstract boolean dealLeave(LeaveRequest leave);
    }

    // 组长
    static class GroupLeader extends Handler {

        public GroupLeader() {
            super(Handler.NUM_ONE);
        }

        @Override
        public boolean dealLeave(LeaveRequest leave) {
            System.out.println(leave.desc() + "->  组长批准！！！");
            return true;
        }
    }

    // 部门经理
    static class Manager extends Handler{

        public Manager() {
            super(Handler.NUM_THREE);
        }

        @Override
        public boolean dealLeave(LeaveRequest leave) {
            System.out.println(leave.desc() + "->  部门经理批准！！！");
            return true;
        }
    }

    // 总经理
    static class GeneralManager extends Handler{

        public GeneralManager() {
            super(Handler.NUM_SEVEN);
        }

        @Override
        public boolean dealLeave(LeaveRequest leave) {
            System.out.println(leave.desc() + "->  总经理批准！！！");
            return true;
        }
    }
}

