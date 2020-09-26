package nl.smith.mathematics.development.hva.ads.list;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkedListImplTest {

    @ParameterizedTest
    @MethodSource("size")
    public void size(String[] values, int expectedValue) {
        LinkedListImpl<String> list = buildListFromArray(values);

        assertEquals(expectedValue, list.size());
    }

    @ParameterizedTest
    @MethodSource("addAll")
    public void addAll(String[] values, String[] otherValues, int expectedValue) {
        LinkedListImpl<String> list = buildListFromArray(values);
        LinkedListImpl<String> otherList = buildListFromArray(otherValues);

        list.addAll(otherList);

        assertEquals(expectedValue, list.size());
    }

    @ParameterizedTest
    @MethodSource("get")
    public void get(String[] values, int index, String expectedValue, Exception expectedException) {
        LinkedListImpl<String> list = buildListFromArray(values);

        if (expectedException == null) {
            assertEquals(expectedValue, list.get(index));
        } else {
            Exception actualException = assertThrows(Exception.class, () -> list.get(index));
            assertEquals(expectedException.getClass(), actualException.getClass());
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("contains")
    public void contains(String[] values, String value, boolean expectedValue) {
        LinkedListImpl<String> list = buildListFromArray(values);

        assertEquals(expectedValue, list.contains(value));
    }

    private static LinkedListImpl<String> buildListFromArray(String[] values) {
        LinkedListImpl<String> list = new LinkedListImpl<>();
        for (String value : values) {
            list.add(value);
        }

        return list;
    }

    private static Stream<Arguments> size() {
        return Stream.of(
                Arguments.of(new String[]{}, 0),
                Arguments.of(new String[]{null}, 1),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, 5),
                Arguments.of(new String[]{"Osama", "Idi", "Bokassa", "Mengele", null, null}, 6)
        );
    }

    private static Stream<Arguments> addAll() {
        return Stream.of(
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, new String[]{}, 5),
                Arguments.of(new String[]{}, new String[]{"Mark", "Tom", "Frank", null, "Petra"}, 5),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, new String[]{"Osama", "Idi", "Bokassa", "Mengele", null, null}, 11)
        );
    }

    private static Stream<Arguments> get() {
        return Stream.of(
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, 0, "Mark", null),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, 3, null, null),
                Arguments.of(new String[]{}, 0, null, new IndexOutOfBoundsException("Index 0 out of bounds for length 0")),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, -1, null, new IndexOutOfBoundsException("Index -1 out of bounds for length 5"))
        );
    }

    private static Stream<Arguments> contains() {
        return Stream.of(
                Arguments.of(new String[]{}, null, false),
                Arguments.of(new String[]{}, "Mark", false),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, "Mark", true),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, null, true),
                Arguments.of(new String[]{"Mark", "Tom", "Frank", null, "Petra"}, "Colisa", false)
        );
    }
    /*public static void main(String[] args) {









        String[] strings = list.toArray(new String[20]);
        System.out.println(strings.length);

        System.out.println(list.indexOf(null));
        System.out.println(list.lastIndexOf(null));
        System.out.println(list.indexOf("Not an element"));
        System.out.println(list.lastIndexOf("Not an element"));
    }*/
}