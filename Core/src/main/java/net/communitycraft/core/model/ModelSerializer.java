package net.communitycraft.core.model;

public interface ModelSerializer<ModelType extends Model> {
    Object serialize(ModelType model) throws SerializationException;
    ModelType deserialize(Object type, Class<ModelType> modelTypeClass) throws SerializationException;
}
