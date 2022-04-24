package edu.jiangxin.apktoolbox.file.crack;

import org.junit.Assert;
import org.junit.Test;


public class PasswordCrackerTaskTest {

    @Test
    public void testFindPasswordInRange() {
        PasswordFuture passwordFuture = new PasswordFuture();
        PasswordCrackerConsts consts = new PasswordCrackerConsts(1, 6, new StringCracker("c4b9942f2886cd34fce932f279000ef3"));
        PasswordCrackerTask task = new PasswordCrackerTask(0, true,  consts, passwordFuture);
        String password = task.findPasswordInRange(64250866, 64250900, consts.getCracker());
        Assert.assertEquals("Output must be 1294ab", "1294ab", password);
    }

    @Test
    public void testEncryption() {
        Assert.assertEquals("Output: ", StringCracker.encrypt("1294ab", StringCracker.getMessageDigest()), "c4b9942f2886cd34fce932f279000ef3");
        Assert.assertEquals("Output: ", StringCracker.encrypt("2nowbv", StringCracker.getMessageDigest()), "f92f8fa7fd6a5fa45d53227ffec0d6ac");
    }

    @Test
    public void testTransformation() {
        PasswordFuture passwordFuture = new PasswordFuture();
        PasswordCrackerConsts consts = new PasswordCrackerConsts(1, 6, new StringCracker("c4b9942f2886cd34fce932f279000ef3"));
        PasswordCrackerTask task = new PasswordCrackerTask(0, true,  consts, passwordFuture);
        int[] array = new int[consts.getPasswordLength()];

        task.transformDecToBase36(13007, array);
        Assert.assertArrayEquals("Output: " , new int[]{0, 0, 0, 10, 1, 11}, array);

        task.transformDecToBase36(64250867, array);
        Assert.assertArrayEquals("Output: ", new int[]{1, 2, 9, 4, 10, 11}, array);

        task.transformDecToBase36(623714257, array);
        Assert.assertArrayEquals("Output: ", new int[]{10, 11, 12, 13, 0, 1}, array);
    }
}