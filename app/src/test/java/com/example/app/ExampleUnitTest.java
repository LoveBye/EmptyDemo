package com.example.app;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        System.out.println(countAndSay(7));
//        System.out.println(maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
//        System.out.println(lengthOfLastWord(" Hello World "));
        System.out.println(Arrays.toString(plusOne(new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9})));
    }

    public int[] plusOne(int[] digits) {
        
        return digits;
    }

    public int lengthOfLastWord(String s) {
        s = s.trim();
        return s.length() - 1 - s.lastIndexOf(" ");
    }

    public String countAndSay(int n) {
        if (n <= 1) {
            return "1";
        }
        String preStr = countAndSay(n - 1);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < preStr.length(); ) {
            char temp = preStr.charAt(i);
            int count = 1;
            while (i + count < preStr.length() && preStr.charAt(i) == preStr.charAt(i + count)) {
                count++;
            }
            stringBuilder.append(count);
            stringBuilder.append(temp);
            i = i + count;
        }
        return stringBuilder.toString();
    }

    public int maxSubArray(int[] nums) {
        int result = nums[0];//所求的和
        int pre = 0;
        for (int i = 0; i < nums.length; i++) {
            pre = Math.max(nums[i] + pre, nums[i]);
            result = Math.max(pre, result);
        }
        return result;
    }
}