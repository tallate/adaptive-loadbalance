package com.aliware;

import java.util.List;

/**
 */
public class RandomUtil {

    /**
     * 加权随机
     *
     * @return 数组的下标
     */
    public static int randByWeight(double[] weights) {
        Preconditions.checkArgument(weights != null && weights.length > 0, "权重数组不能为空");
        double sum = 0;
        for (double w : weights) {
            sum += w;
        }
        double r = randDbl(0, sum);
        for (int i = 0; i < weights.length; i++) {
            r -= weights[i];
            if (r <= 0) {
                return i;
            }
        }
        throw new RuntimeException("加权随机计算错误？？？");
    }

    public static int randByWeight(List<Double> weights) {
        Preconditions.checkArgument(weights != null && !weights.isEmpty(), "权重数组不能为空");
        double sum = 0;
        for (Double w : weights) {
            Preconditions.checkArgument(w != null, "权重不能为空");
            sum += w;
        }
        double r = randDbl(0, sum);
        for (int i = 0; i < weights.size(); i++) {
            r -= weights.get(i);
            if (r <= 0) {
                return i;
            }
        }
        throw new RuntimeException("加权随机计算错误？？？");
    }

    /**
     * 计算[lb, rb]范围内的一个随机浮点数
     */
    public static double randDbl(double lb, double rb) {
        return lb + Math.random() * (rb - lb);
    }

    /**
     * 计算[lb, rb]范围内的一个随机整数
     */
    public static int randInt(int lb, int rb) {
        return (int) (lb + (Math.random() * (rb - lb) + 0.5));
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    /**
     * 创建长度为n的不重复随机数数组，元素取值范围为[1, n]
     */
    public static int[] randDistinctArr(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }
        for (int i = 1; i < n; i++) {
            int j = randInt(0, i);
            swap(arr, i, j);
        }
        return arr;
    }

}
