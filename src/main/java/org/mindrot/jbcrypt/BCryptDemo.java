package org.mindrot.jbcrypt;

public class BCryptDemo {
    public static void main(String[] args) {
        String password = "123";

        String salt = BCrypt.gensalt(); // 每次随机出来的盐值是不同的
        System.out.println("salt = " + salt);

        String hashpw = BCrypt.hashpw(password, salt);
        System.out.println("hashpw = " + hashpw);
        System.out.println("长度 = " + hashpw.length());

        // 用做密码加密的 hash 算法都是精心设计过的，碰撞概率很低。
        // 使用同样的 hash 算法去 hash "123"，如果 hash 值一样，大概率用户的密码是正确的
        boolean checkpw = BCrypt.checkpw(password, hashpw);
        System.out.println(checkpw);
    }
}
