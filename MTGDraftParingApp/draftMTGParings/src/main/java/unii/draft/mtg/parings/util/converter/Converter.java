package unii.draft.mtg.parings.util.converter;


public interface Converter<T, N> {

    T convert(N n);

    T convert(N n, String data);
}
