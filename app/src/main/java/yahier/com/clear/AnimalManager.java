package yahier.com.clear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalManager {

    private static final Integer[] imgRightRes = {R.drawable.cat, R.drawable.dog, R.drawable.rabbit};
    private static final Integer[] types = {1, 2, 3};

    private static Animal getRandomAnimal() {
        int index = new Random().nextInt(imgRightRes.length);
        return new Animal(types[index], imgRightRes[index]);
    }

    /**
     * 获取新的行数 并且建立 左上右下的结点
     */
    public static List<Animal> getNewLine(int lineSize) {
        int size = 6 * lineSize;
        List<Animal> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Animal animal = getRandomAnimal();
            animal.setPosition(i);
            //建立左边结点
            if (i % 6 != 0) {
                Animal left = list.get(i - 1);
                animal.setNodeLeft(left);
            }

            //建立上边的结点
            if (i > 5) {
                Animal top = list.get(i - 6);
                animal.setNodeTop(top);
            }

            list.add(animal);
        }

        return list;
    }



}
