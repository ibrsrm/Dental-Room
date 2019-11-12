package com.ibrsrm.dentalroom.model.Repository.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class HashList<K, V> {

    protected final ArrayList<V> mList;
    protected final Map<K, V> mMap;

    public HashList() {
        mList = new ArrayList<V>();
        mMap = new HashMap<K, V>();
    }

    public V getListItem(int index) {
        return mList.get(index);
    }

    public V getMapItem(K key) {
        return mMap.get(key);
    }

    public ArrayList<V> getList () {
        return mList;
    }

    public Map<K, V> getMap () {
        return mMap;
    }

    public void clear() {
        mList.clear();
        mMap.clear();
    }

    public abstract void addItem(V value);

    public abstract void reorder(String groupID, int position);
}
