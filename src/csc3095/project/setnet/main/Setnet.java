package csc3095.project.setnet.main;

import csc3095.project.setnet.components.Arc;
import csc3095.project.setnet.components.Flow;
import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.components.Transition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Setnet
{
    @XmlElement
    private int PLACE_COUNTER = 0;

    @XmlElement
    private int TRANSITION_COUNTER = 0;

    @XmlElement
    private int ARC_COUNTER = 0;

    @XmlElement(name = "place")
    private final Set<Place> places = new HashSet<>();

    @XmlElement(name = "transition")
    private final Set<Transition> transitions = new HashSet<>();

    @XmlElement(name = "arc")
    private final Set<Arc> arcs = new HashSet<>();

    public Set<Place> getPlaces() { return places; }

    public Set<Transition> getTransitions() { return transitions; }

    public Set<Arc> getArcs() { return arcs; }

    public Place createPlace()
    {
        Place p = new Place("p" + PLACE_COUNTER);
        ++PLACE_COUNTER;
        places.add(p);
        return p;
    }

    public Transition createTransition()
    {
        Transition t = new Transition("t" + TRANSITION_COUNTER);
        ++TRANSITION_COUNTER;
        transitions.add(t);
        return t;
    }

    public Arc createArc(Place p, Transition t, Flow f)
    {
        Arc a = new Arc("a" + ARC_COUNTER, p, t, f);
        boolean alreadyExists = false;

        for (Arc arc: arcs) alreadyExists = alreadyExists | arc.compareWith(a);
        if (!alreadyExists)
        {
            ++ARC_COUNTER;
            arcs.add(a);
            return a;
        }
        else return null;
    }

    public void removePlace(Place p)
    {
        places.remove(p);
        for (Arc a: arcs) if (a.getPlace() == p) arcs.remove(a);
    }

    public void removeTransition(Transition t)
    {
        transitions.remove(t);
        for (Arc a: arcs) if (a.getTransition() == t) arcs.remove(a);
    }

    public void removeArc(Arc a)
    {
        arcs.remove(a);
        if (a.getFlow() == Flow.PLACE_TO_TRANSITION)
        {
            a.getTransition().removeIncomingArc(a);
            a.getPlace().removeOutgoingTransition(a.getTransition());
        }
        else if (a.getFlow() == Flow.TRANSITION_TO_PLACE)
        {
            a.getTransition().removeOutgoingArc(a);
            a.getPlace().removeIncomingTransition(a.getTransition());
        }
    }

    public void seqFire()
    {
        Set<Place> markings = new HashSet<>();
        Set<Transition> fireableTransitions = new HashSet<>();

        for (Place p: places) if (p.hasToken()) markings.add(p);

        for (Place m: markings)
        {
            for (Transition t: m.getOutgoingTransitions())
            {
                if (t.isFireable()) fireableTransitions.add(t);
            }
        }
        for (Transition t: transitions) if (t.getIncoming().isEmpty()) fireableTransitions.add(t);

        for (Transition t: fireableTransitions) t.fire();
    }

    public void maxFire()
    {
        Set<Place> markings = new HashSet<>();
        Set<Transition> fireableTransitions = new HashSet<>();

        for (Place p: places) if (p.hasToken()) markings.add(p);

        boolean canMaxFire = true;
        for (Place m: markings)
        {
            for (Transition t: m.getOutgoingTransitions())
            {
                canMaxFire = canMaxFire & t.isFireable();
                if (t.isFireable()) fireableTransitions.add(t);
            }
        }
        for (Transition t: transitions) if (t.getIncoming().isEmpty()) fireableTransitions.add(t);

        if (canMaxFire)
        {
            for (Transition t: fireableTransitions)
            {
                t.fire();
            }
        }
    }

    public boolean isSequentiallyFireable()
    {
        Set<Place> markings = new HashSet<>();
        for (Place p: places) if (p.hasToken()) markings.add(p);

        for (Place m: markings)
        {
            for (Transition t: m.getOutgoingTransitions())
            {
                if (t.isFireable()) return true;
            }
        }
        return false;
    }

    public boolean isMaximallyFireable()
    {
        Set<Place> markings = new HashSet<>();
        for (Place p: places) if (p.hasToken()) markings.add(p);

        boolean canMaxFire = true;
        for (Place m: markings)
        {
            for (Transition t: m.getOutgoingTransitions())
            {
                canMaxFire = canMaxFire & t.isFireable();
            }
        }
        return canMaxFire;
    }

    public boolean verifyDeadlock()
    {
        boolean noExistingTokens = false;

        for (Place p: places) noExistingTokens = noExistingTokens | p.hasToken();

        if (!noExistingTokens) return true;
        else
        {
            Set<Place> siphon = new HashSet<>();
            Set<Place> trap = new HashSet<>();

            for (Place p: places)
            {
                if (p.hasToken())
                {
                    for (Transition t: p.getOutgoingTransitions()) if (!t.isFireable()) siphon.add(p);
                    for (Transition t: p.getIncomingTransitions()) if (t.getIncoming().isEmpty()) trap.add(p);
                }
                else
                {
                    if (p.getIncomingTransitions().isEmpty() && !p.getOutgoingTransitions().isEmpty()) return true;
                    else trap.add(p);
                }
            }

            return !trap.containsAll(siphon) && !siphon.isEmpty() && !trap.isEmpty();
        }
    }
}
