package csc3095.project.setnet.components;

import csc3095.project.setnet.components.core.SetnetObject;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Arc extends SetnetObject
{
    @XmlAttribute
    @XmlIDREF
    private final Place place;

    @XmlAttribute
    @XmlIDREF
    private final Transition transition;

    @XmlElement
    private final Flow flow;

    @SuppressWarnings("unused")
    private Arc()
    {
        super("");
        place = null;
        transition = null;
        flow = null;
    }

    public Arc(String name, Place place, Transition transition, Flow flow)
    {
        super(name);
        this.place = place;
        this.transition = transition;
        this.flow = flow;
        updateTransition();
    }

    public final Place getPlace() { return place; }

    public final Transition getTransition() { return transition; }

    public final Flow getFlow() { return flow; }

    public final boolean canBeFired() { return flow.canBeFired(place); }

    public final void initiateFire() { if (canBeFired()) flow.performFire(place); }

    protected final void updateTransition()
    {
        if (flow == Flow.PLACE_TO_TRANSITION)
        {
            transition.addIncomingArc(this);
            place.addOutgoingTransition(transition);
        }
        else if (flow == Flow.TRANSITION_TO_PLACE)
        {
            transition.addOutgoingArc(this);
            place.addIncomingTransition(transition);
        }
        else                                        throw new IllegalStateException();
    }

    public boolean compareWith(Arc rhs) { return this.place == rhs.place && this.transition == rhs.transition && this.flow == rhs.flow; }
}
