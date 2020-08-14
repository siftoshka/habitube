package az.siftoshka.habitube.entities.explore;

public class Media {

    private int id;
    private int background;
    private int image;
    private int text;

    public Media(int id, int background, int image, int text) {
        this.id = id;
        this.background = background;
        this.image = image;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
