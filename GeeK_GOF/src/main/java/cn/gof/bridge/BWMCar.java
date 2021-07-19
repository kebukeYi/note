package cn.gof.bridge;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 16:33
 * @description:
 * @question:
 * @link: https://time.geekbang.org/column/article/202786   评论很形象
 **/
public class BWMCar extends AbstractCar {

    /**
     * 举个很简单的例子，现在有两个纬度
     * Car 车 （奔驰、宝马、奥迪等）
     * Transmission 档位类型 （自动挡、手动挡、手自一体等）
     * 按照继承的设计模式，Car是一个Abstract基类，假设有M个车品牌，N个档位一共要写M*N个类去描述所有车和档位的结合。
     * 而当我们使用桥接模式的话，我首先new一个具体的Car（如奔驰），再new一个具体的Transmission（比如自动档）。然后奔驰.set(手动档)就可以了。
     * 那么这种模式只有M+N个类就可以描述所有类型，这就是M*N的继承类爆炸简化成了M+N组合
     */

    @Override
    public void run() {
        System.out.println(transmission.numTransmission());
    }

    public static void main(String[] args) {
        final BWMCar bwmCar = new BWMCar();
        bwmCar.setTransmission(new HNHtransmission());
        bwmCar.run();
    }

}
 
