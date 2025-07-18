package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomUsername(){
        return faker.name().username();
    }

    public static String randomPassword(){
        return faker.number().digits(5);
    }

    public static String randomName(){
        return faker.name().firstName();
    }

    public static String randomSurname(){
        return faker.name().lastName();
    }

    public static String randomCategoryName(){
        return faker.food().fruit();
    }

    public static String randomSentence(int wordCount){
        return faker.chuckNorris().fact();
    }
}
