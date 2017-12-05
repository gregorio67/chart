package dymn.chart.main;

import dymn.utils.ReloadPropertyUtil;

public class TestReloading {
    public static void main(String[] args) {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ReloadPropertyUtil.getString("test.reload"));
        }
    }
}
