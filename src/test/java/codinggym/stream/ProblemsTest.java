package codinggym.stream;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static codinggym.stream.TestPosts.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ProblemsTest {

    @Test
    @DisplayName("01 Хорошие, свежие посты")
    void problem01() {
        // Вернуть список постов, которые набрали более 500 лайков, среди опубликованных за последние 3 месяца.
        // Сортировать список от самого свежего поста к самому старому.
        LocalDate NOW = LocalDate.of(2020, Month.OCTOBER, 26);
        List<Post> expected = Arrays.asList(BLOG_3, BLOG_6, BLOG_8);

        // Solution
        List<Post> result = POSTS.stream()
                .filter(post -> post.getLikes() > 500 && post.getDate().isAfter(NOW.minusMonths(3)))
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());

        // Check
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("02 Заголовки и URL в порядке")
    void problem02() {
        // Проверить, что у всех постов есть непустой заголовок и url находится на сайте epam.
        // Использовать готовые предикаты.

        // Solution
        Predicate<Post> isTitleNull = bp -> bp.getTitle() == null;
        Predicate<Post> isTitleBlank = bp -> bp.getTitle().trim().isEmpty();
        Predicate<Post> isUrlEpam = bp -> "www.epam.com".equalsIgnoreCase(bp.getUrl().getHost());

        boolean result = POSTS.stream()
                .allMatch(isTitleNull.negate().and(isTitleBlank.negate()).and(isUrlEpam));

        // Check
        assertTrue(result);
    }

    @Test
    @DisplayName("03 Дубликаты")
    void problem03() {
        // Найти дубликаты - посты с одинаковыми URL.
        // Map.of(K, V, K, V, K, V) - Java 9
        Map<String, Set<Post>> expected = Map.of("https://www.epam.com/insights/podcasts/joanne-chang-on-designing-a-delicious-customer-experience",
                Set.of(PODCAST_9_DUPLICATE, PODCAST_8_DUPLICATE));

        // Solution
        Map<String, Set<Post>> result = POSTS.stream()
                .collect(Collectors.groupingBy(Post::getUrl, Collectors.toSet()))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue()));

        // Check
        assertEquals(expected, result);
    }

    /*
    * What will the following code print?
    *
    * List<Integer> ls = Arrays.asList(1, 2, 3);
    * Function<Integer, Integer> func = a -> a * a;  //1
    * ls.stream().map(func).peek(System.out::print); //2
    *
    * 1) Compilation error at //1
    *
    * 2) Compilation error at //2
    *
    * 3) 149
    *
    * 4) 123
    *
    * 5) It will compile and run fine but will not print anything.
    * */

    @Test
    @DisplayName("04 Поддержим лайками")
    void problem04() {
        // Поддержим лайками. Найти 3 наименее залайканных поста, добавить каждому 100 лайков,
        // вернуть список этих постов. Затем вернуть их количество лайков к исходному.
        List<Post> expected = Arrays.asList(PODCAST_8_DUPLICATE, BLOG_2, PODCAST_5);

        // Solution
        List<Post> result = POSTS.stream()
                .sorted(Comparator.comparing(Post::getLikes))
                .limit(3)
                .peek(post -> post.setLikes(post.getLikes() + 100))
                .collect(Collectors.toList());

        result.forEach(post -> post.setLikes(post.getLikes() - 100));

        // Check
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("05 Последний элемент стрима")
    void problem05() {
        // Вернуть последний элемент стрима.
        Post expected = PODCAST_9_DUPLICATE;

        // Solution
        Post result = POSTS.stream()
                .reduce((accumulator, next) -> next)
                .orElseThrow();

        // Check
        assertEquals(expected, result);
    }

    /*
    * Given that a method named Double getPrice(String id) exists and may potentially return null, about which of the
    * following options can you be certain that a run time exception will not be thrown?
    *
    * You had to select 2 options
    *
    * 1) Optional<Double> price = Optional.of(getPrice("1111"));
    *
    * 2) Optional<Double> price = Optional.ofNullable(getPrice("1111"));
    *    Double x = price.orElse(getPrice("2222"));
    *
    * 3) Optional<Double> price = Optional.ofNullable(getPrice("1111"));
    *    Double y = price.orElseGet(()->getPrice("333"));
    *
    * 4) Optional<Double> price = Optional.of(getPrice("1111"), 10.0);
    *
    * 5) Optional<Double> price = Optional.of(getPrice("1111"));
    *    Double z = price.orElseThrow(()->new RuntimeException("Bad Code"));
    * */

    @Test
    @DisplayName("06 Сумма лайков по категориям")
    void problem06() {
        // Вернуть EnumMap содержащий сумму лайков в каждой категории PostType.
        // Map.of(K, V, K, V, K, V) - Java 9
        Map<PostType, Integer> expected = Map.of(
                PostType.BLOG, 4093,
                PostType.INTERVIEW, 4246,
                PostType.PODCAST, 2623);

        // Solution
        Map<PostType, Integer> result = POSTS.stream()
                .collect(Collectors.groupingBy(Post::getType, () -> new EnumMap<>(PostType.class), Collectors.summingInt(Post::getLikes)));

        // Check
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("07 Авторы-чемпионы")
    void problem07() {
        // Вернуть авторов, у которых более 3-х постов и их лучший пост (наибольший по лайкам).
        // Примечание: у поста может быть несколько авторов.
        Map<String, Post> expected = Map.of("Ken Gordon", PODCAST_9_DUPLICATE, "Jitin Agarwal", PODCAST_3);

        // Solution
        Map<String, Post> result = null;

        // Check
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("08 Новые авторы")
    void problem08() {
        // Сравните старый список авторов со свежим списком из постов. Найдите, какие новые авторы появились.
        // Старые списки авторов находятся в файлах в src/test/resources/authors
        Set<String> expected = new HashSet<>(Arrays.asList("Patrick Allen", "Dmitry Krasovskiy"));

        // Solution
        Set<String> result = null;

        // Check
        assertEquals(expected, result);
    }
}
