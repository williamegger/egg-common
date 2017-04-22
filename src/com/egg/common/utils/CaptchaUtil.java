package com.egg.common.utils;

import com.egg.common.log.LogKit;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    public static final String CHECK_CODE = "_CHECK_CODE_";

    /**
     * 创建验证码
     */
    public static void buildCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int width = 113;
        int height = 32;
        String content = randomStr(4);
        BufferedImage image = buildImage(width, height, content);

        // 将生成的字符串存储在session中
        HttpSession session = request.getSession();
        session.setAttribute(CHECK_CODE, content);

        // 禁止图像缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 创建二进制的输出流
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            // 将图像输出到Servlet输出流中。
            ImageIO.setUseCache(false); // 禁止写磁盘缓存文件。去掉这一句可能会造成打开缓存文件不能被释放。
            ImageIO.write(image, "jpeg", sos);
        } catch (Exception e) {
            LogKit.error("输出验证码出现异常: ", e);
        } finally {
            try {
                image.flush();
                if (null != sos) {
                    sos.flush();
                    sos.close();
                }
            } catch (Exception ex) {
                LogKit.error("关闭资源时出现异常", ex);
            }
        }
    }

    private static BufferedImage buildImage(int width, int height, String content) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Random random = new Random(); // 创建一个随机类

        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE); // 背景颜色要偏淡
        g.fillRect(0, 0, width, height); // 画背景

        g.setColor(getRandColor(0, 255)); // 边框颜色
        g.drawRect(0, 0, width - 1, height - 1); // 画边框

        // g.setColor(getRandColor(160, 200)); // 随机产生5条干扰线，使图象中的认证码不易被其它程序探测到
        // for (int i = 0; i < 8; i++) {
        // 	int x = random.nextInt(width);
        // 	int y = random.nextInt(height);
        // 	int x1 = random.nextInt(width);
        // 	int y1 = random.nextInt(height);
        // 	g.drawLine(x, y, x1, y1);
        // }
        //
        // g.setColor(getRandColor(160, 200)); // 随机产生100点，使图象中的认证码不易被其它程序探测到
        // for (int i = 0; i < 100; i++) {
        // 	int x = random.nextInt(width);
        // 	int y = random.nextInt(height);
        // 	g.drawLine(x, y, x, y);
        // }

        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.setColor(getRandColor(60, 250));
            g.drawOval(x, y, y, y);
        }

        int fontSize = (int) Math.floor(height * 0.85);
        Font font = new Font("Consolas", Font.BOLD, fontSize); // Times New Roman, 创建字体，字体的大小应该根据图片的高度来定。
        g.setFont(font); // 设置字体

        // 用随机产生的颜色将验证码绘制到图像中。
        // 生成随机颜色(因为是做前景，所以偏深)
        // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
        for (int i = 0, len = content.length(); i < len; i++) {
            String ch = String.valueOf(content.charAt(i));
            int tmp = random.nextInt((int) (height - fontSize * 0.75));
            g.drawString(ch, fontSize * i + 8, height - tmp);
        }
        g.dispose(); // 图像生效

        return image;
    }

    private static String randomStr(int len) {
        String s = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
        int sLen = s.length();

        StringBuilder sb = new StringBuilder(len);
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(String.valueOf(s.charAt(random.nextInt(sLen))));
        }
        return sb.toString();
    }

    /**
     * 得到验证码
     */
    public static String getCaptcha(HttpSession session) {
        if (null == session) {
            return null;
        }
        Object checkCode = session.getAttribute(CHECK_CODE);
        return (null == checkCode) ? null : checkCode.toString();
    }

    /**
     * 验证传入的验证码
     */
    public static boolean verifyCaptcha(HttpSession session, String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        String captcha = getCaptcha(session);
        if (captcha == null || captcha.isEmpty()) {
            return false;
        }
        return captcha.equalsIgnoreCase(code);
    }

    private static Color getRandColor(int lower, int upper) {
        Random random = new Random();
        if (upper > 255) {
            upper = 255;
        }
        if (upper < 1) {
            upper = 1;
        }
        if (lower < 1) {
            lower = 1;
        }
        if (lower > 255) {
            lower = 255;
        }
        int r = lower + random.nextInt(upper - lower);
        int g = lower + random.nextInt(upper - lower);
        int b = lower + random.nextInt(upper - lower);
        return new Color(r, g, b);
    }

}
