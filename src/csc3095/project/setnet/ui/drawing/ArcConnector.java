package csc3095.project.setnet.ui.drawing;

import csc3095.project.setnet.components.Arc;
import csc3095.project.setnet.components.Flow;
import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.main.Setnet;

public enum ArcConnector
{
    PLACE_SELECTED
            {
                @Override
                public Arc connect(Setnet setnet, Place p, Transition t)
                {
                    return setnet.createArc(p, t, Flow.PLACE_TO_TRANSITION);
                }
            },

    TRANSITION_SELECTED
            {
                @Override
                public Arc connect(Setnet setnet, Place p, Transition t)
                {
                    return setnet.createArc(p, t, Flow.TRANSITION_TO_PLACE);
                }
            },

    NONE_SELECTED
            {
                @Override
                Arc connect(Setnet setnet, Place p, Transition t) { return null; }
            };

    abstract Arc connect(Setnet setnet, Place p, Transition t);
}
