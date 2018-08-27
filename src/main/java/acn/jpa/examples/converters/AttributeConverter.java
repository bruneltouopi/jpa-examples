package acn.jpa.examples.converters;

public interface AttributeConverter<X,Y> {
    public Y convertToDatabaseColumn(X attribute);
    public X convertToEntityAttribute(Y dbData);
}
