package org.nsu.cse215.labgroup3.pms.database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelSerializer {
    private final Map<Class<? extends XMLDataSerializer<?>>, XMLDataSerializer<?>> serializerCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public Element serialize(Object object, Document document) {
        Class<?> clazz = object.getClass();

        if (!clazz.isAnnotationPresent(Model.class)) {
            throw new SerializationException("Cannot serialize object of %s class".formatted(clazz.getName()));
        }

        Model modelAnnotation = getModelAnnotation(clazz);
        String tagName = getModelTagName(clazz, modelAnnotation);
        Element element = document.createElement(tagName);

        for (Field field : clazz.getDeclaredFields()) {
            org.nsu.cse215.labgroup3.pms.database.Field fieldAnnotation = getFieldAnnotation(field);

            if (fieldAnnotation == null) {
                continue;
            }

            String name = getFieldName(field, fieldAnnotation);
            XMLDataSerializer<?> serializer = getFieldSerializer(fieldAnnotation);

            try {
                field.setAccessible(true);
                Object rawValue = field.get(object);
                element.setAttribute(name, serializer != null ? ((XMLDataSerializer<Object>) serializer).encode(rawValue) : rawValue.toString());
            } catch (IllegalAccessException e) {
                throw new SerializationException(e);
            }
        }

        return element;
    }

    public <T> T deserialize(Class<T> clazz, Node userNode) {
        T object;

        try {
            object = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!clazz.isAnnotationPresent(Model.class)) {
            throw new SerializationException("Cannot deserialize object of %s class".formatted(clazz.getName()));
        }

        Model modelAnnotation = getModelAnnotation(clazz);
        String tagName = getModelTagName(clazz, modelAnnotation);

        if (!userNode.getNodeName().equals(tagName)) {
            throw new SerializationException("Invalid tag name: %s".formatted(userNode.getNodeName()));
        }

        NamedNodeMap attributes = userNode.getAttributes();

        for (Field field : clazz.getDeclaredFields()) {
            org.nsu.cse215.labgroup3.pms.database.Field fieldAnnotation = getFieldAnnotation(field);

            if (fieldAnnotation == null) {
                continue;
            }

            String name = getFieldName(field, fieldAnnotation);
            XMLDataSerializer<?> serializer = getFieldSerializer(fieldAnnotation);

            field.setAccessible(true);

            String valueString = attributes.getNamedItem(name).getTextContent();
            Object value;
            Type type = field.getType();

            if (serializer != null) {
                value = serializer.decode(valueString);
            }
            else if (type == Integer.class) {
                value = Integer.valueOf(valueString);
            }
            else if (type == Long.class) {
                value = Long.valueOf(valueString);
            }
            else if (type == Float.class) {
                value = Float.valueOf(valueString);
            }
            else if (type == Double.class) {
                value = Double.valueOf(valueString);
            }
            else if (type == Boolean.class) {
                value = Boolean.valueOf(valueString);
            }
            else {
                value = valueString;
            }

            try {
                field.set(object, value);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return object;
    }

    private String getModelTagName(Class<?> clazz, Model modelAnnotation) {
        return modelAnnotation.tagName().isEmpty() ? clazz.getSimpleName().toLowerCase() : modelAnnotation.tagName();
    }

    private Model getModelAnnotation(Class<?> clazz) {
        return Objects.requireNonNull(clazz.getAnnotation(org.nsu.cse215.labgroup3.pms.database.Model.class));
    }

    private org.nsu.cse215.labgroup3.pms.database.Field getFieldAnnotation(Field field) {
        if (!field.isAnnotationPresent(org.nsu.cse215.labgroup3.pms.database.Field.class)) {
            return null;
        }

        return field.getAnnotation(org.nsu.cse215.labgroup3.pms.database.Field.class);
    }

    private String getFieldName(Field field, org.nsu.cse215.labgroup3.pms.database.Field fieldAnnotation) {
        String name = fieldAnnotation.name();

        if (name.isEmpty()) {
            name = field.getName();
        }

        return name;
    }

    @SuppressWarnings("unchecked")
    private XMLDataSerializer<?> getFieldSerializer(org.nsu.cse215.labgroup3.pms.database.Field fieldAnnotation) {
        final var serializerClass = fieldAnnotation.serializer();

        if (serializerClass == XMLDataSerializer.class) {
            return null;
        }

        final var cachedObject = serializerCache.getOrDefault(serializerClass, null);

        if (cachedObject != null) {
            return cachedObject;
        }

        try {
            final var serializer = serializerClass.getConstructor().newInstance();
            serializerCache.put((Class<? extends XMLDataSerializer<?>>) serializerClass, serializer);
            return serializer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
