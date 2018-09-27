package csc3095.project.setnet.components;

import csc3095.project.setnet.components.core.SetnetObject;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class Place extends SetnetObject
{
    @XmlElement(name = "token")
    private boolean token;

    @XmlIDREF
    @XmlAttribute(name = "incoming")
    private Set<Transition> incomingTransitions = new HashSet<>();

    @XmlIDREF
    @XmlAttribute(name = "outgoing")
    private Set<Transition> outgoingTransitions = new HashSet<>();

    @SuppressWarnings("unused")
    private Place() { super(""); }

    public Place(String name)
    {
        super(name);
        token = false;
    }

    public final Set<Transition> getIncomingTransitions() { return incomingTransitions; }

    public final Set<Transition> getOutgoingTransitions() { return outgoingTransitions; }

    public final void addIncomingTransition(Transition t) { incomingTransitions.add(t); }

    public final void removeIncomingTransition(Transition t) { incomingTransitions.remove(t); }

    public final void addOutgoingTransition(Transition t) { outgoingTransitions.add(t); }

    public final void removeOutgoingTransition(Transition t) { outgoingTransitions.remove(t); }

    public final void addToken() { token = true; }

    public final void removeToken() { token = false; }

    public final boolean hasToken() { return token; }

    public boolean compareWith(Place rhs) { return this.getName() == rhs.getName(); }
}
