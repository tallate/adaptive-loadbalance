package com.aliware;

import org.apache.dubbo.common.utils.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class RandomUtil {

    /**
     * 给出一个权重数组，按权重选出其中一个
     *
     * @return 下标
     */
    public static int randOne(List<Double> weights) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(weights), "权重数组不能为空");
        if (weights.size() == 1) {
            return 0;
        }
        double sum = weights.stream()
                .reduce((w1, w2) -> w1 + w2)
                .orElse(1.0);
        return randOne(weights, sum);
    }

    public static int randOne(List<Double> weights, double sum) {
        List<Double> normalized = weights.stream()
                .map(weight -> {
                    if (weight <= 0) {
                        throw new RuntimeException("权重不可 <= 0");
                    }
                    return weight / sum;
                })
                .collect(Collectors.toList());
        double pos = randDbl(0, 1);
        double curSum = 0;
        for (int i = 0; i < normalized.size(); i++) {
            curSum += normalized.get(i);
            if (curSum >= pos) {
                return i;
            }
        }
        throw new RuntimeException("wtf???");
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
