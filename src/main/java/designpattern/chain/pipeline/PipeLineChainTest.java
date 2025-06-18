package designpattern.chain.pipeline;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: pipeline责任链（Netty相关）
 * @Author: mingri31164
 * @Date: 2025/6/18 15:58
 **/
public class PipeLineChainTest {
    public static void main(String[] args) {

        // 场景：请假三天内，组长请假即可、3-7天部门经理请假、7天以上总经理请假
        LeaveRequest leaveRequest = new LeaveRequest("张三", 7, "身体不适");

        Pipeline pipeline = new Pipeline();
        pipeline.addHandler(new GroupLeader());
        pipeline.addHandler(new Manager());
        pipeline.addHandler(new GeneralManager());

        pipeline.process(leaveRequest);
    }

    @Data
    @AllArgsConstructor
    static class LeaveRequest {
        private String name;
        private Integer day;
        private String reason;

        public String desc() {
            return "姓名：" + name + " 请假天数：" + day + "天 原因：" + reason;
        }
    }

    // 抽象处理器接口
    interface Handler {
        void handle(LeaveRequest request);
    }

    // 统一处理器链
    static class Pipeline {
        private final List<Handler> handlers = new ArrayList<>();

        public void addHandler(Handler handler) {
            handlers.add(handler);
        }

        public void process(LeaveRequest request) {
            if (request.getDay() <= 0) {
                throw new IllegalArgumentException("请假天数必须大于0");
            }
            for (Handler handler : handlers) {
                handler.handle(request);
            }
        }
    }

    // 组长
    static class GroupLeader implements Handler {
        @Override
        public void handle(LeaveRequest leave) {
            if (leave.getDay() >= 1) {
                System.out.println(leave.desc() + " -> 组长批准！！！");
            }
        }
    }

    // 部门经理
    static class Manager implements Handler {
        @Override
        public void handle(LeaveRequest leave) {
            if (leave.getDay() >= 3) {
                System.out.println(leave.desc() + " -> 部门经理批准！！！");
            }
        }
    }

    // 总经理
    static class GeneralManager implements Handler {
        @Override
        public void handle(LeaveRequest leave) {
            if (leave.getDay() >= 7) {
                System.out.println(leave.desc() + " -> 总经理批准！！！");
            }
        }
    }
}