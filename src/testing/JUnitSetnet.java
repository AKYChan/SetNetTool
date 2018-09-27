//package testing;
//
//import chan.project.setnet.components.Arc;
//import chan.project.setnet.components.Flow;
//import chan.project.setnet.components.Place;
//import chan.project.setnet.components.Transition;
//import chan.project.setnet.main.Setnet;
//
//import chan.project.setnet.save.XMLHandler;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//
//public class JUnitSetnet
//{
//    private Setnet setnet = null;
//
//    @Test
//    public void testingSetnetInstantiation()
//    {
//        setnet = new Setnet();
//        assert setnet != null;
//    }
//
//    @Test
//    public void testingPlaceCreation()
//    {
//        setnet = new Setnet();
//        assert setnet.getPlaces().isEmpty();  //Ensure that the set of Places in the Setnet is empty first
//
//        setnet.createPlace();
//        assert !setnet.getPlaces().isEmpty(); //Ensure that the set of Places in the Setnet is no longer empty
//    }
//
//    @Test
//    public void testingTransitionCreation()
//    {
//        setnet = new Setnet();
//        assert setnet.getTransitions().isEmpty(); //Ensure that the set of Transitions in the Setnet is empty first
//
//        setnet.createTransition();
//        assert !setnet.getTransitions().isEmpty(); //Ensure that the set of Transitions in the Setnet is no longer empty
//    }
//
//    @Test
//    public void testingArcCreation()
//    {
//        setnet = new Setnet();
//
//        setnet.createPlace();
//        setnet.createPlace();
//        setnet.createPlace();
//        assert !setnet.getPlaces().isEmpty();
//
//        setnet.createTransition();
//        setnet.createTransition();
//        assert !setnet.getTransitions().isEmpty();
//
//        Place p0 = null, p1 = null, p2 = null;
//        for (Place p: setnet.getPlaces())
//        {
//            switch (p.getName())
//            {
//                case "p0": p0 = p; break;
//                case "p1": p1 = p; break;
//                case "p2": p2 = p; break;
//                default: assert false; break;
//            }
//        }
//
//        Transition t0 = null, t1 = null;
//        for (Transition t: setnet.getTransitions())
//        {
//            switch (t.getName())
//            {
//                case "t0": t0 = t; break;
//                case "t1": t1 = t; break;
//                default: assert false; break;
//            }
//        }
//
//        boolean checkIfAnyComponentsAreNull = p0 == null || p1 == null || p2 == null || t0 == null || t1 == null;
//
//        assert !checkIfAnyComponentsAreNull;
//        if (!checkIfAnyComponentsAreNull)
//        {
//            setnet.createArc(p0, t0, Flow.PLACE_TO_TRANSITION);
//            setnet.createArc(p0, t1, Flow.PLACE_TO_TRANSITION);
//            setnet.createArc(p1, t0, Flow.TRANSITION_TO_PLACE);
//            setnet.createArc(p2, t1, Flow.TRANSITION_TO_PLACE);
//
//            boolean checkIfArcsInstantiated = setnet.getArcs().isEmpty();
//            assert !checkIfArcsInstantiated;
//            if (!checkIfAnyComponentsAreNull)
//            {
//                boolean passable = true;
//                for (Arc a: setnet.getArcs())
//                {
//                    switch (a.getName())
//                    {
//                        case "a0": passable = passable & arcChecker(a, p0, t0, Flow.PLACE_TO_TRANSITION); break;
//                        case "a1": passable = passable & arcChecker(a, p0, t1, Flow.PLACE_TO_TRANSITION); break;
//                        case "a2": passable = passable & arcChecker(a, p1, t0, Flow.TRANSITION_TO_PLACE); break;
//                        case "a3": passable = passable & arcChecker(a, p2, t1, Flow.TRANSITION_TO_PLACE); break;
//                        default: assert false; break;
//                    }
//                }
//                assert passable;
//            }
//        }
//        else assert false;
//    }
//
//    @Test
//    public void testingSequentialFire()
//    {
//        setnet = new Setnet();
//
//        Place p0 = null, p1 = null, p2 = null, p3 = null;
//
//        Transition t0 = null, t1 = null;
//
//        for (int p = 0; p < 4; ++p) setnet.createPlace(); //Create places p0, p1, p2 and p3
//        for (Place p: setnet.getPlaces())
//        {
//            switch (p.getName())
//            {
//                case "p0": p0 = p; p.addToken(); break;
//                case "p1": p1 = p; break;
//                case "p2": p2 = p; break;
//                case "p3": p3 = p; break;
//                default: assert false; break;
//            }
//        }
//
//        for (int t = 0; t < 2; ++t) setnet.createTransition(); //Create transitions t0 and t1
//        for (Transition t: setnet.getTransitions())
//        {
//            switch (t.getName())
//            {
//                case "t0": t0 = t; break;
//                case "t1": t1 = t; break;
//                default: assert false; break;
//            }
//        }
//
//        setnet.createArc(p0, t0, Flow.PLACE_TO_TRANSITION); //Creates arcs
//        setnet.createArc(p0, t1, Flow.PLACE_TO_TRANSITION); //a0, a1, a2, a3
//        setnet.createArc(p1, t0, Flow.TRANSITION_TO_PLACE); //and a4 where a0 = p0 -> t0,
//        setnet.createArc(p2, t1, Flow.TRANSITION_TO_PLACE); //a1 = p0 -> t1, a2 = t0 -> p1
//        setnet.createArc(p3, t1, Flow.PLACE_TO_TRANSITION); //a3 = t1 -> p2 and a4 = p3 -> t1
//
//        setnet.seqFire();
//
//        //After sequentially firing, p0 should no longer have a token while p1 does.
//        //However, p2 does not because p3 (which is without a token) blocks t1 from
//        //firing and hence, makes p2 and p3 retain a state of no tokens.
//        assert (!p0.hasToken() && p1.hasToken() && !p2.hasToken() && !p3.hasToken());
//    }
//
//    @Test
//    public void testingMaxFire()
//    {
//        setnet = new Setnet();
//
//        Place p0 = null, p1 = null, p2 = null;
//
//        Transition t0 = null, t1 = null;
//
//        for (int p = 0; p < 3; ++p) setnet.createPlace(); //Create places p0, p1, p2 and p3
//        for (Place p: setnet.getPlaces())
//        {
//            switch (p.getName())
//            {
//                case "p0": p0 = p; p.addToken(); break;
//                case "p1": p1 = p; break;
//                case "p2": p2 = p; break;
//                default: assert false; break;
//            }
//        }
//
//        for (int t = 0; t < 2; ++t) setnet.createTransition(); //Create transitions t0 and t1
//        for (Transition t: setnet.getTransitions())
//        {
//            switch (t.getName())
//            {
//                case "t0": t0 = t; break;
//                case "t1": t1 = t; break;
//                default: assert false; break;
//            }
//        }
//
//        setnet.createArc(p0, t0, Flow.PLACE_TO_TRANSITION); //Creates arcs
//        setnet.createArc(p0, t1, Flow.PLACE_TO_TRANSITION); //a0, a1, a2 and a3
//        setnet.createArc(p1, t0, Flow.TRANSITION_TO_PLACE); //where a0 = p0 -> t0, a1 = p0 -> t1,
//        setnet.createArc(p2, t1, Flow.TRANSITION_TO_PLACE); //a2 = t0 -> p1 and a3 = t1 -> p2
//
//        setnet.maxFire();
//
//        //After maximally firing, p0 should no longer have a token while both p1
//        //and p2 gains a token since both transitions are enabled. This is the case
//        //for any other Set-nets as long as every transition that has a set of preceding places
//        //(with tokens) is enabled meaning it can be fired.
//        assert (!p0.hasToken() && p1.hasToken() && p2.hasToken());
//    }
//
//    @Test
//    public void testingFailSequentialFire()
//    {
//        setnet = new Setnet();
//
//        Place p0 = null, p1 = null, p2 = null;
//
//        Transition t0 = null, t1 = null;
//
//        for (int p = 0; p < 3; ++p) setnet.createPlace(); //Create places p0, p1, p2 and p3
//        for (Place p: setnet.getPlaces())
//        {
//            switch (p.getName())
//            {
//                case "p0": p0 = p; break;
//                case "p1": p1 = p; break;
//                case "p2": p2 = p; break;
//                default: assert false; break;
//            }
//        }
//
//        for (int t = 0; t < 2; ++t) setnet.createTransition(); //Create transitions t0 and t1
//        for (Transition t: setnet.getTransitions())
//        {
//            switch (t.getName())
//            {
//                case "t0": t0 = t; break;
//                case "t1": t1 = t; break;
//                default: assert false; break;
//            }
//        }
//
//        setnet.createArc(p0, t0, Flow.PLACE_TO_TRANSITION); //Creates arcs
//        setnet.createArc(p0, t1, Flow.PLACE_TO_TRANSITION); //a0, a1, a2 and a3
//        setnet.createArc(p1, t0, Flow.TRANSITION_TO_PLACE); //where a0 = p0 -> t0, a1 = p0 -> t1,
//        setnet.createArc(p2, t1, Flow.TRANSITION_TO_PLACE); //a2 = t0 -> p1 and a3 = t1 -> p2
//
//        setnet.seqFire();
//
//        //Using the same Set-net from the successful maximal firing test case, this time all places do not
//        //gain a token. This is because if there are no tokens that can enable the transitions
//        //(for example, place p0 now has no tokens) then no transitions can fire and hence has a deadlock.
//        assert (!p0.hasToken() && !p1.hasToken() && !p2.hasToken());
//    }
//
//    @Test
//    public void testingFailMaximalFire()
//    {
//        setnet = new Setnet();
//
//        Place p0 = null, p1 = null, p2 = null, p3 = null;
//
//        Transition t0 = null, t1 = null;
//
//        for (int p = 0; p < 4; ++p) setnet.createPlace(); //Create places p0, p1, p2 and p3
//        for (Place p: setnet.getPlaces())
//        {
//            switch (p.getName())
//            {
//                case "p0": p0 = p; p.addToken(); break;
//                case "p1": p1 = p; break;
//                case "p2": p2 = p; break;
//                case "p3": p3 = p; break;
//                default: assert false; break;
//            }
//        }
//
//        for (int t = 0; t < 2; ++t) setnet.createTransition(); //Create transitions t0 and t1
//        for (Transition t: setnet.getTransitions())
//        {
//            switch (t.getName())
//            {
//                case "t0": t0 = t; break;
//                case "t1": t1 = t; break;
//                default: assert false; break;
//            }
//        }
//
//        setnet.createArc(p0, t0, Flow.PLACE_TO_TRANSITION); //Creates arcs
//        setnet.createArc(p0, t1, Flow.PLACE_TO_TRANSITION); //a0, a1, a2, a3
//        setnet.createArc(p1, t0, Flow.TRANSITION_TO_PLACE); //and a4 where a0 = p0 -> t0,
//        setnet.createArc(p2, t1, Flow.TRANSITION_TO_PLACE); //a1 = p0 -> t1, a2 = t0 -> p1
//        setnet.createArc(p3, t1, Flow.PLACE_TO_TRANSITION); //a3 = t1 -> p2 and a4 = p3 -> t1
//
//        setnet.maxFire();
//
//        //Using the same Set-net from the successful sequential firing test case, the Set-net can no longer
//        //fire the transitions, even if one might be enabled. For this example, t0 is enabled because of p0. However,
//        //t1 is not enabled because it is connected by both p0 and p3, which has no token. Due to this, t1 is disabled
//        //and based on the maximal concurrency principle, t0 also cannot fire and hence, p0 retains its token
//        //while p1, p2 and p3 have no tokens.
//        assert (p0.hasToken() && !p1.hasToken() && !p2.hasToken() && !p3.hasToken());
//    }
//
//    private boolean arcChecker(Arc a, Place p, Transition t, Flow f) { return a.getPlace() == p && a.getTransition() == t && a.getFlow() == f; }
//}
