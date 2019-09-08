package ab.tjl.gmall.user.controller;

import java.util.Random;

/**
 * @author:tangjilin
 * @Description:随机抽取
 * @Date:Created in 19:14 2019/9/5
 * @Modified By:
 */
public class RandomPeople {
    public static void main(String[] args) {
        Random col = new Random();
        Random row = new Random();

        int lie = col.nextInt(1)+1;
        int hang = row.nextInt(8)+1;

        System.out.println("有请第"+lie+"列"+"第"+hang+"行"+"的同学回答问题。。。");
    }
}
