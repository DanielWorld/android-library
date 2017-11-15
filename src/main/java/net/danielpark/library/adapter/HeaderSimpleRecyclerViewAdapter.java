package net.danielpark.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import net.danielpark.library.model.ModelInterface;

import java.util.ArrayList;
import java.util.Collection;

/**
 * RecyclerView adapter which manages Header / Footer and contains only one viewHolder.
 * <br><br>
 * Created by namgyu.park on 2017. 10. 18..
 */

public abstract class HeaderSimpleRecyclerViewAdapter<T extends ModelInterface, H, F> extends SimpleRecyclerViewAdapter<T, RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = Integer.MIN_VALUE;		// Header item view type
    private final int TYPE_FOOTER = Integer.MAX_VALUE;		// Footer item view type

    protected H mHeaderData;
    protected F mFooterData;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        }
        else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateBasicViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;

        if (position < getHeaderCount() && holder.getItemViewType() == TYPE_HEADER) {
            onBindHeaderViewHolder(holder, position);
        } else if (useHeader() && position > getBasicItemCount() + (getHeaderCount() - 1) && holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterViewHolder(holder, position);
        } else if (!useHeader() && position >= getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterViewHolder(holder, position);
        }
        else {
            onBindBasicItemViewHolder(holder, position - (useHeader() ? getHeaderCount() : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = getBasicItemCount();
        itemCount += useHeader() ? getHeaderCount() : 0;
        itemCount += useFooter() ? getFooterCount() : 0;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (useHeader() && position < getHeaderCount()) {
            return TYPE_HEADER;
        }

        if (useHeader() && position > getBasicItemCount() + (getHeaderCount() - 1) && useFooter())
            return TYPE_FOOTER;
        else if (!useHeader() && position >= getBasicItemCount() && useFooter())
            return TYPE_FOOTER;

        return super.getItemViewType(position);
    }

    /**
     * Get item's count without Header, Footer count
     * @return  data's item count except for header and footer
     */
    public int getBasicItemCount() {
        return mDataList.size();
    }

    public abstract boolean useHeader();

    public abstract boolean useFooter();

    public abstract int getHeaderCount();

    public abstract int getFooterCount();

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract void onBindBasicItemViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Clear all data (Make sure to call this before updating all data)
     */
    @Override
    public void clearData() {
        mHeaderData = null;
        mFooterData = null;

        super.clearData();
    }

    /**
     * Update Header data
     *
     * @param newData	if it's null, then consider there's no Header data
     */
    public void updateHeaderData(H newData) {
        boolean existHeader = mHeaderData != null;

        mHeaderData = newData;

        // Header data existed before
        if (existHeader) {
            // newData size > 0
            if (mHeaderData != null) {
                notifyItemRangeChanged(0, getHeaderCount());
            } else {	// newData size == 0
                notifyItemRangeRemoved(0, getHeaderCount());
            }
        }
        // No Header data existed before
        else {
            // newData size > 0
            if (mHeaderData != null) {
                notifyItemRangeInserted(0, getHeaderCount());
            } else {	// newData size == 0
                // No Header data has existed. even now!
            }
        }
    }

    /**
     * Update Footer data
     *
     * @param newData		if it's null, then consider there's no Footer data
     */
    public void updateFooterData(F newData) {
        boolean existFooter = mFooterData != null;

        mFooterData = newData;

        // Footer data existed before
        if (existFooter) {
            // newData size > 0
            if (mFooterData != null) {
                notifyItemRangeChanged((useHeader() ? getHeaderCount() : 0) + getBasicItemCount(), getFooterCount());
            } else {	// newData size == 0
                notifyItemRangeRemoved((useHeader() ? getHeaderCount() : 0) + getBasicItemCount(), getFooterCount());
            }
        }
        // No Footer data existed before
        else {
            // newData size > 0
            if (mFooterData != null) {
                notifyItemRangeInserted((useHeader() ? getHeaderCount() : 0) + getBasicItemCount(), getFooterCount());
            } else {	// newData size == 0
                // No Footer data has existed. even now!
            }
        }
    }


    /**
     * Add new data to current data at index in List. <br>
     *    <p> load next page. no checking duplicate data </p>
     *
     * @param newData       It should implement List.
     * @param newNextUrl    next url for data
     * @param index         position
     */
    @Override
    public void addDataList(ArrayList<T> newData, String newNextUrl, int index) {
        if (mDataList == null) return;
        if (newData == null) return;

        mDataList.addAll(index, newData);
        mNextDataList = newNextUrl;

        notifyItemRangeInserted(useHeader() ? getHeaderCount() + index : index, newData.size());
    }

    /**
     * Add one data
     *
     * @param addData new data for adding
     */
    @Override
    public void addData(T addData) {
        if (mDataList == null) return;
        if (addData == null) return;
        if (addData instanceof Collection) return;

        mDataList.add(addData);

        int addPosition = mDataList.size() - 1;

        // If Header exists, change index position
        addPosition += useHeader() ? getHeaderCount() : 0;

        notifyItemInserted(addPosition);
    }

    /**
     * Add one data at index
     *
     * @param addData new data for adding
     * @param index position of data to add
     */
    @Override
    public void addData(T addData, int index) {
        if (mDataList == null) return;
        if (addData == null) return;
        if (addData instanceof Collection) return;

        if (index > mDataList.size() || index < 0) {
            mDataList.add(addData);

            int addPosition = mDataList.size() - 1;

            // If Header exists, change index position
            addPosition += useHeader() ? getHeaderCount() : 0;

            notifyItemInserted(addPosition);
        } else {
            mDataList.add(index, addData);

            // If Header exists, change index position
            index += useHeader() ? getHeaderCount() : 0;

            notifyItemInserted(index);
        }
    }

    /**
     * Update one data
     *
     * @param updateData simple object. not List
     */
    @Override
    public void updateOneData(T updateData) {
        if (mDataList == null) return;
        if (updateData == null) return;
        if (updateData instanceof Collection) return;

        int updatePosition = getDataPosition(updateData);

        if (updatePosition != -1) {
            mDataList.set(updatePosition, updateData);

            // If Header exists, change index position
            updatePosition += useHeader() ? getHeaderCount() : 0;

            notifyItemChanged(updatePosition);
        }
    }

    /**
     * Delete one data
     *
     * @param deleteData data to delete
     */
    @Override
    public void deleteOneData(T deleteData) {
        if (mDataList == null) return;
        if (deleteData == null) return;
        if (deleteData instanceof Collection) return;

        int deletePosition = getDataPosition(deleteData);

        if (deletePosition != -1) {
            mDataList.remove(deletePosition);

            // If Header exists, change index position
            deletePosition += useHeader() ? getHeaderCount() : 0;

            notifyItemRemoved(deletePosition);
        }
    }

    //---------------------------------------------------------------------------------
    // Daniel (2017-03-27 18:43:52): methods to get primitive data
    //---------------------------------------------------------------------------------

    public H getHeaderData() throws Exception {
        return mHeaderData;
    }

    public F getFooterData() throws Exception {
        return mFooterData;
    }

    @Override
    public T getData(int position) throws Exception {
        if (useHeader())
            return mDataList.get(position - getHeaderCount());
        else
            return mDataList.get(position);
    }

    @Override
    public String getId(int position) throws Exception {
        if (useHeader())
            return mDataList.get(position - getHeaderCount()).getUniqueId();
        else
            return mDataList.get(position).getUniqueId();
    }


}
