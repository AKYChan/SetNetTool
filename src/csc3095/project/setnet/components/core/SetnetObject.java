package csc3095.project.setnet.components.core;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;

public class SetnetObject
{
    private String name;
    private double x;
    private double y;

    public SetnetObject(String name) { this.name = name; }

    public void setName(String name) { this.name = name; }

    @XmlAttribute
    @XmlID
    public String getName() { return name; }

    @XmlElement
    public final double getX() { return x; }

    @XmlElement
    public final double getY() { return y; }

    public final void setX(double x) { this.x = x; }

    public final void setY(double y) { this.y = y; }

    @Override
    public String toString() { return name; }
}
