package com.ibrsrm.dentalroom.model.Repository.Common;

public class GroupCollection extends HashList<String, Group> {

    public GroupCollection () {
        super();
    }

    @Override
    public void addItem(Group group) {
        /* TODO add sorted */
        this.mList.add(group);
        this.mMap.put(group.getUid(), group);
    }

    @Override
    public void reorder(String groupID, int position) {
        int itemPos = -1;
        for (Group group : mList) {
            if (group.getUid().equals(groupID)) {
                itemPos = mList.indexOf(group);
                break;
            }
        }
        if (itemPos > 0) {
            Group gr = mList.get(itemPos);
            mList.remove(itemPos);
            mList.add(position, gr);
        }
    }

}
