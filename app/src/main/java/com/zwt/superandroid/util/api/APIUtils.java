package com.zwt.superandroid.util.api;

import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class APIUtils {
    public static final int INVALID_INTEGER_VALUE = -1;
    private static final int DEVICE_TYPE = 2;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String PHOTO_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String DAYLOG_TIMESTAMP_FORMAT = "yyyy-MM-dd";

    private static ExclusionStrategy sExclusionStrategy = getExclusionStrategy();

    private static ExclusionStrategy getExclusionStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(OmittedAnnotation.class) != null ||
                        f.getAnnotation(RequestBodyForm.class) != null ||
                        f.hasModifier(Modifier.STATIC) ||
                        f.hasModifier(Modifier.TRANSIENT);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }

    public static List<NameValuePair> getRequestParams(APIBaseRequest request) {
        return APIUtils.getPostRequestParams(request);
    }

    public static ArrayList<NameValuePair> getPostRequestParams(APIBaseRequest request) {
        ArrayList<NameValuePair> list = new ArrayList<>();

        try {
            Gson gson = new GsonBuilder().setExclusionStrategies(sExclusionStrategy).create();
            request.setBody(gson.toJson(request));
        } catch (Throwable r) {
            r.printStackTrace();
        }
        genFormParams(list, request);

        if (list.size() == 0) {
            return null;
        }

        return list;
    }

    private static boolean isParamExist(ArrayList<NameValuePair> list, String name) {
        for (NameValuePair pair : list) {
            if (pair.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isFieldCanBeOmitted(Field field) {
        return field.isAnnotationPresent(OmittedAnnotation.class) || Modifier.isStatic(field.getModifiers()) ||
                Modifier.isTransient(field.getModifiers());
    }

    public static boolean isFieldForm(Field field) {
        return field.isAnnotationPresent(RequestBodyForm.class);
    }

    private static void genParams(ArrayList<NameValuePair> list, Object clsObject) {
        for (Class<?> clazz = clsObject.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields == null || fields.length == 0) {
                continue;
            }

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object obj = field.get(clsObject);
                    if (obj == null || isFieldCanBeOmitted(field) || isParamExist(list, field.getName())) {
                        continue;
                    }

                    if (obj instanceof String) {
                        if (!TextUtils.isEmpty((String) obj)) {
                            list.add(new NameValuePair(field.getName(), (String) obj));
                        }
                    } else if (obj instanceof Float) {
                        if (((float) obj) > 0) {
                            list.add(new NameValuePair(field.getName(), String.valueOf(obj)));
                        }
                    } else if (obj instanceof Integer) {
                        if (((int) obj) != INVALID_INTEGER_VALUE) {
                            list.add(new NameValuePair(field.getName(), String.valueOf(((int) obj) == Integer.MIN_VALUE ? -1 : obj)));
                        }
                    } else if (obj instanceof Long) {
                        if (((long) obj) != -1) {
                            list.add(new NameValuePair(field.getName(), String.valueOf(obj)));
                        }
                    } else if ((obj instanceof List) && (((List) obj).size() > 0)) {
                        if ((((List) obj).get(0) instanceof String) || (((List) obj).get(0) instanceof Integer)) {
                            for (Object elem : (List) obj) {
                                list.add(new NameValuePair(field.getName(), String.valueOf(elem)));
                            }
                        }
                    } else if (!(obj instanceof File)) {
                        list.add(new NameValuePair(field.getName(), String.valueOf(obj)));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void genFormParams(ArrayList<NameValuePair> list, Object clsObject) {
        for (Class<?> clazz = clsObject.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields == null || fields.length == 0) {
                continue;
            }
            if (!clazz.getSimpleName().equals(APIBaseRequest.class.getSimpleName())) {
                continue;
            }

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object obj = field.get(clsObject);
                    if (obj == null || !isFieldForm(field)) {
                        continue;
                    }

                    list.add(new NameValuePair(field.getName(), (String) obj));
                } catch (Throwable r) {
                    r.printStackTrace();
                }
            }
        }
    }
}
