package kr.hs.emirim.ham.firebasestart.realtimedb;

public class MemoItem {
    private String user;
    private String tilte;
    private String memocontents;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getMemocontents() {
        return memocontents;
    }

    public void setMemocontents(String memocontents) {
        this.memocontents = memocontents;
    }
}
