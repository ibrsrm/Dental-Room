package com.ibrsrm.dentalroom.model.Repository.Common;

public class ContactCollection extends HashList<String, Contact> {

    public ContactCollection() {
        super();
    }

    @Override
    public void addItem(Contact contact) {
        /* TODO add sorted */
        this.mList.add(contact);
        this.mMap.put(contact.getMail(), contact);
    }

    @Override
    public void reorder(String groupID, int position) {
        /* TODO implement reorder */
    }

}
