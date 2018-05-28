package acn.hackation.mavenproject1;

import javax.persistence.Converter;

@Converter
public class BooleanToIntegerConverter implements AttributeConverter<Boolean,Integer> {
    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute?1:0);
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return dbData>0;
    }
}
