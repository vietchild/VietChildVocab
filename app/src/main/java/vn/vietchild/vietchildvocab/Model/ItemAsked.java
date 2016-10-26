package vn.vietchild.vietchildvocab.Model;

/**
 * Created by Nguyen Phung Hung on 08/10/16.
 */

public class ItemAsked {
    String itemalias;
    Boolean rightanswer;

    public ItemAsked(String itemalias, Boolean rightanswer) {
        this.itemalias = itemalias;
        this.rightanswer = rightanswer;
    }

    public ItemAsked() {
    }

    public String getItemalias() {
        return itemalias;
    }

    public void setItemalias(String itemalias) {
        this.itemalias = itemalias;
    }

    public Boolean getRightanswer() {
        return rightanswer;
    }

    public void setRightanswer(Boolean rightanswer) {
        this.rightanswer = rightanswer;
    }
}
