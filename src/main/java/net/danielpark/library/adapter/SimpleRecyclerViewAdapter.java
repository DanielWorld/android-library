package net.danielpark.library.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import net.danielpark.library.model.ModelInterface;
import net.danielpark.library.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * RecyclerView adapter which contains only one viewHolder. <br>
 *     No Header / Footer.
 * <br><br>
 * Created by namgyu.park on 2017. 10. 17..
 */

public abstract class SimpleRecyclerViewAdapter<T extends ModelInterface, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected ArrayList<T> mDataList = new ArrayList<>();
    protected String mNextDataList;

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * Get next url for next data. <br>
     *     If there is no url, then throw exception
     *
     * @return
     * @throws Exception
     */
    public String getNextUrl() throws Exception {
        if (StringUtil.isNullorEmpty(mNextDataList))
            throw new Exception("No next data url!");

        return mNextDataList;
    }

    /**
     * Clear all data (Make sure to call this before updating all data)
     */
    public void clearData() {
        // TODO: Daniel (2017-05-29 11:18:37): final 로 처리하면 null 걱정은 없을텐데..
        if (mDataList == null) mDataList = new ArrayList<>();

        mDataList.clear();
        mNextDataList = null;

        notifyDataSetChanged();
    }

    /**
     * Update all new data
     *
     * @param newData
     * @param newNextUrl
     */
    public void updateDataList(ArrayList<T> newData, String newNextUrl) {
        if (mDataList == null) mDataList = new ArrayList<>();
        if (newData == null) return;

        mDataList.clear();
        mDataList.addAll(newData);

        mNextDataList = newNextUrl;
        notifyDataSetChanged();
    }

    /**
     * add new data to current data. <br>
     *     load next data
     *
     * @param newData    It should implement List<E>.
     * @param newNextUrl
     */
    public void addDataList(ArrayList<T> newData, String newNextUrl) {
        if (mDataList == null) return;
        if (newData == null) return;

        int curSize = getItemCount();

        try {
            // Delete duplicate data (check data using 'id' by max new data' size times)
            // TIP. HashMap, LinkedHashMap (guarantee insertion order), HashTable (guarnatee synchronized but might occur overhead)
            // which doesn't allow duplicate key and they don't support indexOf().
            // Which means ArrayList.get() => O(1) is better than them.

            final int newDataSize = newData.size();
            for (int i = curSize - 1; i >= 0 && i >= curSize - newDataSize; i--) {
                Iterator<T> it = newData.iterator();
                if (!it.hasNext()) {
//				Log.i("OKAY", "No data in  " + i);
                    break;
                }
                while (it.hasNext()) {
//				Log.v("OKAY", "Current i : " + i);
                    if (it.next().getId() == mDataList.get(i).getId()) {
//					Log.e("OKAY", "Remove : " + i);
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------

        mDataList.addAll(newData);
        mNextDataList = newNextUrl;

        notifyItemRangeInserted(curSize, newData.size());
    }

    /**
     * Add new data to current data at index in List. <br>
     *     load next page. no checking duplicate data </p>
     *
     * @param newData       It should implement List<E>.
     * @param newNextUrl    next url for data
     * @param index         position
     */
    public void addDataList(ArrayList<T> newData, String newNextUrl, int index) {
        if (mDataList == null) return;
        if (newData == null) return;

        mDataList.addAll(index, newData);
        mNextDataList = newNextUrl;

        notifyItemRangeInserted(index, newData.size());
    }

    /**
     * Add one data
     *
     * @param addData
     */
    public void addData(T addData) {
        if (mDataList == null) return;
        if (addData == null) return;
        if (addData instanceof Collection) return;

        mDataList.add(addData);

        int addPosition = mDataList.size() - 1;

        notifyItemInserted(addPosition);
    }

    /**
     * Add one data at index
     *
     * @param addData
     * @param index
     */
    public void addData(T addData, int index) {
        if (mDataList == null) return;
        if (addData == null) return;
        if (addData instanceof Collection) return;

        if (index > mDataList.size() || index < 0) {
            mDataList.add(addData);

            int addPosition = mDataList.size() - 1;

            notifyItemInserted(addPosition);
        } else {
            mDataList.add(index, addData);

            notifyItemInserted(index);
        }
    }

    /**
     * Update one data
     *
     * @param updateData simple object. not List<E>
     */
    public void updateOneData(T updateData) {
        if (mDataList == null) return;
        if (updateData == null) return;
        if (updateData instanceof Collection) return;

        int updatePosition = getDataPosition(updateData);

        if (updatePosition != -1) {
            mDataList.set(updatePosition, updateData);

            notifyItemChanged(updatePosition);
        }
    }

    /**
     * Delete one data
     *
     * @param deleteData
     */
    public void deleteOneData(T deleteData) {
        if (mDataList == null) return;
        if (deleteData == null) return;
        if (deleteData instanceof Collection) return;

        int deletePosition = getDataPosition(deleteData);

        if (deletePosition != -1) {
            mDataList.remove(deletePosition);

            notifyItemRemoved(deletePosition);
        }
    }

    /**
     * Get index from {@link #mDataList}
     *
     * @param newData   simple object, not List<E>
     * @return
     */
    public int getDataPosition(T newData) {
        for (int index = 0; index < mDataList.size(); index++) {
            if (newData.getId() == mDataList.get(index).getId()) {
                return index;
            }
        }
        return -1;
    }

    //---------------------------------------------------------------------------------
    // Daniel (2017-03-27 18:43:52): methods to get primitive data
    //---------------------------------------------------------------------------------

    public T getData(int position) throws Exception {
        return mDataList.get(position);
    }

    /**
     * Get data by 'id'
     * @param id
     * @return
     * @throws Exception
     */
    public @Nullable
    T getDataById(int id) throws Exception {
        for (int i = 0; i < mDataList.size(); i++) {
            if (id == mDataList.get(i).getId()) {
                return mDataList.get(i);
            }
        }
        return null;
    }

    /**
     * Get unique 'id' in data
     * @param position
     * @return
     * @throws Exception
     */
    public int getId(int position) throws Exception {
        return mDataList.get(position).getId();
    }

    public int getUserId(int position) throws Exception {
		return mDataList.get(position).getUserId();
	}
}
