package exp.rx.fsapi.box.data;

public class BoxFileRequest {

    String name;
    BoxResource parent;

    public BoxFileRequest(String name, BoxResource parent) {
        this.name = name;
        this.parent = parent;
    }
}
