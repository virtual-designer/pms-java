package org.nsu.cse215.labgroup3.pms.database;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.time.Instant;

public class ModelSerializer {
    public void serialize(Object object, Element element) {
        Class<?> clazz = object.getClass();

        if (!clazz.isAnnotationPresent(Model.class)) {
            throw new IllegalStateException("Cannot serialize object of %s class".formatted(clazz.getName()));
        }

        for (Field field : clazz.getDeclaredFields()) {
            String name = getFieldName(field);

            if (name == null) {
                continue;
            }

            try {
                element.setAttribute(name, field.get(object).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> T deserialize(Class<T> clazz, Node userNode) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        NamedNodeMap attributes = userNode.getAttributes();
        T object = clazz.getConstructor().newInstance();

        if (!clazz.isAnnotationPresent(Model.class)) {
            throw new IllegalStateException("Cannot serialize object of %s class".formatted(clazz.getName()));
        }

        for (Field field : clazz.getDeclaredFields()) {
            String name = getFieldName(field);

            if (name == null) {
                continue;
            }

            String valueString = attributes.getNamedItem(name).getTextContent();
            Object value;
            Type type = field.getType();

            if (type == Integer.class) {
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
            else if (type == Instant.class) {
                value = Instant.parse(valueString);
            }
            else {
                value = valueString;
            }

            field.set(object, value);
        }

        return object;
    }

    private String getFieldName(Field field) {
        if (!field.isAnnotationPresent(org.nsu.cse215.labgroup3.pms.database.Field.class)) {
            return null;
        }

        final var fieldAnnotation = field.getAnnotation(org.nsu.cse215.labgroup3.pms.database.Field.class);
        String name = fieldAnnotation.name();

        if (name.isEmpty()) {
            name = field.getName();
        }

        field.setAccessible(true);
        return name;
    }
}
