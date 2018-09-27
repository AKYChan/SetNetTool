package csc3095.project.setnet.components;

import csc3095.project.setnet.components.core.SetnetObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class Transition extends SetnetObject
{
    @XmlAttribute
    @XmlIDREF
    private final Set<Arc> incoming = new HashSet<Arc>();

    @XmlAttribute
    @XmlIDREF
    private final Set<Arc> outgoing = new HashSet<Arc>();

    @SuppressWarnings("unused")
    private Transition() { super(""); }

    public Transition(String name) { super(name); }

    public final Set<Arc> getIncoming() { return incoming; }

    public final Set<Arc> getOutgoing() { return outgoing; }

    public final void addIncomingArc(Arc a) { incoming.add(a); }

    public final void removeIncomingArc(Arc a) { incoming.remove(a); }

    public final void addOutgoingArc(Arc a) { outgoing.add(a); }

    public final void removeOutgoingArc(Arc a) { outgoing.remove(a); }

    public final boolean isFireable()
    {
        if (outgoing.isEmpty()) return false;
        else
        {
            boolean fireable = true;
            for (Arc a: incoming) fireable = fireable & a.canBeFired();
            return fireable;
        }
    }

    public final void fire()
    {
        for (Arc a: incoming) a.initiateFire();
        for (Arc a: outgoing) a.initiateFire();
    }

    public boolean compareWith(Transition rhs) { return this.getName() == rhs.getName(); }
}
