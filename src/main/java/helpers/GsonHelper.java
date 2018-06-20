package helpers;

import com.google.gson.GsonBuilder;

/**
 * Helper class for convert any object with annotated @Expose fields to JSON string
 */
public class GsonHelper {
    private Object object;

    public GsonHelper(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create().toJson(this.object);
    }
}
