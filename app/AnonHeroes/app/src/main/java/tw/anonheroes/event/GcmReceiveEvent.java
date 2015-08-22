package tw.anonheroes.event;

/**
 * Created by ivan on 8/23/15.
 */
public class GcmReceiveEvent {

    private String result = "";
    private String url = "";
    private boolean is119 = false;
    private boolean is110 = false;
    private boolean is113 = false;
    private int major =1;
    private int minor =1;

    public GcmReceiveEvent(String result){
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean is119() {
        return is119;
    }

    public void setIs119(boolean is119) {
        this.is119 = is119;
    }

    public boolean is110() {
        return is110;
    }

    public void setIs110(boolean is110) {
        this.is110 = is110;
    }

    public boolean is113() {
        return is113;
    }

    public void setIs113(boolean is113) {
        this.is113 = is113;
    }

    public int getMajor() { return major;}

    public void setMajor(int major) {this.major = major;}

    public int getMinor() {return minor;}

    public void setMinor(int minor) {this.minor = minor;}
}
