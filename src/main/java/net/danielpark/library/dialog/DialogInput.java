package net.danielpark.library.dialog;

import android.content.DialogInterface;
import net.danielpark.library.util.StringUtil;

/**
 * Information to create default {@link android.support.v7.app.AlertDialog}
 *
 * <br><br>
 * Created by namgyu.park on 2017. 10. 27..
 */

public final class DialogInput {

	private final String title;
	private final String message;
	private final String positiveButtonText;
	private final String negativeButtonText;
	private final String neutralButtonText;
	private final boolean isCancelable;

	/**
	 * <p>
	 * Positive button listener returns -1 <br>
	 * Negative button listener returns -2 <br>
	 * Neutral button listener returns -3 <br>
	 * </p>
	 */
	private final DialogInterface.OnClickListener clickListener;

	private DialogInput(Builder builder) {
		this.title = builder.title;
		this.message = builder.message;
		this.positiveButtonText = builder.positiveButtonText;
		this.negativeButtonText = builder.negativeButtonText;
		this.neutralButtonText = builder.neutralButtonText;
		this.isCancelable = builder.isCancelable;
		this.clickListener = builder.clickListener;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getPositiveButtonText() {
		return positiveButtonText;
	}

	public String getNegativeButtonText() {
		return negativeButtonText;
	}

	public String getNeutralButtonText() {
		return neutralButtonText;
	}

	public boolean isCancelable() {
		return isCancelable;
	}

	public DialogInterface.OnClickListener getClickListener() {
		return clickListener;
	}

	public boolean hasTitle() {
		return !StringUtil.isNullorEmpty(message);
	}

	public boolean hasMessage() {
		return !StringUtil.isNullorEmpty(message);
	}

	public boolean hasPositiveButtonText() {
		return !StringUtil.isNullorEmpty(positiveButtonText);
	}

	public boolean hasNegativeButtonText() {
		return !StringUtil.isNullorEmpty(negativeButtonText);
	}

	public boolean hasNeutralButtonText() {
		return !StringUtil.isNullorEmpty(neutralButtonText);
	}

	public boolean hasClickListener() {
		return clickListener != null;
	}

	public static class Builder {

		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private String neutralButtonText;
		private boolean isCancelable;
		private DialogInterface.OnClickListener clickListener;

		public Builder() {
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setPositiveButtonText(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		public Builder setNegativeButtonText(String negativeButtonText) {
			this.negativeButtonText = negativeButtonText;
			return this;
		}

		public Builder setMiddleButtonText(String neutralButtonText) {
			this.neutralButtonText = neutralButtonText;
			return this;
		}

		public Builder setCancelable(boolean isCancelable) {
			this.isCancelable = isCancelable;
			return this;
		}

		public Builder setClickListener(DialogInterface.OnClickListener clickListener) {
			this.clickListener = clickListener;
			return this;
		}

		public DialogInput build() {
			if (StringUtil.isNullorEmpty(title)
					&& StringUtil.isNullorEmpty(message))
				throw new RuntimeException("Title or Message shouldn't be empty!");

			if (StringUtil.isNullorEmpty(positiveButtonText)
					&& StringUtil.isNullorEmpty(negativeButtonText)
					&& StringUtil.isNullorEmpty(neutralButtonText))
				throw new RuntimeException("Make sure to set button text at least one!");

			return new DialogInput(this);
		}
	}
}
