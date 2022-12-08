package am.aua.textures;

public class Texture {

    private int textureID;

    private float shininess = 1; // shine damper
    private float specularStrength = 0; // reflecitivy

    public Texture (int texture){
        this.textureID = texture;
    }

    public int getID() {
        return textureID;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }

    public void setReflectivity(float specularStrength) {
        this.specularStrength = specularStrength;
    }

}
