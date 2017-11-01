package net.danielpark.library.model;

import android.support.annotation.Nullable;

/**
 * All model response must implement {@link ModelInterface}
 * <br><br>
 * Created by namgyu.park on 2017. 10. 17..
 */

public interface ModelInterface {

	/**
	 * Unique id
	 * @return
	 */
	int getId();

	/**
	 * Unique user id
	 * @return
	 * @throws Exception
	 */
	int getUserId() throws Exception;

	boolean hasImage();

	@Nullable
	String getImage();

	boolean hasText();

	String getText();

	void setText(String text);
}
