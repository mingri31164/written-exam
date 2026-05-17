package schedule;

import java.util.*;

/**
 * DAG编排
 * 输入示例：
 5 5
 0 1
 0 2
 1 3
 2 3
 3 4
 */
public class DAGSchedule {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // 节点数
        int n = sc.nextInt();

        // 边数
        int m = sc.nextInt();

        // 边集合
        int[][] edges = new int[m][2];

        for (int i = 0; i < m; i++) {
            edges[i][0] = sc.nextInt();
            edges[i][1] = sc.nextInt();
        }

        // 执行DAG编排
        List<Integer> result = dagSchedule(n, edges);

        // 判断是否存在环
        if (result.size() != n) {
            System.out.println("存在循环依赖，DAG无法执行");

        } else {
            System.out.println("DAG执行完成");
            System.out.print("执行顺序: ");
            for (int i = 0; i < result.size(); i++) {
                System.out.print(result.get(i));
                if (i != result.size() - 1) {
                    System.out.print(" -> ");
                }
            }
        }
    }

    /**
     * DAG任务编排核心方法（拓扑排序）
     *
     * 实现思路：
     * 1. 使用邻接表构建整个 DAG 图，同时统计每个节点的入度（前置依赖数量）
     * 2. 将所有入度为 0 的节点加入队列，表示这些任务当前无依赖、可直接执行
     * 3. 不断从队列中取出节点加入结果集，并“删除”其对后继节点的依赖影响：
     *      即将后继节点入度减 1
     * 4. 当某个后继节点入度减为 0 时，说明其所有前置任务均已完成，可加入队列继续执行
     * 5. 最终若结果集中节点数量等于总节点数，则说明 DAG 执行成功；
     *      否则说明图中存在环状依赖，部分任务无法被调度执行
     *
     * @param n     节点数量
     * @param edges 有向边关系：
     *              例如：0 -> 1，表示任务0执行完成后才能执行任务1
     * @return 返回一个合法的任务执行顺序
     */
    public static List<Integer> dagSchedule(int n, int[][] edges) {

        // 邻接表
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // 入度数组
        int[] inDegree = new int[n];

        // 建图
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];

            graph[from].add(to);
            // 统计入度
            inDegree[to]++;
        }

        // BFS队列
        Queue<Integer> queue = new LinkedList<>();

        // 找到所有入度为0的节点
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // 执行结果
        List<Integer> result = new ArrayList<>();
        // 拓扑排序
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            result.add(cur);

            // 遍历后继节点
            for (int next : graph[cur]) {
                inDegree[next]--;
                // 新的可执行节点
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        return result;
    }
}