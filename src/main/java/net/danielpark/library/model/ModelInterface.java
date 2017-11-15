package net.danielpark.library.model;

import android.support.annotation.Nullable;

/**
 * All model response must implement {@link ModelInterface}
 * <br><br>
 * Created by namgyu.park on 2017. 10. 17..
 */

public interface ModelInterface {

    /**
     * Unique id in String (.. UUID)
     * <p>If unique id is Integer, then convert it to {@link String}</p>
     *
     * @return unique id
     */
    String getUniqueId();

}
