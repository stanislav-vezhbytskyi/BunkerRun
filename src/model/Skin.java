package model;

public class Skin {
    private int id;
    private String title;
    private String image;
    private int price;
    private String sprite;
    private boolean isBought;
    private boolean isPicked;
    public Skin(int id, String title, String image, int price, String sprite, boolean isBought, boolean isPicked) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.sprite = sprite;
        this.isBought = isBought;
        this.isPicked = isPicked;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getImage() {
        return image;
    }
    public int getPrice() {
        return price;
    }
    public String getSprite() {
        return sprite;
    }
    public boolean getIsBought() {
        return isBought;
    }
    public boolean getIsPicked() {
        return isPicked;
    }
}
