package com.owm.findimg;

/**
 * Created by owm on 2017/9/19.
 */

public class FindImgUtils {
//    /**
//     * 小图是否属于大图
//     *
//     * @param mubiao 目标图片（大图）地址
//     * @param yuantu 源图片（小图）地址
//     * @return
//     * @throws IOException
//     */
//    private static boolean FindImg(String mubiao, String yuantu) throws IOException {
//        boolean isFind = false;
//        Bitmap yuantusource = BitmapFactory.decodeFile(yuantu, null);
//        Bitmap mubiaosource = BitmapFactory.decodeFile(mubiao, null);
////		  huidu("yuantu" , yuantu);
////		  huidu("mubiao" , mubiao);
//        String mubiaoHashCode;
//        String yuantuHashCode;
//        Bitmap jiequsource;
//        int width = yuantusource.getWidth();
//        int height = yuantusource.getHeight();
//        int Mwidth = mubiaosource.getWidth();
//        int Mheight = mubiaosource.getHeight();
//        mubiaoHashCode = BufproduceFingerPrint(mubiaosource);
//        //通过循环来查找图片（就是从左上到右下）
//        for (int i = 0; i < width - Mwidth; i++) {
//            for (int j = 0; j < height - Mheight; j++) {
//                jiequsource = Bitmap.createBitmap(yuantusource, i, j, width, height, null,
//                        false);
//                yuantuHashCode = BufproduceFingerPrint(jiequsource);
//                int difference = hammingDistance(mubiaoHashCode, yuantuHashCode);
//                if (difference == 0) {
//                    ImageUtils.savePhotoToSDCard(context, "找到", jiequsource);
//                    SharedUtil.saveData(context, "Find", "x", String.valueOf(i));
//                    SharedUtil.saveData(context, "Find", "y", String.valueOf(j));
////					  LogInfo.ceshi("找到2：x="+ backX + "y=" + j);
//                    isFind = true;
//                    break;
//                } else {
////					  LogInfo.ceshi("没找到2：x="+ backX + "y=" + j);
////					  ToastUtil.showToast(context, "正在查找中···");
//                }
//            }
//        }
//
//        if (isFind) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 处理图片
//     *
//     * @param source
//     * @return
//     */
//    public static String BufproduceFingerPrint(Bitmap source) {
////	    BufferedImage source = ImageHelper.readPNGImage(filename);// 读取文件
//        int width = 8;
//        int height = 8;
//        int pixelColor;
//        // 第一步，缩小尺寸。
//        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
//        Bitmap thumb = ImageHelper.zoomImage(source, width, height);
//
//        // 第二步，简化色彩。
//        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
//        int[] pixels = new int[width * height];
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
////	    	  System.out.println("i=" + i +";y=" + j);
////	    	  pixelColor = thumb.getPixel(i, j);
////	    	  R = Color.red(pixelColor);
////              G = Color.green(pixelColor);
////              B = Color.blue(pixelColor);
//                pixels[i * height + j] = ImageHelper.rgbToGray(thumb.getPixel(i, j));
//            }
//        }
//
//        // 第三步，计算平均值。
//        // 计算所有64个像素的灰度平均值。
//        int avgPixel = ImageHelper.average(pixels);
//
//        // 第四步，比较像素的灰度。
//        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
//        int[] comps = new int[width * height];
//        for (int i = 0; i < comps.length; i++) {
//            if (pixels[i] >= avgPixel) {
//                comps[i] = 1;
//            } else {
//                comps[i] = 0;
//            }
//        }
//
//        // 第五步，计算哈希值。
//        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
//        StringBuffer hashCode = new StringBuffer();
//        for (int i = 0; i < comps.length; i += 4) {
//            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
//                    * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1)
//                    + comps[i + 2];
//            hashCode.append(binaryToHex(result));
//        }
//
//        // 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
//        return hashCode.toString();
//    }
//
//    private static char binaryToHex(int binary) {
//        char ch = ' ';
//        switch (binary) {
//            case 0:
//                ch = '0';
//                break;
//            case 1:
//                ch = '1';
//                break;
//            case 2:
//                ch = '2';
//                break;
//            case 3:
//                ch = '3';
//                break;
//            case 4:
//                ch = '4';
//                break;
//            case 5:
//                ch = '5';
//                break;
//            case 6:
//                ch = '6';
//                break;
//            case 7:
//                ch = '7';
//                break;
//            case 8:
//                ch = '8';
//                break;
//            case 9:
//                ch = '9';
//                break;
//            case 10:
//                ch = 'a';
//                break;
//            case 11:
//                ch = 'b';
//                break;
//            case 12:
//                ch = 'c';
//                break;
//            case 13:
//                ch = 'd';
//                break;
//            case 14:
//                ch = 'e';
//                break;
//            case 15:
//                ch = 'f';
//                break;
//            default:
//                ch = ' ';
//        }
//        return ch;
//    }
//
//    /**
//     * 2个是否相同，0为相同
//     *
//     * @param sourceHashCode
//     * @param hashCode
//     * @return
//     */
//    public static int hammingDistance(String sourceHashCode, String hashCode) {
//        int difference = 0;
//        int len = sourceHashCode.length();
//
//        for (int i = 0; i < len; i++) {
//            if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
//                difference++;
//            }
//        }
//
//        return difference;
//    }

}
