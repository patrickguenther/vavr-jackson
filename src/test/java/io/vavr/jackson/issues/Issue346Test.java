package io.vavr.jackson.issues;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Option;
import io.vavr.jackson.datatype.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue346Test extends BaseTest {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ImplType.class, name = "I")
    })
    public interface BaseType {
    }

    public static class ImplType implements BaseType {
        @JsonProperty("v")
        int v;

        public ImplType() {}

        public ImplType(int v) {
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ImplType)) return false;
            ImplType implType = (ImplType) o;
            return v == implType.v;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(v);
        }
    }

    public static class BaseTypeBox {
        @JsonProperty("item")
        BaseType item;

        public BaseTypeBox() {}

        public BaseTypeBox(BaseType item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof BaseTypeBox)) return false;
            BaseTypeBox that = (BaseTypeBox) o;
            return Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item);
        }
    }

    public static class ImplTypeBox {
        @JsonProperty("item")
        ImplType item;

        public ImplTypeBox() {}

        public ImplTypeBox(ImplType item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ImplTypeBox)) return false;
            ImplTypeBox that = (ImplTypeBox) o;
            return Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item);
        }
    }

    public static class Box<T> {
        @JsonProperty("item")
        T item;

        public Box() {}

        public Box(T item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Box)) return false;
            Box<?> box = (Box<?>) o;
            return Objects.equals(item, box.item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item);
        }
    }

    public static class OptionImplTypeBox {
        @JsonProperty("item")
        Option<ImplType> item;

        public OptionImplTypeBox() {}

        public OptionImplTypeBox(Option<ImplType> item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof OptionImplTypeBox)) return false;
            OptionImplTypeBox that = (OptionImplTypeBox) o;
            return Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item);
        }
    }

    public static class OptionBaseTypeBox {
        @JsonProperty("item")
        Option<BaseType> item;

        public OptionBaseTypeBox() {}

        public OptionBaseTypeBox(Option<BaseType> item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof OptionBaseTypeBox)) return false;
            OptionBaseTypeBox that = (OptionBaseTypeBox) o;
            return Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item);
        }
    }

    public static Stream<Arguments> getScenarios() {
        return Stream.of(
                /* 1 baseline without vavr */
                Arguments.of(
                        "ImplType as BaseType",
                        new TypeReference<BaseType>() {
                        },
                        new ImplType(1),
                        "{\"@type\":\"I\",\"v\":1}"
                ),
                /* 2 baseline without vavr  */
                Arguments.of(
                        "ImplType as ImplType",
                        new TypeReference<ImplType>() {
                        },
                        new ImplType(1),
                        "{\"@type\":\"I\",\"v\":1}"
                ),
                /* 3 baseline without vavr  */
                Arguments.of(
                        "BaseTypeBox holding ImplType",
                        new TypeReference<BaseTypeBox>() {
                        },
                        new BaseTypeBox(new ImplType(1)),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                ),
                /* 4 baseline without vavr  */
                Arguments.of(
                        "ImplTypeBox holding ImplType",
                        new TypeReference<ImplTypeBox>() {
                        },
                        new ImplTypeBox(new ImplType(1)),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                ),
                /* 5 */
                Arguments.of(
                        "Option<ImplType> as Option<ImplType>",
                        new TypeReference<Option<ImplType>>() {
                        },
                        Option.of(new ImplType(1)),
                        "{\"@type\":\"I\",\"v\":1}"
                ),
                /* 6 */
                Arguments.of(
                        "Option<ImplType> as Option<BaseType>",
                        new TypeReference<Option<BaseType>>() {
                        },
                        Option.of(new ImplType(1)),
                        "{\"@type\":\"I\",\"v\":1}"
                ),
                /* 7 */
                Arguments.of(
                        "OptionImplTypeBox holding Option<ImplType>",
                        new TypeReference<OptionImplTypeBox>() {
                        },
                        new OptionImplTypeBox(Option.of(new ImplType(1))),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                ),
                /* 8 */
                Arguments.of(
                        "OptionBaseTypeBox holding Option<ImplType>",
                        new TypeReference<OptionBaseTypeBox>() {
                        },
                        new OptionBaseTypeBox(Option.of(new ImplType(1))),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                ),
                /* 9 */
                Arguments.of(
                        "Box<Option<ImplType>> holding Option<ImplType>",
                        new TypeReference<Box<Option<ImplType>>>() {
                        },
                        new Box<>(Option.of(new ImplType(1))),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                ),
                /* 10 */
                Arguments.of(
                        "Box<Option<BaseType>> holding Option<ImplType>",
                        new TypeReference<Box<Option<BaseType>>>() {
                        },
                        new Box<>(Option.of(new ImplType(1))),
                        "{\"item\":{\"@type\":\"I\",\"v\":1}}"
                )
        );
    }

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = mapper();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getScenarios")
    public void shouldSerializeObjectsCorrectly(
            String description,
            TypeReference<?> typeReference,
            Object object,
            String json
    ) throws Exception {
        assertEquals(json, objectMapper.writerFor(typeReference).writeValueAsString(object));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getScenarios")
    public void shouldDeserializeStringsIntoObjectsCorrectly(
            String description,
            TypeReference<?> typeReference,
            Object object,
            String json
    ) throws Exception {
        assertEquals(object, objectMapper.readValue(json, typeReference));
    }

}
