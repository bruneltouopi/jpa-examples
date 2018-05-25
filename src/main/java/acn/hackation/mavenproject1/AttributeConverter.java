package acn.hackation.mavenproject1;

public interface AttributeConverter<X,Y> {
    public Y convertToDatabaseColumn(X attribute);
    public X convertToEntityAttribute(Y dbData);
}
